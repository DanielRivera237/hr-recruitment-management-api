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

---

# Endpoints

## 1. Autenticación

Ruta base: `/api/auth`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/auth/register` | Público | 201 | Registra un candidato y devuelve un JWT |
| POST | `/api/auth/login` | Público | 200 | Autentica al usuario y devuelve un JWT |

### RegisterRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `name` | String | Sí | Máximo 100 caracteres |
| `surname` | String | Sí | Máximo 100 caracteres |
| `email` | String | Sí | Correo válido, máximo 150 y único |
| `password` | String | Sí | Entre 8 y 72 caracteres |
| `confirmPassword` | String | Sí | Debe coincidir con `password` |

```json
{
  "name": "Ana",
  "surname": "López",
  "email": "ana@example.com",
  "password": "ClaveSegura123",
  "confirmPassword": "ClaveSegura123"
}
```

### LoginRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `email` | String | Sí | Correo válido |
| `password` | String | Sí | Contraseña del usuario |

```json
{
  "email": "admin@rrhh.com",
  "password": "admin12345uca!!!"
}
```

---

## 2. Empresas

Ruta base: `/api/companies`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/companies` | ADMIN, RECRUITER | 201 | Crea una empresa |
| GET | `/api/companies` | ADMIN, RECRUITER | 200 | Lista todas las empresas |
| GET | `/api/companies/{id}` | ADMIN, RECRUITER | 200 | Obtiene una empresa por ID |
| PUT | `/api/companies/{id}` | ADMIN, RECRUITER | 200 | Actualiza una empresa |
| DELETE | `/api/companies/{id}` | ADMIN, RECRUITER | 204 | Desactiva una empresa de forma lógica |

### CompanyRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `name` | String | Sí | Máximo 150; nombre único |
| `description` | String | No | Máximo 1000 |
| `location` | String | Sí | Máximo 150 |
| `sector` | String | Sí | Máximo 100 |
| `website` | String | No | Máximo 255 |

```json
{
  "name": "Tech Solutions SV",
  "description": "Desarrollo de software",
  "location": "San Salvador",
  "sector": "Tecnología",
  "website": "https://example.com"
}
```

---

## 3. Perfil del candidato

Ruta base: `/api/candidates/profile`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/candidates/profile/me` | Autenticado | 201 | Crea o completa el perfil del usuario actual |
| GET | `/api/candidates/profile/me` | Autenticado | 200 | Obtiene el perfil del usuario actual |
| PUT | `/api/candidates/profile/me` | Autenticado | 200 | Actualiza el perfil del usuario actual |

### CandidateProfileRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `phone` | String | No | Máximo 25 |
| `address` | String | No | Máximo 255 |
| `professionalSummary` | String | No | Máximo 1500 |
| `yearsExperience` | Integer | No | Mínimo 0 |
| `salaryExpectation` | BigDecimal | No | No negativa |
| `availability` | String | No | Máximo 100 |

```json
{
  "phone": "7777-7777",
  "address": "San Salvador",
  "professionalSummary": "Desarrollador backend Java",
  "yearsExperience": 2,
  "salaryExpectation": 1200.00,
  "availability": "Inmediata"
}
```

---

## 4. Educación

Ruta base: `/api/candidates/education`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/candidates/education` | Autenticado | 201 | Agrega un estudio al perfil actual |
| GET | `/api/candidates/education/me` | Autenticado | 200 | Lista la educación del perfil actual |
| PUT | `/api/candidates/education/{id}` | Autenticado | 200 | Actualiza un registro propio |
| DELETE | `/api/candidates/education/{id}` | Autenticado | 204 | Elimina un registro propio |

### EducationRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `institution` | String | Sí | Máximo 150 |
| `degree` | String | Sí | Máximo 150 |
| `fieldOfStudy` | String | Sí | Máximo 150 |
| `startDate` | LocalDate | Sí | Formato `YYYY-MM-DD` |
| `endDate` | LocalDate | No | Formato `YYYY-MM-DD` |
| `currentlyStudying` | Boolean | No | Indica si continúa estudiando |

```json
{
  "institution": "Universidad Centroamericana",
  "degree": "Ingeniería Informática",
  "fieldOfStudy": "Tecnologías de Información",
  "startDate": "2022-01-15",
  "endDate": null,
  "currentlyStudying": true
}
```

---

## 5. Experiencia laboral

