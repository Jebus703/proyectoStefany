package com.saas.workspaces.service;

import com.saas.workspaces.dto.request.ProjectRequest;
import com.saas.workspaces.dto.response.ProjectResponse;
import com.saas.workspaces.entity.Project;
import com.saas.workspaces.entity.User;
import com.saas.workspaces.entity.Workspace;
import com.saas.workspaces.exception.ForbiddenException;
import com.saas.workspaces.exception.NotFoundException;
import com.saas.workspaces.repository.ProjectRepository;
import com.saas.workspaces.repository.UserRepository;
import com.saas.workspaces.repository.WorkspaceRepository;
import com.saas.workspaces.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * Obtiene todos los proyectos del workspace activo
     */
    public List<ProjectResponse> getProjectsByWorkspace(String token) {
        Long workspaceId = jwtUtil.getWorkspaceId(token);

        return projectRepository.findByWorkspaceIdOrderByCreatedAtDesc(workspaceId).stream()
                .map(ProjectResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo proyecto en el workspace activo
     */
    public ProjectResponse createProject(String token, ProjectRequest request) {
        // Verificar permisos de creacion
        if (!jwtUtil.canCreate(token)) {
            throw new ForbiddenException("No tienes permisos para crear proyectos en este workspace");
        }

        Long workspaceId = jwtUtil.getWorkspaceId(token);
        Long userId = jwtUtil.getUserId(token);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace no encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setWorkspace(workspace);
        project.setCreatedBy(user);

        Project savedProject = projectRepository.save(project);

        return ProjectResponse.fromEntity(savedProject);
    }

    /**
     * Actualiza un proyecto existente
     */
    public ProjectResponse updateProject(String token, Long projectId, ProjectRequest request) {
        // Verificar permisos de edicion
        if (!jwtUtil.canEdit(token)) {
            throw new ForbiddenException("No tienes permisos para editar proyectos en este workspace");
        }

        Long workspaceId = jwtUtil.getWorkspaceId(token);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado"));

        // Verificar que el proyecto pertenece al workspace actual
        if (!project.getWorkspace().getId().equals(workspaceId)) {
            throw new ForbiddenException("No tienes acceso a este proyecto");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        Project savedProject = projectRepository.save(project);

        return ProjectResponse.fromEntity(savedProject);
    }

    /**
     * Elimina un proyecto
     */
    public void deleteProject(String token, Long projectId) {
        // Verificar permisos de eliminacion
        if (!jwtUtil.canDelete(token)) {
            throw new ForbiddenException("No tienes permisos para eliminar proyectos en este workspace");
        }

        Long workspaceId = jwtUtil.getWorkspaceId(token);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado"));

        // Verificar que el proyecto pertenece al workspace actual
        if (!project.getWorkspace().getId().equals(workspaceId)) {
            throw new ForbiddenException("No tienes acceso a este proyecto");
        }

        projectRepository.delete(project);
    }
}
