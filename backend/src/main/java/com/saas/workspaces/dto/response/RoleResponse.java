package com.saas.workspaces.dto.response;

import com.saas.workspaces.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean canCreate;
    private Boolean canEdit;
    private Boolean canDelete;

    public static RoleResponse fromEntity(Role role) {
        return new RoleResponse(
            role.getId(),
            role.getName(),
            role.getDescription(),
            role.getCanCreate(),
            role.getCanEdit(),
            role.getCanDelete()
        );
    }
}
