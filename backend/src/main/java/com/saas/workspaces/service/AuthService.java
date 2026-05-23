package com.saas.workspaces.service;

import com.saas.workspaces.dto.request.LoginRequest;
import com.saas.workspaces.dto.request.TokenRequest;
import com.saas.workspaces.dto.response.*;
import com.saas.workspaces.entity.Role;
import com.saas.workspaces.entity.User;
import com.saas.workspaces.entity.UserWorkspace;
import com.saas.workspaces.entity.Workspace;
import com.saas.workspaces.exception.NotFoundException;
import com.saas.workspaces.exception.UnauthorizedException;
import com.saas.workspaces.repository.UserRepository;
import com.saas.workspaces.repository.UserWorkspaceRepository;
import com.saas.workspaces.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final JwtUtil jwtUtil;

    /**
     * Autentica al usuario y retorna sus datos junto con los workspaces a los que tiene acceso
     */
    public LoginResponse login(LoginRequest request) {
        // Buscar usuario por username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));

        // Validar password (sin encriptacion por ahora)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedException("Credenciales invalidas");
        }

        // Obtener los workspaces del usuario con sus roles
        List<UserWorkspace> userWorkspaces = userWorkspaceRepository.findByUserId(user.getId());

        List<WorkspaceResponse> workspaces = userWorkspaces.stream()
                .map(uw -> WorkspaceResponse.fromEntityWithRole(
                        uw.getWorkspace(),
                        uw.getRole().getName()
                ))
                .collect(Collectors.toList());

        return new LoginResponse(UserResponse.fromEntity(user), workspaces);
    }

    /**
     * Genera un token JWT para el usuario en el workspace seleccionado
     */
    public TokenResponse generateToken(TokenRequest request) {
        // Verificar que el usuario tenga acceso al workspace
        UserWorkspace userWorkspace = userWorkspaceRepository
                .findByUserIdAndWorkspaceId(request.getUserId(), request.getWorkspaceId())
                .orElseThrow(() -> new NotFoundException("El usuario no tiene acceso a este workspace"));

        Workspace workspace = userWorkspace.getWorkspace();
        Role role = userWorkspace.getRole();

        // Generar el token JWT
        String token = jwtUtil.generateToken(
                request.getUserId(),
                request.getWorkspaceId(),
                role.getId(),
                role.getCanCreate(),
                role.getCanEdit(),
                role.getCanDelete()
        );

        return new TokenResponse(
                token,
                WorkspaceResponse.fromEntity(workspace),
                RoleResponse.fromEntity(role)
        );
    }
}
