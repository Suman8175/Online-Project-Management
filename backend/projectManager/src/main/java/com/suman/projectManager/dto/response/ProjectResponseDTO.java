package com.suman.projectManager.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectResponseDTO {
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private LocalDateTime createdAt;
    private String category;
    private List<String> tags;
    private ProjectUserResponseDTO admin;
    private List<ProjectUserResponseDTO> teamMembers;
    private Long chatId;

}
