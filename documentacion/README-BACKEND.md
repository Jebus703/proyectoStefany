# Documentacion del Backend

API REST para el Sistema SaaS Multi-Tenant de Gestion de Proyectos.

---

## Informacion General

| Atributo | Valor |
|----------|-------|
| Base URL | `http://localhost:8080` |
| Formato | JSON |
| Autenticacion | JWT Bearer Token |

---

## Endpoints

### 1. POST /api/auth/login

Autentica al usuario y retorna sus datos junto con los workspaces a los que tiene acceso.

**Request:**
```json
{
  "username": "jperez",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "user": {
      "id": 1,
      "username": "jperez",
      "email": "juan.perez@email.com",
      "fullName": "Juan Perez"
    },
    "workspaces": [
      {
        "id": 1,
        "name": "Workspace Alfa",
        "description": "Espacio de trabajo para proyectos de desarrollo",
        "role": "ADMIN"
      },
      {
        "id": 2,
        "name": "Workspace Beta",
        "description": "Espacio de trabajo para proyectos de QA",
        "role": "LECTOR"
      },
      {
        "id": 3,
        "name": "Workspace Gamma",
        "description": "Espacio de trabajo para proyectos de innovacion",
        "role": "EDITOR"
      }
    ]
  }
}
```

**Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Credenciales invalidas",
  "data": null
}
```

---

### 2. POST /api/auth/token

Genera el token JWT para el workspace seleccionado por el usuario.

**Request:**
```json
{
  "userId": 1,
  "workspaceId": 1
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Token generado exitosamente",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsIndvcmtzcGFjZUlkIjoxLCJyb2xlSWQiOjEsImNhbkNyZWF0ZSI6dHJ1ZSwiY2FuRWRpdCI6dHJ1ZSwiY2FuRGVsZXRlIjp0cnVlLCJzdWIiOiIxIiwiaWF0IjoxNzE2MzgwMDAwLCJleHAiOjE3MTY0NjY0MDB9.xxxxx",
    "workspace": {
      "id": 1,
      "name": "Workspace Alfa",
      "description": "Espacio de trabajo para proyectos de desarrollo",
      "role": null
    },
    "role": {
      "id": 1,
      "name": "ADMIN",
      "description": "Administrador - Acceso total",
      "canCreate": true,
      "canEdit": true,
      "canDelete": true
    }
  }
}
```

**Response (404 Not Found):**
```json
{
  "success": false,
  "message": "El usuario no tiene acceso a este workspace",
  "data": null
}
```

---

### 3. GET /api/projects

Obtiene los proyectos del workspace activo (extraido del token).

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Operacion exitosa",
  "data": [
    {
      "id": 1,
      "name": "Sistema de Inventario",
      "description": "Desarrollo del modulo de inventario",
      "createdBy": "Juan Perez",
      "createdAt": "2026-05-22T10:30:00",
      "updatedAt": "2026-05-22T10:30:00"
    },
    {
      "id": 2,
      "name": "App Movil Ventas",
      "description": "Aplicacion movil para equipo de ventas",
      "createdBy": "Maria Garcia",
      "createdAt": "2026-05-22T11:00:00",
      "updatedAt": "2026-05-22T11:00:00"
    }
  ]
}
```

**Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Token invalido o expirado",
  "data": null
}
```

---

### 4. POST /api/projects

Crea un nuevo proyecto en el workspace activo.

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request:**
```json
{
  "name": "Nuevo Proyecto",
  "description": "Descripcion del nuevo proyecto"
}
```

**Response (201 Created) - Si es ADMIN o EDITOR:**
```json
{
  "success": true,
  "message": "Proyecto creado exitosamente",
  "data": {
    "id": 7,
    "name": "Nuevo Proyecto",
    "description": "Descripcion del nuevo proyecto",
    "createdBy": "Juan Perez",
    "createdAt": "2026-05-22T15:30:00",
    "updatedAt": "2026-05-22T15:30:00"
  }
}
```

**Response (403 Forbidden) - Si es LECTOR:**
```json
{
  "success": false,
  "message": "No tienes permisos para crear proyectos en este workspace",
  "data": null
}
```

---

### 5. GET /api/workspaces

Obtiene todos los workspaces del sistema (endpoint extra).

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Operacion exitosa",
  "data": [
    {
      "id": 1,
      "name": "Workspace Alfa",
      "description": "Espacio de trabajo para proyectos de desarrollo",
      "role": null
    },
    {
      "id": 2,
      "name": "Workspace Beta",
      "description": "Espacio de trabajo para proyectos de QA",
      "role": null
    },
    {
      "id": 3,
      "name": "Workspace Gamma",
      "description": "Espacio de trabajo para proyectos de innovacion",
      "role": null
    },
    {
      "id": 4,
      "name": "Workspace Delta",
      "description": "Workspace privado - Sin usuarios asignados",
      "role": null
    }
  ]
}
```

