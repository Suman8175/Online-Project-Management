package com.suman.projectManager.service.interfac;

import com.suman.projectManager.dto.request.ProjectRequestDTO;
import com.suman.projectManager.dto.response.ProjectResponseDTO;
import com.suman.projectManager.entity.ProjectEntity;
import com.suman.projectManager.entity.User;

import java.util.List;

public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO project,String email);

    List<ProjectResponseDTO> getInvolvedProject (String email,String tag,String category);

//    ProjectResponseDTO getProjectById(Long projectId);
//
//    boolean deleteProject(Long projectId,Long userId);
//
//    ProjectResponseDTO updateProject(ProjectRequestDTO updateProject,Long projectId);
//
//    boolean addTeamMemberInProject(Long projectId,Long userId,Long ownerId);
//    boolean removeTeamMemberFromProject(Long projectId,Long userId,Long ownerId);

//    ChatEntity getChatByProjectId(Long projectId);

}
