package com.saas.workspaces.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El username es requerido")
    private String username;

    @NotBlank(message = "El password es requerido")
    private String password;
}
