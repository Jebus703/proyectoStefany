-- =============================================
-- SCHEMA.SQL - Estructura de tablas para H2
-- Sistema SaaS Multi-Tenant de Gestion de Proyectos
-- =============================================

-- Tabla de Roles (con permisos explicitos)
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(100),
    can_create BOOLEAN NOT NULL DEFAULT FALSE,
    can_edit BOOLEAN NOT NULL DEFAULT FALSE,
    can_delete BOOLEAN NOT NULL DEFAULT FALSE
);

-- Tabla de Usuarios
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Workspaces (Espacios de trabajo)
CREATE TABLE workspaces (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de relacion Usuario-Workspace con Rol
CREATE TABLE user_workspace (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    workspace_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_uw_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_uw_workspace FOREIGN KEY (workspace_id) REFERENCES workspaces(id),
    CONSTRAINT fk_uw_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT uk_user_workspace UNIQUE (user_id, workspace_id)
);

-- Tabla de Proyectos
CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    workspace_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_workspace FOREIGN KEY (workspace_id) REFERENCES workspaces(id),
    CONSTRAINT fk_project_user FOREIGN KEY (created_by) REFERENCES users(id)
);
