package com.suman.projectManager.repository;

import com.suman.projectManager.entity.ProjectEntity;
import com.suman.projectManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {


    //Like we want to fetch all projects in which we are right?either we are teamMemberOrAdmin
    List<ProjectEntity> findByProjectTeamMemberContainingOrAdmin(User user,User admin);


}