Ruta base: `/api/candidates/experience`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/candidates/experience` | Autenticado | 201 | Agrega experiencia al perfil actual |
| GET | `/api/candidates/experience/me` | Autenticado | 200 | Lista la experiencia del perfil actual |
| PUT | `/api/candidates/experience/{id}` | Autenticado | 200 | Actualiza una experiencia propia |
| DELETE | `/api/candidates/experience/{id}` | Autenticado | 204 | Elimina una experiencia propia |

### WorkExperienceRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `companyName` | String | Sí | Máximo 150 |
| `position` | String | Sí | Máximo 150 |
| `description` | String | No | Máximo 1500 |
| `startDate` | LocalDate | Sí | Formato `YYYY-MM-DD` |
| `endDate` | LocalDate | No | Formato `YYYY-MM-DD` |
| `currentlyWorking` | Boolean | No | Indica si continúa trabajando |

```json
{
  "companyName": "Empresa Demo",
  "position": "Desarrollador Junior",
  "description": "Desarrollo de APIs REST",
  "startDate": "2024-01-10",
  "endDate": null,
  "currentlyWorking": true
}
```

---

## 6. Habilidades

Ruta base: `/api/candidates/skills`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/candidates/skills` | Autenticado | 201 | Agrega una habilidad al candidato actual |
| GET | `/api/candidates/skills/me` | Autenticado | 200 | Lista las habilidades del candidato actual |
| DELETE | `/api/candidates/skills/{id}` | Autenticado | 204 | Quita una habilidad del candidato actual |

### SkillRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `name` | String | Sí | Máximo 100 caracteres |

```json
{
  "name": "Java"
}
```

---

## 7. Vacantes

Ruta base: `/api/job-offers`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/job-offers` | ADMIN, RECRUITER | 201 | Crea una vacante en `DRAFT` |
| GET | `/api/job-offers` | ADMIN, RECRUITER | 200 | Lista las vacantes |
| GET | `/api/job-offers/{id}` | ADMIN, RECRUITER | 200 | Obtiene una vacante |
| PUT | `/api/job-offers/{id}` | ADMIN, RECRUITER | 200 | Actualiza una vacante `DRAFT` |
| PATCH | `/api/job-offers/{id}/publish` | ADMIN, RECRUITER | 200 | Publica una vacante `DRAFT` |
| PATCH | `/api/job-offers/{id}/close` | ADMIN, RECRUITER | 200 | Cierra una vacante `PUBLISHED` |
| DELETE | `/api/job-offers/{id}` | ADMIN, RECRUITER | 204 | Cambia la vacante a `CANCELLED` |

### JobOfferRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `title` | String | Sí | Máximo 150 |
| `description` | String | Sí | Máximo 2000 |
| `requirements` | String | Sí | Máximo 2000 |
| `salaryMin` | BigDecimal | Sí | Mayor o igual a 0; no mayor que `salaryMax` |
| `salaryMax` | BigDecimal | Sí | Mayor que 0 |
| `modality` | Enum | Sí | `ON_SITE`, `REMOTE` o `HYBRID` |
| `location` | String | Sí | Máximo 150 |
| `closingDate` | LocalDate | Sí | Fecha futura |
| `companyId` | Long | Sí | Empresa existente y activa |
| `recruiterProfileId` | Long | Sí | Reclutador perteneciente a la empresa |
| `skillIds` | Set<Long> | No | IDs de habilidades requeridas |

```json
{
  "title": "Desarrollador Java",
  "description": "Desarrollo de servicios backend",
  "requirements": "Java, Spring Boot y PostgreSQL",
  "salaryMin": 1000.00,
  "salaryMax": 1800.00,
  "modality": "HYBRID",
  "location": "San Salvador",
  "closingDate": "2026-12-31",
  "companyId": 1,
  "recruiterProfileId": 1,
  "skillIds": [1, 2]
}
```

---

## 8. Postulaciones

Ruta base: `/api/applications`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/applications` | ADMIN, RECRUITER, CANDIDATE | 201 | Postula un candidato a una vacante publicada |
| GET | `/api/applications` | ADMIN, RECRUITER, CANDIDATE | 200 | Lista todas las postulaciones |
| GET | `/api/applications/{id}` | ADMIN, RECRUITER, CANDIDATE | 200 | Obtiene una postulación |
| GET | `/api/applications/candidate/{candidateProfileId}` | ADMIN, RECRUITER, CANDIDATE | 200 | Lista postulaciones de un candidato |
| GET | `/api/applications/job-offer/{jobOfferId}` | ADMIN, RECRUITER, CANDIDATE | 200 | Lista postulaciones de una vacante |
| PATCH | `/api/applications/{id}/status` | ADMIN, RECRUITER, CANDIDATE | 200 | Cambia el estado según el flujo permitido |

### ApplicationRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `candidateProfileId` | Long | Sí | Candidato existente |
| `jobOfferId` | Long | Sí | Vacante existente y `PUBLISHED` |
| `coverLetter` | String | No | Máximo 2000; no se permite duplicar postulación |

