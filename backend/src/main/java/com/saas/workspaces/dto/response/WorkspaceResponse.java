package com.saas.workspaces.dto.response;

import com.saas.workspaces.entity.Workspace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceResponse {

    private Long id;
    private String name;
    private String description;
    private String role; // Rol del usuario en este workspace (opcional)

    public static WorkspaceResponse fromEntity(Workspace workspace) {
        return new WorkspaceResponse(
            workspace.getId(),
            workspace.getName(),
            workspace.getDescription(),
            null
        );
    }

    public static WorkspaceResponse fromEntityWithRole(Workspace workspace, String roleName) {
        return new WorkspaceResponse(
            workspace.getId(),
            workspace.getName(),
            workspace.getDescription(),
            roleName
        );
    }
}
