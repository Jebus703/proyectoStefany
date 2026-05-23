# Sistema SaaS Multi-Tenant - Gestion de Proyectos

Sistema MVP de gestion de proyectos multi-tenant donde los usuarios pueden pertenecer a multiples espacios de trabajo (Workspaces) con diferentes niveles de acceso segun el rol asignado en cada uno.

## Tecnologias

| Capa | Tecnologia |
|------|------------|
| Backend | Java 17 + Spring Boot 3 |
| Frontend | Angular 17 |
| Base de Datos | H2 (en memoria) |
| Contenedores | Docker + Docker Compose |

## Requisitos Previos

- Docker Desktop instalado y en ejecucion
- Puerto 8080 disponible (Backend)
- Puerto 4200 disponible (Frontend)

## Requerimientos del negocio
1. Dentro de un Workspace, se pueden crear Proyectos
   <img width="2997" height="860" alt="image" src="https://github.com/user-attachments/assets/98360a3d-a9ce-41b8-aa77-d8472034d3fe" />

