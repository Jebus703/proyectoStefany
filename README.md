# Sistema SaaS Multi-Tenant - Gestion de Proyectos

Sistema MVP de gestion de proyectos multi-tenant donde los usuarios pueden pertenecer a multiples espacios de trabajo (Workspaces) con diferentes niveles de acceso segun el rol asignado en cada uno.

## Tecnologias

| Capa | Tecnologia |
|------|------------|
| Backend | Java 17 + Spring Boot 3 |
| Frontend | Angular 17 |
| Base de Datos | H2 (en memoria) |
| Contenedores | Docker + Docker Compose |

## Requisitos obligatorios de entrega y criterios de exclusión

1. Entrega vía repositorio público: El código debe ser entregado mediante un enlace a un repositorio
público accesible (GitHub, GitLab o Bitbucket). No se aceptarán archivos ZIP, adjuntos por correo o
enlaces a carpetas de Google Drive/OneDrive.

3. Ejecución 100% en Docker: La solución completa (Base de datos con su script de datos pre-cargados,
Back-end y Front-end) debe ser capaz de levantarse localmente utilizando Docker. Se debe incluir un
archivo docker-compose.yml (y sus respectivos Dockerfile) de manera que con solo ejecutar el comando
docker compose up --build en la raíz del proyecto, toda la aplicación quede funcional. Las instrucciones
exactas de ejecución deben estar documentadas en el README.md.


## Respuestas al ejercicio

## 1. Dentro de un Workspace, se pueden crear Proyectos
<img width="2997" height="860" alt="image" src="https://github.com/user-attachments/assets/4906deb1-7f52-4ffc-bd69-f794485113e1" />

## 2. Reglas de acceso por rol:
● Admin - Puede crear, editar, eliminar proyectos.

                                                        ● Front
<img width="3745" height="865" alt="image" src="https://github.com/user-attachments/assets/896127e0-05b1-47bf-ad96-2545f72ac416" />
- auth.service.ts - línea 77 Consulta del backend token se guarda los roles del usuario
<img width="2083" height="717" alt="image" src="https://github.com/user-attachments/assets/d40339d9-582f-4843-9f3f-b80d7590af0c" />
- auth.service.ts - línea 125-136  guarda el valor  para saber si activar o no los botones
<img width="1240" height="617" alt="image" src="https://github.com/user-attachments/assets/9ee6ac5e-0897-4119-853b-14fd2d5cbd1f" />
- dashboard.component.ts - línea 47  inyecto el service para que html tenga acceso y a los valores de los metodos
<img width="910" height="140" alt="image" src="https://github.com/user-attachments/assets/82cd530c-de58-4b31-9fb5-be1159a64d88" />
- dashboard.component.html - línea 30,54,62 activo o no dependiendo de los valores de los metodos
<img width="1725" height="197" alt="image" src="https://github.com/user-attachments/assets/fc061553-20e9-40c3-811c-d71a759dc2fe" />
<img width="870" height="272" alt="image" src="https://github.com/user-attachments/assets/ef693b91-45da-4a1c-bc59-a908b3fcd37b" />
<img width="805" height="290" alt="image" src="https://github.com/user-attachments/assets/52aa62a9-b8a5-4f16-9369-e51facd5fdd7" />

                                                                  ● Backend
- ProjectService.java - línea 45 Crear proyecto
<img width="2155" height="412" alt="image" src="https://github.com/user-attachments/assets/347b9056-feb4-443d-a299-5d0d5d16811c" />
- ProjectService.java - línea 74 Editar proyecto
<img width="2202" height="427" alt="image" src="https://github.com/user-attachments/assets/d912137b-eb36-4f62-b50c-b0113b881f1c" />
- ProjectService.java - línea 74 Eliminar proyecto
<img width="2170" height="435" alt="image" src="https://github.com/user-attachments/assets/518ea1ce-b590-4ae7-a429-c2a30c82f9f8" />
● Editor - Solo puede crear y editar proyectos.

                                                        ● Front
