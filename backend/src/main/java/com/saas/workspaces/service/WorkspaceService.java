package com.saas.workspaces.service;

import com.saas.workspaces.dto.response.WorkspaceResponse;
import com.saas.workspaces.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    /**
     * Obtiene todos los workspaces del sistema
     */
    public List<WorkspaceResponse> getAllWorkspaces() {
        return workspaceRepository.findAll().stream()
                .map(WorkspaceResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
