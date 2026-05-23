package com.saas.workspaces.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {

    @NotNull(message = "El userId es requerido")
    private Long userId;

    @NotNull(message = "El workspaceId es requerido")
    private Long workspaceId;
}
