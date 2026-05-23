# Sistema SaaS Multi-Tenant - Gestion de Proyectos

Sistema MVP de gestion de proyectos multi-tenant donde los usuarios pueden pertenecer a multiples espacios de trabajo (Workspaces) con diferentes niveles de acceso segun el rol asignado en cada uno.

## Tecnologias

| Capa | Tecnologia |
|------|------------|
| Backend | Java 17 + Spring Boot 3 |
| Frontend | Angular 17 |
| Base de Datos | H2 (en memoria) |
| Contenedores | Docker + Docker Compose |

---

## Despliegue con Docker

### Requisitos Previos

- Docker Desktop instalado y en ejecucion
- Puerto 8080 disponible (Backend)
- Puerto 4200 disponible (Frontend)

### Ejecutar con Docker Compose

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd proyectoStefany

# Construir y ejecutar
docker compose up --build

# Para ejecutar en segundo plano
docker compose up --build -d

# Para detener
docker compose down
```

### Ejecutar solo el Backend con Docker

```bash
# Desde la raiz del proyecto
cd backend

# Construir imagen
docker build -t saas-backend .

# Ejecutar contenedor
docker run -p 8080:8080 saas-backend
```

---

## Endpoints de la API

Base URL: `http://localhost:8080`

### Autenticacion

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | `/api/auth/login` | Autentica usuario y retorna workspaces disponibles |
| POST | `/api/auth/token` | Genera token JWT para el workspace seleccionado |

### Proyectos

| Metodo | Endpoint | Descripcion | Auth |
|--------|----------|-------------|------|
| GET | `/api/projects` | Lista proyectos del workspace activo | Bearer Token |
| POST | `/api/projects` | Crea proyecto (requiere rol ADMIN o EDITOR) | Bearer Token |

### Workspaces

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/api/workspaces` | Lista todos los workspaces del sistema |

---

## Ejemplos de Uso

### 1. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "jperez", "password": "password123"}'
```

### 2. Generar Token

```bash
curl -X POST http://localhost:8080/api/auth/token \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "workspaceId": 1}'
```

### 3. Listar Proyectos

```bash
curl -X GET http://localhost:8080/api/projects \
  -H "Authorization: Bearer <token>"
```

### 4. Crear Proyecto

```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name": "Nuevo Proyecto", "description": "Descripcion del proyecto"}'
```

---

## Usuarios de Prueba

| Usuario | Password | Email |
|---------|----------|-------|
| jperez | password123 | juan.perez@email.com |
| mgarcia | password123 | maria.garcia@email.com |
| clopez | password123 | carlos.lopez@email.com |

### Matriz de Roles

| Usuario | Workspace Alfa | Workspace Beta | Workspace Gamma |
|---------|:--------------:|:--------------:|:---------------:|
| jperez  | ADMIN          | LECTOR         | EDITOR          |
| mgarcia | EDITOR         | ADMIN          | LECTOR          |
| clopez  | LECTOR         | EDITOR         | ADMIN           |

---

## Acceso a la Consola H2

Para ver la base de datos en memoria:

1. Ejecutar el backend
2. Ir a: `http://localhost:8080/h2-console`
3. Configurar:
   - JDBC URL: `jdbc:h2:mem:workspacesdb`
   - User: `sa`
   - Password: (vacio)

---

## Estructura del Proyecto

```
proyectoStefany/
├── backend/                    # API REST Spring Boot
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                   # Aplicacion Angular (pendiente)
├── documentacion/              # Documentacion tecnica
│   ├── README.md               # Esquema de BD
│   ├── README-BACKEND.md       # Documentacion de endpoints
│   └── postman_collection.json # Coleccion Postman
├── docker-compose.yml
└── README.md
```
