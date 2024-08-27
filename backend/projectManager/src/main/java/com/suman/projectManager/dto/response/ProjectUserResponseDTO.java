package com.suman.projectManager.dto.response;

import lombok.Data;

@Data
public class ProjectUserResponseDTO {
    private Long userId;
    private String userName;
    private String email;
}
