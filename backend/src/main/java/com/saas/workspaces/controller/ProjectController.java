package com.saas.workspaces.controller;

import com.saas.workspaces.dto.request.ProjectRequest;
import com.saas.workspaces.dto.response.ProjectResponse;
import com.saas.workspaces.exception.UnauthorizedException;
import com.saas.workspaces.service.ProjectService;
import com.saas.workspaces.util.ApiResponse;
import com.saas.workspaces.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final JwtUtil jwtUtil;

    /**
     * GET /api/projects
     * Obtiene los proyectos del workspace activo
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjects(
            @RequestHeader("Authorization") String authHeader) {

        String token = validateAndExtractToken(authHeader);
        List<ProjectResponse> projects = projectService.getProjectsByWorkspace(token);

        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    /**
     * POST /api/projects
     * Crea un nuevo proyecto en el workspace activo
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ProjectRequest request) {

        String token = validateAndExtractToken(authHeader);
        ProjectResponse project = projectService.createProject(token, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Proyecto creado exitosamente", project));
    }

    /**
     * PUT /api/projects/{id}
     * Actualiza un proyecto existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {

        String token = validateAndExtractToken(authHeader);
        ProjectResponse project = projectService.updateProject(token, id, request);

        return ResponseEntity.ok(ApiResponse.success("Proyecto actualizado exitosamente", project));
    }

    /**
     * DELETE /api/projects/{id}
     * Elimina un proyecto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String token = validateAndExtractToken(authHeader);
        projectService.deleteProject(token, id);

        return ResponseEntity.ok(ApiResponse.success("Proyecto eliminado exitosamente", null));
    }

    /**
     * Valida y extrae el token del header Authorization
     */
    private String validateAndExtractToken(String authHeader) {
        String token = jwtUtil.extractTokenFromHeader(authHeader);

        if (token == null || !jwtUtil.validateToken(token)) {
            throw new UnauthorizedException("Token invalido o expirado");
        }

        return token;
    }
}
