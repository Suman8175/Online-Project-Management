package com.suman.projectManager.controller;

import com.suman.projectManager.dto.request.LoginRequestDTO;
import com.suman.projectManager.dto.request.UserRegisterDTO;
import com.suman.projectManager.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    @PostMapping("/public/log-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response){
        Authentication authentication = authService.authenticate(loginRequestDTO.getUserName(), loginRequestDTO.getPassword());

        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication,response));
    }

    @PostMapping("/public/sign-in")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDTO userRegisterDTO,HttpServletResponse httpServletResponse){
        return ResponseEntity.ok(authService.registerUser(userRegisterDTO,httpServletResponse));
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }
}