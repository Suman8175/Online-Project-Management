package com.suman.projectManager.service.interfac;

import com.suman.projectManager.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    User findByUserId(Long userId);

    User findByEmail(String email);
    User updateUserProjectSize(User user,int number);


}
