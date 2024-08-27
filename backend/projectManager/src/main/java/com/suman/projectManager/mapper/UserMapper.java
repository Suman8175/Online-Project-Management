package com.suman.projectManager.mapper;

import com.suman.projectManager.dto.request.UserRegisterDTO;
import com.suman.projectManager.dto.response.ProjectUserResponseDTO;
import com.suman.projectManager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    public User convertToEntity(UserRegisterDTO userRegistrationDto) {
        User user = new User();
        user.setUserName(userRegistrationDto.getUserName());
        user.setEmailId(userRegistrationDto.getEmail());
        user.setRoles(userRegistrationDto.getRoles());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        return user;
    }

    public ProjectUserResponseDTO mapUserToProjectUserResponseDTO(User user){
        ProjectUserResponseDTO projectUserResponseDTO=new ProjectUserResponseDTO();
        projectUserResponseDTO.setUserId(user.getUserId());
        projectUserResponseDTO.setUserName(user.getUserName());
        projectUserResponseDTO.setEmail(user.getEmailId());
        return projectUserResponseDTO;
    }

}
