package com.suman.projectManager.service;

import com.suman.projectManager.config.jwt.JwtTokenGenerator;
import com.suman.projectManager.dto.request.UserRegisterDTO;
import com.suman.projectManager.dto.response.AuthResponseDTO;
import com.suman.projectManager.dto.TokenType;
import com.suman.projectManager.entity.RefreshTokenEntity;
import com.suman.projectManager.entity.User;
import com.suman.projectManager.exception.AlreadyExistsException;
import com.suman.projectManager.exception.BadRequest;
import com.suman.projectManager.mapper.UserMapper;
import com.suman.projectManager.repository.RefreshTokenRepository;
import com.suman.projectManager.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {


    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${myapp.jwt.expiration}")
    private int jwtExpirationInMinutes;

    private final AuthenticationManager authenticationManager;
    public Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }


    public AuthResponseDTO getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try
        {
            User userInfoEntity = userRepository.findByEmailId(authentication.getName())
                    .orElseThrow(()->{
                        log.error("[AuthService:userSignInAuth] User :{} not found",authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");});


            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
            saveUserRefreshToken(userInfoEntity,refreshToken);
            createRefreshTokenCookie(response,refreshToken);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",userInfoEntity.getUserName());
            return  AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(jwtExpirationInMinutes*60)
                    .userName(userInfoEntity.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }


    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
        if(!validateRefreshToken(authorizationHeader)){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }
        final String refreshToken = authorizationHeader.substring(7);
        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));
        User userInfoEntity = refreshTokenEntity.getUser();
        //Now create the Authentication object
        Authentication authentication =  createAuthenticationObject(userInfoEntity);
        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return  AuthResponseDTO.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(jwtExpirationInMinutes)
                .userName(userInfoEntity.getEmailId())
                .tokenType(TokenType.Bearer)
                .build();
    }


    public AuthResponseDTO registerUser(UserRegisterDTO userRegisterDTO,HttpServletResponse response) {
        try {
            log.info("[AuthService:registerUser]User Registration Started with :::{}", userRegisterDTO);
            if (!validateUserDetails(userRegisterDTO)){
                throw new BadRequest("Provide all details and password should be more than 6 characters");
            }

            Optional<User> user = userRepository.findByEmailId(userRegisterDTO.getEmail());
            if (user.isPresent()) {
                throw new AlreadyExistsException("User already exists");
            }
            User userDetailsEntity = userMapper.convertToEntity(userRegisterDTO);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            User savedUserDetails = userRepository.save(userDetailsEntity);
            saveUserRefreshToken(userDetailsEntity,refreshToken);

            createRefreshTokenCookie(response,refreshToken);
            log.info("[AuthService:registerUser] User:{} Successfully registered",savedUserDetails.getUserName());
            return   AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(jwtExpirationInMinutes * 60)
                    .userName(savedUserDetails.getEmailId())
                    .tokenType(TokenType.Bearer)
                    .build();

        }
        catch (BadRequest | AlreadyExistsException e) {
            log.error("[AuthService:registerUser] Exception: {}", e.getMessage());
            throw e;  // Let the custom exception handler deal with this
        }
        catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    private boolean validateUserDetails(UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO.getEmail().isEmpty() || userRegisterDTO.getUserName().isEmpty()
                || userRegisterDTO.getPassword().isEmpty() || userRegisterDTO.getRoles().isEmpty()){
            return false;
        }
        if (userRegisterDTO.getPassword().length()<6 ){
            return false;
        }
        return true;
    }


    private void saveUserRefreshToken(User user, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

    }
    private Cookie createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }
    private boolean validateRefreshToken(String authorizationHeader){
        return authorizationHeader.startsWith(TokenType.Bearer.name());
    }
    private static Authentication createAuthenticationObject(User userInfoEntity) {
        // Extract user details from UserDetailsEntity
        String username = userInfoEntity.getEmailId();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }


}