<img width="2910" height="1027" alt="image" src="https://github.com/user-attachments/assets/b6f08c85-cd08-4711-af56-f031d4fb149a" />
                                                        ● Backend
- ProjectService.java - línea 74 Editar proyecto
<img width="2202" height="427" alt="image" src="https://github.com/user-attachments/assets/d912137b-eb36-4f62-b50c-b0113b881f1c" />
- ProjectService.java - línea 74 Eliminar proyecto
<img width="2170" height="435" alt="image" src="https://github.com/user-attachments/assets/518ea1ce-b590-4ae7-a429-c2a30c82f9f8" />
● Lector - Solo puede ver los proyectos.

                                                        ● Front

<img width="2975" height="930" alt="image" src="https://github.com/user-attachments/assets/c4514715-45c6-4ca8-86ee-44d70d760da8" />

## Alcance y simplificaciones
1. NO es necesario desarrollar pantallas ni endpoints para registrar Usuarios ni crear nuevos Workspaces
   ok no se desarrollo, no hay captura de pantalla
2. Se requiere que incluyas un script de base de datos con datos pre-cargados para pruebas. El script debe
contener al menos:
Los script se incluyeron en los archivos .data estos archivos contienen los scripts de tanto el modelado de las base de datos como los datos iniciales
estos se encuentran en el  backend en:
[data](backend/src/main/resources/data.sql) ,
[estructura](backend/src/main/resources/schema.sql)
<img width="925" height="802" alt="image" src="https://github.com/user-attachments/assets/fa2611bd-745d-4fdb-ab98-37c5e3908e6b" />

● 1 Usuario de prueba
<img width="1362" height="347" alt="image" src="https://github.com/user-attachments/assets/86cd5980-81b0-4e0e-ac93-71b57aafd469" />
● 2 Workspaces distintos: Workspace Alfa y Workspace Beta.
<img width="1420" height="265" alt="image" src="https://github.com/user-attachments/assets/eef24788-c328-4184-bf5f-be685eb04030" />
● El usuario debe estar asignado a Workspace Alfa como Admin y a Workspace Beta como Lector.
<img width="1270" height="140" alt="image" src="https://github.com/user-attachments/assets/96ed221d-a2ba-4727-97e8-76a6eb968dc3" />
● Un par de Proyectos de ejemplo creados en cada Workspace.
<img width="1580" height="600" alt="image" src="https://github.com/user-attachments/assets/2be25067-496a-4a7e-aee2-dd66ab7673bd" />
## Requerimientos del Back-end (Java, Python o C
                                                        JAVA

Deberás exponer los siguientes endpoints estructurados para manejar el contexto del Workspace:
1. POST /api/auth/login: Autentica al Usuario (credenciales fijas del usuario pre-cargado). Debe retornar los
datos del Usuario y el listado de Workspaces a los que tiene acceso
La exposicion del endpoint se iso en un controller aparte , para solo el Login ,en la logica se utilizo tambien un dto,service,repositori
[Controller Login](backend/src/main/java/com/saas/workspaces/controller/AuthController.java)
<img width="1845" height="260" alt="image" src="https://github.com/user-attachments/assets/f4a07794-8554-4a78-9069-114172b8998d" />
● POSTMAN
<img width="2195" height="1952" alt="image" src="https://github.com/user-attachments/assets/250b3131-be58-4b6b-a51d-af5ec3300b21" />

2. POST /api/auth/token: (Intercambio de contexto). Recibe el Workspace seleccionado por el usuario y
genera el token de acceso final.
[Controller Token](backend/src/main/java/com/saas/workspaces/controller/AuthController.java)
<img width="1892" height="302" alt="image" src="https://github.com/user-attachments/assets/8f99cfa3-f589-41fc-99ec-ffae9a9afaf3" />
● POSTMAN
<img width="2265" height="1345" alt="image" src="https://github.com/user-attachments/assets/7d7766d2-ae89-481f-82cd-44a6521e2dd3" />