```json
{
  "candidateProfileId": 1,
  "jobOfferId": 1,
  "coverLetter": "Estoy interesado en formar parte del equipo."
}
```

### ApplicationStatusRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `status` | Enum | Sí | `APPLIED`, `REVIEWED`, `TECHNICAL_INTERVIEW`, `REJECTED` o `HIRED` |

```json
{
  "status": "REVIEWED"
}
```

---

## 9. Entrevistas

Ruta base: `/api/interviews`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/interviews` | ADMIN, RECRUITER | 201 | Agenda una entrevista |
| GET | `/api/interviews` | ADMIN, RECRUITER | 200 | Lista entrevistas |
| GET | `/api/interviews/{id}` | ADMIN, RECRUITER | 200 | Obtiene una entrevista |
| GET | `/api/interviews/application/{applicationId}` | ADMIN, RECRUITER | 200 | Lista entrevistas por postulación |
| PUT | `/api/interviews/{id}` | ADMIN, RECRUITER | 200 | Reprograma una entrevista |
| PATCH | `/api/interviews/{id}/status` | ADMIN, RECRUITER | 200 | Actualiza el estado de la entrevista |

### InterviewRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `scheduledDate` | LocalDateTime | Sí | Fecha y hora futura, formato ISO-8601 |
| `meetingUrl` | String | Sí | Máximo 500 |
| `type` | Enum | Sí | `HUMAN_RESOURCES`, `TECHNICAL` o `FINAL` |
| `applicationId` | Long | Sí | Postulación en etapa válida |
| `recruiterProfileId` | Long | Sí | Reclutador de la empresa de la vacante |

```json
{
  "scheduledDate": "2026-08-10T14:30:00",
  "meetingUrl": "https://meet.example.com/entrevista-1",
  "type": "TECHNICAL",
  "applicationId": 1,
  "recruiterProfileId": 1
}
```

### InterviewStatusRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `status` | Enum | Sí | `SCHEDULED`, `COMPLETED`, `CANCELLED` o `RESCHEDULED` |

```json
{
  "status": "COMPLETED"
}
```

---

## 10. Comentarios privados

Ruta base: `/api/recruiter-comments`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/recruiter-comments` | ADMIN, RECRUITER | 201 | Crea un comentario privado |
| GET | `/api/recruiter-comments/application/{applicationId}` | ADMIN, RECRUITER | 200 | Lista comentarios de una postulación |
| DELETE | `/api/recruiter-comments/{id}` | ADMIN, RECRUITER | 204 | Elimina un comentario |

### RecruiterCommentRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `comment` | String | Sí | Máximo 2000 |
| `applicationId` | Long | Sí | Postulación existente |
| `recruiterProfileId` | Long | Sí | Debe pertenecer a la empresa de la vacante |

```json
{
  "comment": "Buen desempeño técnico y comunicación clara.",
  "applicationId": 1,
  "recruiterProfileId": 1
}
```

---

## 11. Pruebas técnicas

Ruta base: `/api/technical-tests`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/technical-tests` | ADMIN, RECRUITER, CANDIDATE | 201 | Asigna una prueba a una postulación en entrevista técnica |
| GET | `/api/technical-tests/{id}` | ADMIN, RECRUITER, CANDIDATE | 200 | Obtiene una prueba |
| GET | `/api/technical-tests/application/{applicationId}` | ADMIN, RECRUITER, CANDIDATE | 200 | Lista pruebas por postulación |
| PATCH | `/api/technical-tests/{id}/submit` | ADMIN, RECRUITER, CANDIDATE | 200 | Marca una prueba pendiente como enviada |
| PATCH | `/api/technical-tests/{id}/review` | ADMIN, RECRUITER, CANDIDATE | 200 | Registra resultado y marca `REVIEWED` |

### TechnicalTestRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `title` | String | Sí | Máximo 150 |
| `externalUrl` | String | Sí | Máximo 500 |
| `deadline` | LocalDateTime | Sí | Fecha futura |
| `applicationId` | Long | Sí | Postulación en `TECHNICAL_INTERVIEW` |

```json
{
  "title": "Prueba backend Java",
  "externalUrl": "https://example.com/test/123",
  "deadline": "2026-08-15T23:59:00",
  "applicationId": 1
}
```

### TechnicalTestResultRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `result` | BigDecimal | Sí | Valor entre 0 y 100 |

```json
{
  "result": 87.50
}
```

---

## 12. Notificaciones

Ruta base: `/api/notifications`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/notifications` | ADMIN, RECRUITER, CANDIDATE | 201 | Crea una notificación |
| GET | `/api/notifications/user/{userId}` | ADMIN, RECRUITER, CANDIDATE | 200 | Lista notificaciones de un usuario |
| GET | `/api/notifications/user/{userId}/unread` | ADMIN, RECRUITER, CANDIDATE | 200 | Lista notificaciones no leídas |
| PATCH | `/api/notifications/{id}/read` | ADMIN, RECRUITER, CANDIDATE | 200 | Marca una notificación como leída |
| DELETE | `/api/notifications/{id}` | ADMIN, RECRUITER, CANDIDATE | 204 | Elimina una notificación |

