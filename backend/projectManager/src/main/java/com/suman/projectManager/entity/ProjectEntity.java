package com.suman.projectManager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PROJECTS")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private LocalDateTime createdAt;
    private String category;
    private List<String> tags;


    @ManyToOne
    @JoinColumn(name = "admin_id",referencedColumnName = "userId")
    private User admin;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "project_team_member_mapping",
               joinColumns = @JoinColumn(name = "projectId"),
               inverseJoinColumns = @JoinColumn(name = "project_member_id"))
    private List<User> projectTeamMember=new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id",referencedColumnName = "chatId")
    private ChatEntity chat;


}