3. GET /api/projects: Retorna los Proyectos del Workspace activo. Requiere autenticación y autorización.
[Controller Token](backend/src/main/java/com/saas/workspaces/controller/ProjectController.java)
<img width="1667" height="362" alt="image" src="https://github.com/user-attachments/assets/c658da52-3261-4ad4-ab11-2300751e89a8" />
● POSTMAN
<img width="2182" height="852" alt="image" src="https://github.com/user-attachments/assets/a10591ab-228b-4ec4-a62e-697db9bba539" />

4. POST /api/projects: Crea un Proyecto en el Workspace activo. Requiere autenticación y autorización.
Debe validar que el rol sea Admin o Editor. Si es Lector, debe retornar un error.
[Controller Token](backend/src/main/java/com/saas/workspaces/controller/ProjectController.java)
<img width="1632" height="502" alt="image" src="https://github.com/user-attachments/assets/c3a9d1a7-4e31-4166-b950-07a46e57980e" />
<img width="1877" height="975" alt="image" src="https://github.com/user-attachments/assets/9b3b5e02-acc2-4db7-991d-f7d3310aeb71" />
● POSTMAN
<img width="2212" height="1612" alt="image" src="https://github.com/user-attachments/assets/cfe079ec-d091-468f-8497-037a6f6c9729" />
<img width="2235" height="1612" alt="image" src="https://github.com/user-attachments/assets/c1f56b0e-fba4-4a89-9b91-5bcfed63cead" />

## Requerimientos del Front-end (Angular o React)

                                                        ANGULAR 

1. Pantalla de Login: Formulario básico para ingresar las credenciales del Usuario pre-cargado.
[Login](front/src/app/components/login/login.component.html),
[Login](front/src/app/components/login/login.component.css),
[Login](front/src/app/components/login/login.component.ts)
<img width="3047" height="1955" alt="image" src="https://github.com/user-attachments/assets/a777da62-183f-4c72-8113-874c9c5fcb59" />

2. Selector de Workspace: Tras el login, una vista o modal simple que muestre los Workspaces disponibles
devueltos por la API para que el usuario elija uno.
[workspace](front/src/app/components/workspace/workspace-selector.component.html),
[workspace](front/src/app/components/workspace/workspace-selector.component.css),
[workspace](front/src/app/components/workspace/workspace-selector.component.ts)
<img width="2982" height="1840" alt="image" src="https://github.com/user-attachments/assets/827bc212-94b1-43bc-8ea4-ec801a0c62c3" />

3. Dashboard de Proyectos: Muestra la lista de Proyectos del Workspace seleccionado.
[dashboard](front/src/app/components/dashboard/dashboard.component.html),
[dashboard](front/src/app/components/dashboard/dashboard.component.css),
[dashboard](front/src/app/components/dashboard/dashboard.component.ts),
[header](front/src/app/components/header/header.component.html),
[header](front/src/app/components/header/header.component.css),
[header](front/src/app/components/header/header.component.ts)

<img width="3802" height="1205" alt="image" src="https://github.com/user-attachments/assets/c83ded6a-01ca-4c69-996b-88c5093d7bbd" />

● Si el usuario entró al Workspace donde es Admin o Editor, debe estar visible un botón de "Crear
Proyecto".
<img width="3802" height="1205" alt="image" src="https://github.com/user-attachments/assets/f3444ae0-b89b-484a-aa7b-fe083350678d" />
<img width="3775" height="1247" alt="image" src="https://github.com/user-attachments/assets/3d99aa11-ced6-447e-8a1b-04b12e883ec9" />
● Si entró al Workspace donde es Lector, el botón de "Crear Proyecto" debe estar oculto o deshabilitado.
<img width="3785" height="1167" alt="image" src="https://github.com/user-attachments/assets/cf0b8051-6674-46b7-82ac-896d6da3114d" />






