### NotificationRequest

| Campo | Tipo | Obligatorio | Reglas |
|---|---|---:|---|
| `title` | String | Sí | Máximo 150 |
| `message` | String | Sí | Máximo 2000 |
| `userId` | Long | Sí | Usuario existente |

```json
{
  "title": "Cambio de estado",
  "message": "Tu postulación pasó a entrevista técnica.",
  "userId": 2
}
```

---

## 13. Scoring de candidatos

Ruta base: `/api/scoring`

| Método | Ruta | Acceso | Éxito | Descripción |
|---|---|---|---:|---|
| POST | `/api/scoring/applications/{applicationId}` | ADMIN, RECRUITER | 200 | Calcula y guarda el score de una postulación |

### Distribución del score

| Criterio | Puntaje máximo |
|---|---:|
| Habilidades | 60 |
| Ubicación | 20 |
| Expectativa salarial | 20 |
| **Total** | **100** |

---

## Reglas de negocio principales

- Un correo electrónico no puede registrarse más de una vez.
- El registro público crea únicamente usuarios con rol `CANDIDATE`.
- Una empresa se desactiva de forma lógica; no se elimina físicamente.
- El salario mínimo de una vacante no puede superar el salario máximo.
- Una vacante solo puede editarse mientras esté en estado `DRAFT`.
- Solo una vacante `DRAFT` puede publicarse y solo una `PUBLISHED` puede cerrarse.
- Un candidato no puede postular dos veces a la misma vacante.
- Solo se aceptan postulaciones a vacantes `PUBLISHED`.
- Flujo de postulación: `APPLIED → REVIEWED → TECHNICAL_INTERVIEW → HIRED`.
- Una postulación puede pasar a `REJECTED` desde etapas previas.
- Una postulación `REJECTED` o `HIRED` no puede cambiar nuevamente de estado.
- Las entrevistas deben programarse en fechas futuras y por un reclutador de la empresa correspondiente.
- Las pruebas técnicas solo se asignan en etapa `TECHNICAL_INTERVIEW`.
- El resultado de una prueba técnica debe estar entre 0 y 100.
- El scoring total es de 100 puntos.

---

## Códigos HTTP

| Código | Significado | Uso |
|---|---|---|
| 200 | OK | Consulta, actualización, cambio de estado o autenticación exitosa |
| 201 | Created | Recurso creado correctamente |
| 204 | No Content | Eliminación, cancelación o desactivación exitosa |
| 400 | Bad Request | Validación o regla de negocio incumplida |
| 401 | Unauthorized | Credenciales inválidas o falta de autenticación |
| 403 | Forbidden | Usuario autenticado sin el rol requerido |
| 404 | Not Found | Recurso solicitado inexistente |
| 409 | Conflict | Dato duplicado o conflicto de unicidad |
| 500 | Internal Server Error | Error inesperado del servidor |

### Estructura estándar de error

```json
{
  "timestamp": "2026-06-28T19:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Existen errores de validación",
  "path": "/api/companies",
  "validationErrors": {
    "name": "El nombre de la empresa es obligatorio"
  }
}
```

---

## Flujo recomendado de prueba

1. Ejecutar PostgreSQL y crear la base `rrhh_db`.
2. Configurar las variables de entorno.
3. Ejecutar la aplicación.
4. Abrir Insomnia y crear una colección para la API.
5. Registrar un candidato o iniciar sesión con un usuario existente.
6. Copiar el token JWT y configurarlo mediante `Auth → Bearer Token`.
7. Crear los datos base en este orden:
   - Empresa
   - Reclutador existente
   - Habilidades
   - Vacante
   - Publicación
   - Candidato y perfil
   - Postulación
8. Continuar el proceso:
   - Revisión
   - Entrevista técnica
   - Prueba
   - Scoring
   - Contratación o rechazo

---

## Integrantes

- Marden Eliuth Larios Orellana — 00029223
- Josué Daniel Rivera Alvarenga — 00048722
- David Alejandro Sandoval Quijano — 00379422
- Josué Daniel Pérez Nájera — 00027822

**Asignatura:** Programación N-Capas  
**Catedrática:** Ing. Luisa Arévalo  
**Universidad Centroamericana José Simeón Cañas**

