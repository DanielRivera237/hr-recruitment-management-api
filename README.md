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
