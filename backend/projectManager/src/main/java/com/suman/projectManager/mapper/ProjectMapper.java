package com.suman.projectManager.mapper;

import com.suman.projectManager.dto.request.ProjectRequestDTO;
import com.suman.projectManager.dto.response.ProjectResponseDTO;
import com.suman.projectManager.dto.response.ProjectUserResponseDTO;
import com.suman.projectManager.entity.ChatEntity;
import com.suman.projectManager.entity.ProjectEntity;
import com.suman.projectManager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final UserMapper userMapper;
    public  ProjectResponseDTO mapper(ProjectEntity project){
       ProjectResponseDTO projectResponseDTO =new ProjectResponseDTO();
       projectResponseDTO.setProjectId(project.getProjectId());
       projectResponseDTO.setProjectName(project.getProjectName());
       projectResponseDTO.setProjectDescription(project.getProjectDescription());
       projectResponseDTO.setCategory(project.getCategory());
       projectResponseDTO.setTags(project.getTags());
       projectResponseDTO.setCreatedAt(project.getCreatedAt());
       projectResponseDTO.setAdmin(userMapper.mapUserToProjectUserResponseDTO(project.getAdmin()));
       projectResponseDTO.setTeamMembers(mapTeamMembers(project));
       projectResponseDTO.setChatId(project.getChat().getChatId());
       return projectResponseDTO;

    }

    private List<ProjectUserResponseDTO> mapTeamMembers(ProjectEntity project) {
        return project.getProjectTeamMember()
                .stream()
                .map(userMapper::mapUserToProjectUserResponseDTO)
                .toList();

    }


}
