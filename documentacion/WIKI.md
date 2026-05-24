# Wiki - Sistema SaaS Multi-Tenant

Bienvenido a la documentacion del Sistema de Gestion de Proyectos Multi-Tenant.

---

## Tabla de Contenidos

1. [Descripcion General](#descripcion-general)
2. [Arquitectura](#arquitectura)
3. [Flujo de Autenticacion](#flujo-de-autenticacion)
4. [Modelo de Datos](#modelo-de-datos)
5. [Roles y Permisos](#roles-y-permisos)
6. [Guia de Instalacion](#guia-de-instalacion)
7. [Guia de Uso](#guia-de-uso)
8. [FAQ](#faq)

---

## Descripcion General

### Que es este sistema?

Es un MVP de un SaaS (Software as a Service) para gestion de proyectos que implementa el patron **Multi-Tenant**. Esto significa que multiples organizaciones (tenants) pueden usar la misma aplicacion de forma aislada.

### Caracteristicas principales

- **Multi-Workspace**: Un usuario puede pertenecer a multiples espacios de trabajo
- **Roles Contextuales**: El rol de un usuario cambia segun el workspace donde se encuentre
- **Permisos Dinamicos**: Los permisos estan almacenados en base de datos, no hardcodeados
- **JWT Authentication**: Autenticacion stateless con tokens JWT

### Tecnologias utilizadas

| Componente | Tecnologia |
|------------|------------|
| Backend | Java 17, Spring Boot 3, Spring Data JPA |
| Base de Datos | H2 (en memoria) |
| Autenticacion | JWT (JSON Web Tokens) |
| Contenedores | Docker, Docker Compose |
| Frontend | Angular 17 (pendiente) |

---

## Arquitectura

### Estructura del Proyecto

```
proyectoStefany/
├── backend/
│   ├── src/main/java/com/saas/workspaces/
│   │   ├── config/          # Configuraciones (CORS)
│   │   ├── controller/      # Endpoints REST
│   │   ├── dto/             # Objetos de transferencia
│   │   │   ├── request/     # DTOs de entrada
│   │   │   └── response/    # DTOs de salida
│   │   ├── entity/          # Entidades JPA
│   │   ├── exception/       # Manejo de errores
│   │   ├── repository/      # Acceso a datos
│   │   ├── service/         # Logica de negocio
│   │   └── util/            # Utilidades (JWT, ApiResponse)
│   └── src/main/resources/
│       ├── application.properties
│       ├── schema.sql       # Estructura de BD
│       └── data.sql         # Datos iniciales
├── frontend/                # Angular (pendiente)
├── documentacion/           # Documentacion tecnica
└── docker-compose.yml
```

### Capas de la Aplicacion

```
┌─────────────────────────────────────────────────────────┐
│                      FRONTEND                            │
│                     (Angular)                            │
└─────────────────────────┬───────────────────────────────┘
                          │ HTTP/REST
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    CONTROLLERS                           │
│         AuthController, ProjectController                │
└─────────────────────────┬───────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                     SERVICES                             │
│          AuthService, ProjectService                     │
│                  (Logica de negocio)                     │
└─────────────────────────┬───────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                   REPOSITORIES                           │
│     UserRepository, ProjectRepository, etc.              │
│                  (Acceso a datos)                        │
└─────────────────────────┬───────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    BASE DE DATOS                         │
│                        H2                                │
└─────────────────────────────────────────────────────────┘
```

---

## Flujo de Autenticacion

### Diagrama de Secuencia

```
Usuario          Frontend              Backend                BD
   │                 │                    │                   │
   │  1. Ingresa     │                    │                   │
   │  credenciales   │                    │                   │
   │────────────────>│                    │                   │
   │                 │                    │                   │
   │                 │  2. POST /login    │                   │
   │                 │───────────────────>│                   │
   │                 │                    │  3. Buscar user   │
   │                 │                    │──────────────────>│
   │                 │                    │<──────────────────│
   │                 │                    │                   │
   │                 │  4. User + Workspaces                  │
   │                 │<───────────────────│                   │
   │                 │                    │                   │
   │  5. Muestra     │                    │                   │
   │  workspaces     │                    │                   │
   │<────────────────│                    │                   │
   │                 │                    │                   │
   │  6. Selecciona  │                    │                   │
   │  workspace      │                    │                   │
   │────────────────>│                    │                   │
   │                 │                    │                   │
   │                 │  7. POST /token    │                   │
   │                 │  {userId, wsId}    │                   │
   │                 │───────────────────>│                   │
   │                 │                    │  8. Buscar rol    │
   │                 │                    │──────────────────>│
   │                 │                    │<──────────────────│
   │                 │                    │                   │
   │                 │                    │  9. Generar JWT   │
   │                 │                    │                   │
   │                 │  10. Token JWT     │                   │
   │                 │<───────────────────│                   │
   │                 │                    │                   │
   │  11. Dashboard  │                    │                   │
   │  de proyectos   │                    │                   │
   │<────────────────│                    │                   │
   │                 │                    │                   │
   │                 │  12. GET /projects │                   │
   │                 │  + Bearer Token    │                   │
   │                 │───────────────────>│                   │
   │                 │                    │  13. Validar JWT  │
   │                 │                    │  14. Buscar       │
   │                 │                    │      proyectos    │
   │                 │                    │──────────────────>│
   │                 │                    │<──────────────────│
   │                 │  15. Proyectos     │                   │
   │                 │<───────────────────│                   │
   │                 │                    │                   │
```

### Contenido del Token JWT

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

---

## Modelo de Datos

### Diagrama Entidad-Relacion

```
┌──────────────────┐
│      ROLES       │
├──────────────────┤
│ id        (PK)   │
│ name             │
│ description      │
│ can_create       │
│ can_edit         │
│ can_delete       │
└────────┬─────────┘
         │
         │ 1:N
         ▼
┌──────────────────┐        ┌──────────────────┐
│  USER_WORKSPACE  │        │    WORKSPACES    │
├──────────────────┤        ├──────────────────┤
│ id        (PK)   │        │ id        (PK)   │
│ user_id   (FK)───┼────┐   │ name             │
│ workspace_id(FK)─┼────┼──>│ description      │
│ role_id   (FK)   │    │   └──────────────────┘
└──────────────────┘    │
                        │
                        │   ┌──────────────────┐
                        │   │      USERS       │
                        │   ├──────────────────┤
                        └──>│ id        (PK)   │
                            │ username         │
                            │ password         │
                            │ email            │
                            │ full_name        │
                            └──────────────────┘

┌──────────────────┐
│     PROJECTS     │
├──────────────────┤
│ id        (PK)   │
│ name             │
│ description      │
│ workspace_id(FK)─┼──> WORKSPACES
│ created_by (FK)──┼──> USERS
│ created_at       │
│ updated_at       │
└──────────────────┘
```

### Descripcion de Tablas

| Tabla | Descripcion |
|-------|-------------|
| `roles` | Define los roles con sus permisos (ADMIN, EDITOR, LECTOR) |
| `users` | Usuarios del sistema |
| `workspaces` | Espacios de trabajo (tenants) |
| `user_workspace` | Relacion N:M entre usuarios y workspaces con el rol asignado |
| `projects` | Proyectos que pertenecen a un workspace |

---

## Roles y Permisos

### Matriz de Permisos

| Permiso | ADMIN | EDITOR | LECTOR |
|---------|:-----:|:------:|:------:|
| Ver proyectos | Si | Si | Si |
| Crear proyectos | Si | Si | No |
| Editar proyectos | Si | Si | No |
| Eliminar proyectos | Si | No | No |

### Concepto Clave: Roles Contextuales

Un usuario **NO tiene un rol global**. Su rol depende del workspace donde esta trabajando.

**Ejemplo:**
```
Usuario: Juan Perez

Workspace Alfa  → Rol: ADMIN   → Puede crear, editar, eliminar
Workspace Beta  → Rol: LECTOR  → Solo puede ver
Workspace Gamma → Rol: EDITOR  → Puede crear y editar
```

---

## Guia de Instalacion

### Requisitos

- Docker Desktop
- Git
- (Opcional) Java 17 y Maven para desarrollo local

### Instalacion con Docker

```bash
# 1. Clonar repositorio
git clone https://github.com/tu-usuario/proyectoStefany.git
cd proyectoStefany

# 2. Ejecutar con Docker Compose
docker compose up --build

# 3. Verificar que esta corriendo
curl http://localhost:8080/api/workspaces
```

### Instalacion Local (Desarrollo)

```bash
# 1. Clonar repositorio
git clone https://github.com/tu-usuario/proyectoStefany.git
cd proyectoStefany/backend

# 2. Ejecutar con Maven
./mvnw spring-boot:run

# 3. Acceder a la consola H2
# http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:workspacesdb
# User: sa
# Password: (vacio)
```

---

## Guia de Uso

### Usuarios de Prueba

| Usuario | Password | Rol en Alfa | Rol en Beta | Rol en Gamma |
|---------|----------|:-----------:|:-----------:|:------------:|
| jperez | password123 | ADMIN | LECTOR | EDITOR |
| mgarcia | password123 | EDITOR | ADMIN | LECTOR |
| clopez | password123 | LECTOR | EDITOR | ADMIN |

### Probar con Postman

1. Importar la coleccion desde `documentacion/postman_collection.json`
2. Ejecutar "1. Login" con las credenciales de prueba
3. Ejecutar "2. Generate Token" para obtener el JWT
4. El token se guarda automaticamente en la variable `{{token}}`
5. Ejecutar "3. Get Projects" o "4. Create Project"

### Probar con cURL

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "jperez", "password": "password123"}'

# Generar Token
curl -X POST http://localhost:8080/api/auth/token \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "workspaceId": 1}'

# Listar Proyectos (reemplazar <TOKEN>)
curl -X GET http://localhost:8080/api/projects \
  -H "Authorization: Bearer <TOKEN>"

# Crear Proyecto
curl -X POST http://localhost:8080/api/projects \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name": "Mi Proyecto", "description": "Descripcion"}'
```

---

## FAQ

### Por que H2 y no PostgreSQL/MySQL?

Para simplicidad del MVP y facilidad de despliegue. H2 es una base de datos embebida que no requiere instalacion. En produccion se cambiaria a PostgreSQL.

### Por que los permisos estan en la base de datos?

Para evitar codigo hardcodeado. Si mañana se necesita cambiar que LECTOR pueda crear proyectos, solo se actualiza un registro en la BD, sin redesplegar la aplicacion.

### Como se manejan multiples workspaces?

Cada token JWT incluye el `workspaceId`. Todas las operaciones (listar proyectos, crear proyectos) usan ese workspace del token.

### Que pasa si el token expira?

El frontend debe detectar el error 401 y redirigir al login. El token actual expira en 24 horas.

### Como añadir un nuevo rol?

1. Insertar en la tabla `roles` con los permisos deseados
2. El sistema automaticamente lo reconocera

```sql
INSERT INTO roles (name, description, can_create, can_edit, can_delete)
VALUES ('SUPERVISOR', 'Puede ver y editar', FALSE, TRUE, FALSE);
```

---

## Contacto

Para dudas o sugerencias, crear un Issue en el repositorio.
