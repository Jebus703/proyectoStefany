# Documentacion del Esquema de Base de Datos

Sistema SaaS Multi-Tenant de Gestion de Proyectos

---

## Diagrama de Entidades

```
┌──────────────────┐
│      ROLES       │
├──────────────────┤
│ id        (PK)   │
│ name             │
│ description      │
│ can_create       │◄── Permisos dinamicos
│ can_edit         │    almacenados en BD
│ can_delete       │    (no hardcodeados)
└────────┬─────────┘
         │
         │ 1:N
         ▼
┌──────────────────┐        ┌──────────────────┐
│  USER_WORKSPACE  │        │    WORKSPACES    │
├──────────────────┤        ├──────────────────┤
│ id        (PK)   │        │ id        (PK)   │
│ user_id   (FK)───┼────┐   │ name             │
│ workspace_id(FK)─┼────┼──▶│ description      │
│ role_id   (FK)   │    │   │ created_at       │
│ created_at       │    │   └──────────────────┘
└──────────────────┘    │
                        │
                        │   ┌──────────────────┐
                        │   │      USERS       │
                        │   ├──────────────────┤
                        └──▶│ id        (PK)   │
                            │ username         │
                            │ password         │
                            │ email            │
                            │ full_name        │
                            │ created_at       │
                            └──────────────────┘

┌──────────────────┐
│     PROJECTS     │
├──────────────────┤
│ id        (PK)   │
│ name             │
│ description      │
│ workspace_id(FK)─┼──▶ Pertenece a un WORKSPACE
│ created_by (FK)──┼──▶ Creado por un USER
│ created_at       │
│ updated_at       │
└──────────────────┘
```

---

## Descripcion de Tablas

### ROLES
Almacena los roles del sistema con sus permisos.

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | BIGINT | Identificador unico (PK) |
| name | VARCHAR(20) | Nombre del rol (ADMIN, EDITOR, LECTOR) |
| description | VARCHAR(100) | Descripcion del rol |
| can_create | BOOLEAN | Permiso para crear proyectos |
| can_edit | BOOLEAN | Permiso para editar proyectos |
| can_delete | BOOLEAN | Permiso para eliminar proyectos |

### USERS
Almacena los usuarios del sistema.

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | BIGINT | Identificador unico (PK) |
| username | VARCHAR(50) | Nombre de usuario (unico) |
| password | VARCHAR(100) | Contrasena |
| email | VARCHAR(100) | Correo electronico (unico) |
| full_name | VARCHAR(100) | Nombre completo |
| created_at | TIMESTAMP | Fecha de creacion |

### WORKSPACES
Almacena los espacios de trabajo (tenants).

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | BIGINT | Identificador unico (PK) |
| name | VARCHAR(100) | Nombre del workspace (unico) |
| description | VARCHAR(255) | Descripcion del workspace |
| created_at | TIMESTAMP | Fecha de creacion |

### USER_WORKSPACE
Tabla pivote que relaciona usuarios con workspaces y define su rol.

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | BIGINT | Identificador unico (PK) |
| user_id | BIGINT | FK a tabla USERS |
| workspace_id | BIGINT | FK a tabla WORKSPACES |
| role_id | BIGINT | FK a tabla ROLES |
| created_at | TIMESTAMP | Fecha de asignacion |

**Restriccion:** Un usuario solo puede tener UN rol por workspace (UNIQUE user_id + workspace_id).

### PROJECTS
Almacena los proyectos de cada workspace.

| Campo | Tipo | Descripcion |
|-------|------|-------------|
| id | BIGINT | Identificador unico (PK) |
| name | VARCHAR(100) | Nombre del proyecto |
| description | VARCHAR(500) | Descripcion del proyecto |
| workspace_id | BIGINT | FK al workspace donde pertenece |
| created_by | BIGINT | FK al usuario que lo creo |
| created_at | TIMESTAMP | Fecha de creacion |
| updated_at | TIMESTAMP | Fecha de ultima modificacion |

---

## Roles y Permisos

Los permisos estan almacenados en la base de datos, lo que permite modificarlos sin cambiar codigo.

| Rol | can_create | can_edit | can_delete | Descripcion |
|-----|:----------:|:--------:|:----------:|-------------|
| **ADMIN** | TRUE | TRUE | TRUE | Control total sobre proyectos |
| **EDITOR** | TRUE | TRUE | FALSE | Puede crear y editar, no eliminar |
| **LECTOR** | FALSE | FALSE | FALSE | Solo puede visualizar proyectos |

### Ventaja de permisos en BD

