package com.suman.projectManager.controller;

import com.suman.projectManager.dto.request.ProjectRequestDTO;
import com.suman.projectManager.dto.response.ProjectResponseDTO;
import com.suman.projectManager.entity.ProjectEntity;
import com.suman.projectManager.entity.User;
import com.suman.projectManager.service.impls.ProjectServiceImpls;
import com.suman.projectManager.service.impls.UserServiceImpls;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    private final UserServiceImpls userServiceImpls;
    private final ProjectServiceImpls projectServiceImpls;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/welcome-message")
    public ResponseEntity<String> getFirstWelcomeMessage(Authentication authentication){
        return ResponseEntity.ok("Welcome to the JWT Tutorial:"+authentication.getName()+"with scope:"+authentication.getAuthorities());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/test1/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id,Principal principal){
        return new ResponseEntity<>(userServiceImpls.findByUserId(id),HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO projectRequestDTO, Principal principal){
        return new ResponseEntity<>(projectServiceImpls.createProject(projectRequestDTO,principal.getName()),HttpStatus.OK);

    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin-message")
    public ResponseEntity<User> getAdminData(@RequestParam("message") String message, Principal principal){
        System.out.println("Start..123");
        return new ResponseEntity<>(userServiceImpls.findByEmail(principal.getName()),HttpStatus.OK);


//        return ResponseEntity.ok("Admin::"+principal.getName()+" has this message:"+message);

    }
}