package com.suman.projectManager.config.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenGenerator {


    private final JwtEncoder jwtEncoder;
    @Value("${myapp.jwt.expiration}")
    private int jwtExpirationInMinutes;
    @Value("${myapp.jwt.refresh.expiration}")
    private int jwtRefreshExpirationInDays;

    public String generateAccessToken(Authentication authentication) {

        log.info("[JwtTokenGenerator:generateAccessToken] Token Creation Started for:{}", authentication.getName());


        String roles = getRolesOfUser(authentication);
        List<String> rolesInJWT = getRolesOfUserAsList(authentication);

        String permissions = getPermissionsFromRoles(roles);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("suman")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(jwtExpirationInMinutes , ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", permissions)
                .claim("roles", rolesInJWT)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(Authentication authentication) {

        log.info("[JwtTokenGenerator:generateRefreshToken] Token Creation Started for:{}", authentication.getName());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("my-app")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(jwtRefreshExpirationInDays , ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", "SCOPE_REFRESH_TOKEN")
                .claim("roles","SCOPE_REFRESH_TOKEN")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }




    private static String getRolesOfUser(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    private static List<String> getRolesOfUserAsList(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private String getPermissionsFromRoles(String roles) {
        Set<String> permissions = new HashSet<>();

        if (roles.contains("ROLE_ADMIN")) {
            permissions.addAll(List.of("SCOPE_READ", "SCOPE_WRITE","SCOPE_DELETE"));
        }
        if (roles.contains("ROLE_USER")) {
            permissions.addAll(List.of("SCOPE_READ", "SCOPE_WRITE","SCOPE_DELETE"));
        }

        return String.join(" ", permissions);
    }

}

