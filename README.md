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

## Base de Datos

## Acceso a la Consola H2

Para ver la base de datos en memoria:

1. Ejecutar el backend
2. Ir a: `http://localhost:8080/h2-console`
3. Configurar:
   - JDBC URL: `jdbc:h2:mem:workspacesdb`
   - User: `sa`
   - Password: (vacio)

---

## Requisitos Previos

- Docker Desktop instalado y en ejecucion
- Puerto 8080 disponible (Backend)
- Puerto 4200 disponible (Frontend)

## Ejecucion del Proyecto

Clonar el repositorio

