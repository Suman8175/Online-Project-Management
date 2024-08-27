package com.suman.projectManager.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ProjectRequestDTO {

    private String projectName;
    private String projectDescription;
    private List<String> tags;
    private String category;
}
