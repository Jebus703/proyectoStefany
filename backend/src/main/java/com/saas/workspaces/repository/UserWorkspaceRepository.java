package com.saas.workspaces.repository;

import com.saas.workspaces.entity.UserWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, Long> {

    List<UserWorkspace> findByUserId(Long userId);

    Optional<UserWorkspace> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    boolean existsByUserIdAndWorkspaceId(Long userId, Long workspaceId);
}
