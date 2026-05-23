# Sistema SaaS Multi-Tenant - Gestion de Proyectos

Sistema MVP de gestion de proyectos multi-tenant donde los usuarios pueden pertenecer a multiples espacios de trabajo (Workspaces) con diferentes niveles de acceso segun el rol asignado en cada uno.

## Tecnologias

| Capa | Tecnologia |
|------|------------|
| Backend | Java 17 + Spring Boot 3 |
| Frontend | Angular 17 |
| Base de Datos | H2 (en memoria) |
| Contenedores | Docker + Docker Compose |

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
estos se encuentran en el  backend en (backend/src/main/resources/data.sql)
<img width="925" height="802" alt="image" src="https://github.com/user-attachments/assets/fa2611bd-745d-4fdb-ab98-37c5e3908e6b" />

● 1 Usuario de prueba




