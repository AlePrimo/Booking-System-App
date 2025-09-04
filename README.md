# 📅 Booking System App

Un sistema de reservas completo desarrollado con **Spring Boot**, que permite gestionar usuarios, servicios (offerings), reservas (bookings), pagos y notificaciones.

El proyecto incluye autenticación, paginación personalizada, manejo de excepciones centralizado y documentación con **Swagger UI**.

---

## 🚀 Tecnologías utilizadas

- **Java 21** ☕
- **Spring Boot 3**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **Hibernate (ORM)**
- **MySQL** (Base de datos)
- **Maven** (Gestión de dependencias)
- **Lombok** (Reducción de boilerplate)
- **JUnit 5 & Mockito** (Pruebas unitarias e integración)
- **Swagger / OpenAPI 3** (Documentación interactiva)

---

## 📂 Arquitectura del proyecto

El proyecto sigue una **arquitectura en capas**, organizada de la siguiente forma:

```
Entity → Representación de las tablas de la BD
Repository / DAO → Acceso a datos
Service → Lógica de negocio
Controller → Exposición de endpoints REST
DTOs & Mappers → Comunicación entre capas
Exception Handling → Manejo centralizado de errores
Tests → Cobertura para Repository, Service y Controller
```

📌 **Diagrama de Arquitectura (alto nivel):**

```
         [ Cliente / Frontend ]
                  |
             (Swagger UI)
                  |
            [ Controller ]
                  |
            [  Service  ]
                  |
    -----------------------------
    |                           |
[ Repository ]           [ Mapper / DTO ]
    |                           |
 [   Base de Datos (MySQL)   ]
```

---

## ✨ Funcionalidades principales

✔️ Gestión de **Usuarios** con roles  
✔️ Gestión de **Servicios / Offerings**  
✔️ Creación y seguimiento de **Reservas (Bookings)**  
✔️ Registro de **Pagos (Payments)** asociados a reservas  
✔️ Envío y gestión de **Notificaciones (Email / SMS)**  
✔️ **Paginación personalizada** con `PageResponse<T>`  
✔️ **Swagger UI** para probar la API de manera interactiva  
✔️ **Tests completos** de todas las capas  

---

## 📖 Endpoints principales

### 🔹 Usuarios (`/api/users`)
- `GET /api/users` → Listar usuarios con paginación  
- `POST /api/users` → Crear usuario  
- `GET /api/users/{id}` → Obtener usuario por ID  

### 🔹 Servicios (`/api/offerings`)
- `GET /api/offerings` → Listar servicios con paginación  
- `POST /api/offerings` → Crear servicio  
- `GET /api/offerings/{id}` → Obtener servicio por ID  

### 🔹 Reservas (`/api/bookings`)
- `GET /api/bookings` → Listar reservas con paginación  
- `POST /api/bookings` → Crear reserva  
- `GET /api/bookings/{id}` → Obtener reserva por ID  

### 🔹 Pagos (`/api/payments`)
- `GET /api/payments` → Listar pagos con paginación  
- `POST /api/payments` → Crear pago  

### 🔹 Notificaciones (`/api/notifications`)
- `GET /api/notifications` → Listar notificaciones con paginación  
- `POST /api/notifications` → Crear notificación  
- `DELETE /api/notifications/{id}` → Eliminar notificación  

---

## 🛠️ Configuración

### Clonar el repositorio

```bash
git clone https://github.com/AlePrimo/Booking-System-App.git
cd Booking-System-App
```

### Configurar la base de datos en `application.properties` o `application.yml`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookingdb
spring.datasource.username=root
spring.datasource.password=tu_clave
spring.jpa.hibernate.ddl-auto=update
```

### Ejecutar el proyecto

```bash
mvn spring-boot:run
```

---

## 📑 Documentación Swagger

Una vez levantado el proyecto, accedé a la documentación completa desde:  

👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Ahí vas a poder probar todos los endpoints de manera interactiva.

---

## ✅ Testing

El proyecto cuenta con **tests unitarios e integración** para todas las capas:

- `RepositoryTest` → Pruebas de consultas a la BD  
- `ServiceImplTest` → Pruebas de la lógica de negocio con Mockito  
- `ControllerTest` → Pruebas de endpoints con MockMvc y @WebMvcTest  

Ejecutar los tests:

```bash
mvn test
```


## Badges

![Coverage](https://raw.githubusercontent.com/AlePrimo/Booking-System-App/main/.github/badges/jacoco.svg)
![Branch](https://raw.githubusercontent.com/AlePrimo/Booking-System-App/main/.github/badges/branches.svg)





---

## 📌 Ejemplo de respuesta paginada

```json
{
  "content": [
    {
      "id": 1,
      "name": "Juan Pérez",
      "email": "juan.perez@mail.com",
      "roles": ["CUSTOMER"]
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 👨‍💻 Autor

**Alejandro Primo**  
📌 Desarrollador Java Backend  
🔗 [LinkedIn](https://linkedin.com)

---
