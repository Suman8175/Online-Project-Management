package com.suman.projectManager.service.impls;

import com.suman.projectManager.dto.request.ProjectRequestDTO;
import com.suman.projectManager.dto.response.ProjectResponseDTO;
import com.suman.projectManager.entity.ChatEntity;
import com.suman.projectManager.entity.ProjectEntity;
import com.suman.projectManager.entity.User;
import com.suman.projectManager.mapper.ProjectMapper;
import com.suman.projectManager.repository.ProjectRepository;
import com.suman.projectManager.service.interfac.ChatService;
import com.suman.projectManager.service.interfac.ProjectService;
import com.suman.projectManager.service.interfac.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProjectServiceImpls implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final ProjectMapper projectMapper;



    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO, String email) {
        User user=userService.findByEmail(email);
        ChatEntity chat=new ChatEntity();
        ProjectEntity project=new ProjectEntity();
        project.setProjectName(projectRequestDTO.getProjectName());
        project.setProjectDescription(projectRequestDTO.getProjectDescription());
        project.setCreatedAt(LocalDateTime.now());
        project.setCategory(projectRequestDTO.getCategory());
        project.setAdmin(user);
        project.setTags(projectRequestDTO.getTags());
        project.getProjectTeamMember().add(user);
        chatService.createChat(chat);
        project.setChat(chat);
        projectRepository.save(project);
        return projectMapper.mapper(project);

    }

    @Override
    public List<ProjectResponseDTO> getInvolvedProject( String email, String tag, String category) {
        System.out.println("Step:1");
        User user=userService.findByEmail(email);
        System.out.println("Step:2");
        List<ProjectEntity> listOfProjectsInvolved = projectRepository.findByProjectTeamMemberContainingOrAdmin(user, user);
        if (tag!=null){
            listOfProjectsInvolved= listOfProjectsInvolved.stream().filter((project)->project.getTags().contains(tag)).toList();
        }
        System.out.println("Step:3");
        if (category!=null){
            listOfProjectsInvolved=listOfProjectsInvolved.stream().filter((products)->products.getCategory().equals(category)).toList();
        }
        System.out.println("Step:4");
        return listOfProjectsInvolved.stream().map(projectMapper::mapper).toList();
    }

}