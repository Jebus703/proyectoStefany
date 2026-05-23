package com.saas.workspaces.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Clave secreta para firmar el token (en produccion deberia estar en application.properties)
    private static final String SECRET_KEY = "miClaveSecretaSuperSeguraParaJWTDeAlMenos256Bits123456789";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * Genera un token JWT con la informacion del usuario, workspace y rol
     */
    public String generateToken(Long userId, Long workspaceId, Long roleId,
                                 Boolean canCreate, Boolean canEdit, Boolean canDelete) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("workspaceId", workspaceId);
        claims.put("roleId", roleId);
        claims.put("canCreate", canCreate);
        claims.put("canEdit", canEdit);
        claims.put("canDelete", canDelete);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida si el token es valido y no ha expirado
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae todos los claims del token
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae el userId del token
     */
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Extrae el workspaceId del token
     */
    public Long getWorkspaceId(String token) {
        Claims claims = getClaims(token);
        return claims.get("workspaceId", Long.class);
    }

    /**
     * Extrae el roleId del token
     */
    public Long getRoleId(String token) {
        Claims claims = getClaims(token);
        return claims.get("roleId", Long.class);
    }

    /**
     * Verifica si el usuario puede crear
     */
    public Boolean canCreate(String token) {
        Claims claims = getClaims(token);
        return claims.get("canCreate", Boolean.class);
    }

    /**
     * Verifica si el usuario puede editar
     */
    public Boolean canEdit(String token) {
        Claims claims = getClaims(token);
        return claims.get("canEdit", Boolean.class);
    }

    /**
     * Verifica si el usuario puede eliminar
     */
    public Boolean canDelete(String token) {
        Claims claims = getClaims(token);
        return claims.get("canDelete", Boolean.class);
    }

    /**
     * Extrae el token del header Authorization
     * Formato esperado: "Bearer <token>"
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
