package com.saas.workspaces.controller;

import com.saas.workspaces.dto.response.WorkspaceResponse;
import com.saas.workspaces.service.WorkspaceService;
import com.saas.workspaces.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * GET /api/workspaces
     * Obtiene todos los workspaces del sistema
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkspaceResponse>>> getAllWorkspaces() {
        List<WorkspaceResponse> workspaces = workspaceService.getAllWorkspaces();
        return ResponseEntity.ok(ApiResponse.success(workspaces));
    }
}
