package com.saas.workspaces.controller;

import com.saas.workspaces.dto.request.LoginRequest;
import com.saas.workspaces.dto.request.TokenRequest;
import com.saas.workspaces.dto.response.LoginResponse;
import com.saas.workspaces.dto.response.TokenResponse;
import com.saas.workspaces.service.AuthService;
import com.saas.workspaces.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Autentica al usuario y retorna sus datos + workspaces disponibles
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
    }

    /**
     * POST /api/auth/token
     * Genera el token de acceso para el workspace seleccionado
     */
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<TokenResponse>> generateToken(@Valid @RequestBody TokenRequest request) {
        TokenResponse response = authService.generateToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token generado exitosamente", response));
    }
}
