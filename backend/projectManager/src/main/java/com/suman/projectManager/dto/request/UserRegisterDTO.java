package com.suman.projectManager.dto.request;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String userName;
    private String email;
    private String password;
    private String roles;
}
