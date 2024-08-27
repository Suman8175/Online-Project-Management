package com.suman.projectManager.controller;


import com.suman.projectManager.exception.AlreadyExistsException;
import com.suman.projectManager.service.impls.UserServiceImpls;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class DemoController {

    @GetMapping
    public String getDemo(){
        System.out.println("Start");
        return "Hi";
    }
    @GetMapping("/test-conflict")
    public void testConflict() {
        throw new AlreadyExistsException("User already exists");
    }

}
