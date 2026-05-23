package com.saas.workspaces.dto.response;

import com.saas.workspaces.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getCreatedBy().getFullName(),
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }
}