```
Permisos en CODIGO (mal):          Permisos en BD (bien):
─────────────────────────          ─────────────────────────
if (rol == "ADMIN") {              Role role = roleRepo.findById(id);
  canCreate = true;                return role.getCanCreate();
  canEdit = true;        VS
  canDelete = true;                // Si cambias permisos:
}                                  // UPDATE en BD y listo
// Si cambias permisos:            // No hay que redesplegar
// Cambias codigo y redespliegas
```

---

## Conceptos Clave

### 1. Multi-Tenant con Workspaces

El sistema permite que multiples organizaciones (tenants) coexistan en la misma aplicacion. Cada workspace es un tenant aislado con sus propios proyectos.

```
┌─────────────────────────────────────────────────────┐
│                    APLICACION                        │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐   │
│  │ Workspace   │ │ Workspace   │ │ Workspace   │   │
│  │    Alfa     │ │    Beta     │ │   Gamma     │   │
│  │             │ │             │ │             │   │
│  │ [Proyectos] │ │ [Proyectos] │ │ [Proyectos] │   │
│  └─────────────┘ └─────────────┘ └─────────────┘   │
└─────────────────────────────────────────────────────┘
```

### 2. Roles Contextuales (por Workspace)

Un usuario NO tiene un rol global. Su rol DEPENDE del workspace donde esta trabajando.

```
Usuario: Juan Perez (jperez)

┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐
│   WORKSPACE ALFA    │  │   WORKSPACE BETA    │  │   WORKSPACE GAMMA   │
│                     │  │                     │  │                     │
│   Rol: ADMIN        │  │   Rol: LECTOR       │  │   Rol: EDITOR       │
│   ─────────────     │  │   ─────────────     │  │   ─────────────     │
│   + Crear proyecto  │  │   - Solo ver        │  │   + Crear proyecto  │
│   + Editar proyecto │  │   - No puede crear  │  │   + Editar proyecto │
│   + Eliminar        │  │   - No puede editar │  │   - No puede borrar │
└─────────────────────┘  └─────────────────────┘  └─────────────────────┘
```

### 3. Flujo de Autenticacion y Autorizacion

```
1. LOGIN
   Usuario ingresa credenciales
   └──▶ Sistema valida y retorna lista de workspaces disponibles

2. SELECCION DE WORKSPACE
   Usuario elige un workspace
   └──▶ Sistema genera token con contexto (user + workspace + rol)

3. OPERACIONES
   Usuario intenta crear/editar/eliminar proyecto
   └──▶ Sistema consulta permisos del rol en BD
       └──▶ Permite o deniega la operacion
```

### 4. Tabla Pivote USER_WORKSPACE

Esta tabla es el corazon del sistema multi-tenant. Responde a la pregunta:
**"Que rol tiene este usuario en este workspace?"**

```sql
-- Consulta: Obtener rol de jperez en Workspace Alfa
SELECT r.name, r.can_create, r.can_edit, r.can_delete
FROM user_workspace uw
JOIN roles r ON uw.role_id = r.id
WHERE uw.user_id = 1 AND uw.workspace_id = 1;

-- Resultado: ADMIN, true, true, true
```

---

## Datos Pre-cargados para Pruebas

### Usuarios

| Usuario | Password | Email |
|---------|----------|-------|
| jperez | password123 | juan.perez@email.com |
| mgarcia | password123 | maria.garcia@email.com |
| clopez | password123 | carlos.lopez@email.com |

### Workspaces

| ID | Nombre | Descripcion | Usuarios asignados |
|----|--------|-------------|-------------------|
| 1 | Workspace Alfa | Proyectos de desarrollo | jperez, mgarcia, clopez |
| 2 | Workspace Beta | Proyectos de QA | jperez, mgarcia, clopez |
| 3 | Workspace Gamma | Proyectos de innovacion | jperez, mgarcia, clopez |
| 4 | Workspace Delta | Workspace privado | Ninguno |

> **Nota:** Workspace Delta existe pero no tiene usuarios asignados. Util para demostrar endpoint de listar todos los workspaces.

### Matriz de Asignacion de Roles

| Usuario | Workspace Alfa | Workspace Beta | Workspace Gamma |
|---------|:--------------:|:--------------:|:---------------:|
| jperez | **ADMIN** | LECTOR | EDITOR |
| mgarcia | EDITOR | **ADMIN** | LECTOR |
| clopez | LECTOR | EDITOR | **ADMIN** |

> Cada usuario es ADMIN en un workspace diferente, lo que facilita probar todos los escenarios.

### Proyectos

| Proyecto | Workspace | Creado por |
|----------|-----------|------------|
| Sistema de Inventario | Alfa | jperez |
| App Movil Ventas | Alfa | mgarcia |
| Testing Automatizado | Beta | mgarcia |
| Documentacion API | Beta | clopez |
| Prototipo IA | Gamma | clopez |
| Dashboard Analytics | Gamma | jperez |
