# Sistema de Gestión de RRHH

API REST para gestionar empresas, candidatos, vacantes, postulaciones y etapas del proceso de selección.

El backend está desarrollado con arquitectura por capas, utiliza PostgreSQL para persistencia, JWT para autenticación e Insomnia para realizar pruebas de los endpoints.

---

## Información general

| Campo | Tipo | Obligatorio | Reglas / descripción |
|---|---|---:|---|
| URL base local | URL | Sí | `http://localhost:8080` |
| Cliente de pruebas | Aplicación | No | Insomnia |
| Autenticación en Insomnia | Bearer Token | Según endpoint | `Auth → Bearer Token → pegar el JWT` |
| Formato de datos | JSON | Sí | `Content-Type: application/json` |
| Autenticación | Bearer JWT | Según endpoint | `Authorization: Bearer <token>` |

## Tecnologías

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JJWT 0.12.6
- PostgreSQL
- Bean Validation
- Lombok
- Insomnia

## Variables de entorno

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/rrhh_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=TU_CONTRASENIA
JWT_SECRET_KEY=CLAVE_BASE64_SEGURA
JWT_EXPIRATION_TIME=86400000
```

> El archivo `.env` debe mantenerse fuera del repositorio. La expiración predeterminada del token es de 24 horas.

---

## Autenticación y autorización

Los endpoints de registro e inicio de sesión son públicos. El registro crea usuarios con rol `CANDIDATE`. El resto de endpoints requiere un JWT válido según las reglas de autorización configuradas.

### Uso del token

```http
Authorization: Bearer TOKEN_JWT
```

En Insomnia: `Auth → Bearer Token → pegar únicamente el token JWT`.

### Roles configurados

| Rol | Descripción |
|---|---|
| `ADMIN` | Acceso administrativo a los módulos protegidos |
| `RECRUITER` | Gestión de empresas, vacantes, entrevistas, comentarios y scoring |
| `CANDIDATE` | Perfil, educación, experiencia, habilidades, postulaciones, pruebas y notificaciones |

### Matriz de autorización

| Ruta | Acceso | Descripción |
|---|---|---|
| `/api/auth/**` | Público | Registro e inicio de sesión |
| `/api/companies/**` | ADMIN, RECRUITER | Gestión de empresas |
| `/api/job-offers/**` | ADMIN, RECRUITER | Gestión y consulta de vacantes |
| `/api/applications/**` | ADMIN, RECRUITER, CANDIDATE | Gestión de postulaciones |
| `/api/interviews/**` | ADMIN, RECRUITER | Gestión de entrevistas |
| `/api/recruiter-comments/**` | ADMIN, RECRUITER | Comentarios privados |
| `/api/technical-tests/**` | ADMIN, RECRUITER, CANDIDATE | Pruebas técnicas |
| `/api/notifications/**` | ADMIN, RECRUITER, CANDIDATE | Notificaciones |
| `/api/scoring/**` | ADMIN, RECRUITER | Cálculo de score |
| Otros endpoints autenticados | Usuario autenticado | Perfil, educación, experiencia y skills |
