package com.saas.workspaces.repository;

import com.saas.workspaces.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByWorkspaceId(Long workspaceId);

    List<Project> findByWorkspaceIdOrderByCreatedAtDesc(Long workspaceId);
}