---

## Codigos de Estado HTTP

| Codigo | Descripcion |
|--------|-------------|
| 200 | OK - Operacion exitosa |
| 201 | Created - Recurso creado |
| 400 | Bad Request - Error de validacion |
| 401 | Unauthorized - No autenticado o token invalido |
| 403 | Forbidden - Sin permisos |
| 404 | Not Found - Recurso no encontrado |
| 500 | Internal Server Error - Error del servidor |

---

## Estructura del Token JWT

El token contiene la siguiente informacion:

```json
{
  "userId": 1,
  "workspaceId": 1,
  "roleId": 1,
  "canCreate": true,
  "canEdit": true,
  "canDelete": true,
  "sub": "1",
  "iat": 1716380000,
  "exp": 1716466400
}
```

| Campo | Descripcion |
|-------|-------------|
| userId | ID del usuario autenticado |
| workspaceId | ID del workspace activo |
| roleId | ID del rol en ese workspace |
| canCreate | Permiso para crear proyectos |
| canEdit | Permiso para editar proyectos |
| canDelete | Permiso para eliminar proyectos |
| sub | Subject (ID del usuario) |
| iat | Issued At (fecha de creacion) |
| exp | Expiration (fecha de expiracion - 24h) |

---

## Configuracion de Postman

### Importar Coleccion

1. Abrir Postman
2. Click en **Import**
3. Seleccionar el archivo `postman_collection.json` de esta carpeta
4. La coleccion aparecera en el panel izquierdo

### Configurar Variable de Entorno para el Token

Para no tener que copiar/pegar el token manualmente en cada request:

**Paso 1: Crear un Environment**

1. Click en el icono de engranaje (Environments) en la esquina superior derecha
2. Click en **Add**
3. Nombre: `SaaS Workspaces`
4. Agregar variable:
   - Variable: `token`
   - Initial Value: (dejar vacio)
   - Current Value: (dejar vacio)
5. Click en **Save**
6. Seleccionar el environment en el dropdown superior derecho

**Paso 2: Configurar Auto-guardado del Token**

En el request de `/api/auth/token`, ir a la pestana **Tests** y agregar:

```javascript
// Guardar el token automaticamente en la variable de entorno
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    if (jsonData.data && jsonData.data.token) {
        pm.environment.set("token", jsonData.data.token);
        console.log("Token guardado exitosamente");
    }
}
```

**Paso 3: Usar la Variable en otros Requests**

En los requests que requieren autenticacion (GET /api/projects, POST /api/projects):

1. Ir a la pestana **Authorization**
2. Type: **Bearer Token**
3. Token: `{{token}}`

O en el header manualmente:
```
Authorization: Bearer {{token}}
```

### Flujo de Prueba Recomendado

```
1. POST /api/auth/login      → Obtener datos del usuario y workspaces
2. POST /api/auth/token      → Seleccionar workspace y obtener token (se guarda automatico)
3. GET /api/projects         → Ver proyectos del workspace (usa {{token}})
4. POST /api/projects        → Crear proyecto (usa {{token}})
```

---

## Estructura del Proyecto

```
backend/src/main/java/com/saas/workspaces/
├── config/
│   └── CorsConfig.java
├── controller/
│   ├── AuthController.java
│   ├── ProjectController.java
│   └── WorkspaceController.java
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java
│   │   ├── TokenRequest.java
│   │   └── ProjectRequest.java
│   └── response/
│       ├── UserResponse.java
│       ├── RoleResponse.java
│       ├── WorkspaceResponse.java
│       ├── LoginResponse.java
│       ├── TokenResponse.java
│       └── ProjectResponse.java
├── entity/
│   ├── User.java
│   ├── Role.java
│   ├── Workspace.java
│   ├── UserWorkspace.java
│   └── Project.java
├── exception/
│   ├── UnauthorizedException.java
│   ├── ForbiddenException.java
│   ├── NotFoundException.java
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   ├── WorkspaceRepository.java
│   ├── UserWorkspaceRepository.java
│   └── ProjectRepository.java
├── service/
│   ├── AuthService.java
│   ├── ProjectService.java
│   └── WorkspaceService.java
├── util/
│   ├── ApiResponse.java
│   └── JwtUtil.java
└── WorkspacesApplication.java
```
