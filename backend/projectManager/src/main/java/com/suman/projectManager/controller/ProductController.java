package com.suman.projectManager.controller;

import com.suman.projectManager.dto.request.ProjectRequestDTO;
import com.suman.projectManager.dto.response.ProjectResponseDTO;
import com.suman.projectManager.service.impls.ProjectServiceImpls;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ProductController {
    private final ProjectServiceImpls projectServiceImpls;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO projectRequestDTO, Principal principal){
        return new ResponseEntity<>(projectServiceImpls.createProject(projectRequestDTO,principal.getName()), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/projects")
    public ResponseEntity<?> getAssociatedProjectWithFilter(@RequestParam(required = false) String tags,
                                                            @RequestParam(required = false) String category,
                                                            Principal principal){
        System.out.println("Step:0");
        return new ResponseEntity<>(projectServiceImpls.getInvolvedProject(principal.getName(), tags,category),HttpStatus.OK);
    }

}
