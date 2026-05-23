-- =============================================
-- DATA.SQL - Datos pre-cargados para pruebas
-- Sistema SaaS Multi-Tenant de Gestion de Proyectos
-- =============================================

-- =============================================
-- ROLES (con permisos explicitos en BD)
-- =============================================
INSERT INTO roles (id, name, description, can_create, can_edit, can_delete) VALUES
(1, 'ADMIN', 'Administrador - Acceso total', TRUE, TRUE, TRUE),
(2, 'EDITOR', 'Editor - Puede crear y editar', TRUE, TRUE, FALSE),
(3, 'LECTOR', 'Lector - Solo lectura', FALSE, FALSE, FALSE);

-- =============================================
-- USUARIOS (3 usuarios de prueba)
-- =============================================
INSERT INTO users (id, username, password, email, full_name) VALUES
(1, 'jperez', 'password123', 'juan.perez@email.com', 'Juan Perez'),
(2, 'mgarcia', 'password123', 'maria.garcia@email.com', 'Maria Garcia'),
(3, 'clopez', 'password123', 'carlos.lopez@email.com', 'Carlos Lopez');

-- =============================================
-- WORKSPACES (4 espacios de trabajo)
-- =============================================
INSERT INTO workspaces (id, name, description) VALUES
(1, 'Workspace Alfa', 'Espacio de trabajo para proyectos de desarrollo'),
(2, 'Workspace Beta', 'Espacio de trabajo para proyectos de QA'),
(3, 'Workspace Gamma', 'Espacio de trabajo para proyectos de innovacion'),
(4, 'Workspace Delta', 'Workspace privado - Sin usuarios asignados');

-- =============================================
-- ASIGNACION USUARIO-WORKSPACE-ROL
--
-- jperez  -> Alfa (ADMIN), Beta (LECTOR), Gamma (EDITOR)
-- mgarcia -> Alfa (EDITOR), Beta (ADMIN), Gamma (LECTOR)
-- clopez  -> Alfa (LECTOR), Beta (EDITOR), Gamma (ADMIN)
-- =============================================
INSERT INTO user_workspace (user_id, workspace_id, role_id) VALUES
(1, 1, 1),  -- jperez es ADMIN en Workspace Alfa
(1, 2, 3),  -- jperez es LECTOR en Workspace Beta
(1, 3, 2),  -- jperez es EDITOR en Workspace Gamma
(2, 1, 2),  -- mgarcia es EDITOR en Workspace Alfa
(2, 2, 1),  -- mgarcia es ADMIN en Workspace Beta
(2, 3, 3),  -- mgarcia es LECTOR en Workspace Gamma
(3, 1, 3),  -- clopez es LECTOR en Workspace Alfa
(3, 2, 2),  -- clopez es EDITOR en Workspace Beta
(3, 3, 1);  -- clopez es ADMIN en Workspace Gamma

-- =============================================
-- PROYECTOS DE EJEMPLO
-- =============================================
-- Proyectos en Workspace Alfa
INSERT INTO projects (id, name, description, workspace_id, created_by) VALUES
(1, 'Sistema de Inventario', 'Desarrollo del modulo de inventario', 1, 1),
(2, 'App Movil Ventas', 'Aplicacion movil para equipo de ventas', 1, 2);

-- Proyectos en Workspace Beta
INSERT INTO projects (id, name, description, workspace_id, created_by) VALUES
(3, 'Testing Automatizado', 'Suite de pruebas E2E', 2, 2),
(4, 'Documentacion API', 'Documentacion tecnica de APIs', 2, 3);

-- Proyectos en Workspace Gamma
INSERT INTO projects (id, name, description, workspace_id, created_by) VALUES
(5, 'Prototipo IA', 'Prototipo de asistente con inteligencia artificial', 3, 3),
(6, 'Dashboard Analytics', 'Panel de metricas y analytics en tiempo real', 3, 1);
