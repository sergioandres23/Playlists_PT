# Playlists Microservice

Este proyecto es un microservicio desarrollado en **Spring Boot** que permite gestionar **playlists**.  
Incluye integración con base de datos en memoria **H2**, autenticación básica con **Spring Security**, y un conjunto de endpoints REST.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- H2 Database
- Maven

---

## ⚙️ Configuración

El proyecto utiliza una base de datos **H2 en memoria**.  
Las configuraciones principales se encuentran en `src/main/resources/application.properties`:

```properties
# =========================================
# Server
# =========================================
spring.application.name=playlists-ms
server.port=8080

# =========================================
# H2 Database
# =========================================
spring.datasource.url=jdbc:h2:mem:playlistsdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=1234

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Mostrar SQL en logs
spring.jpa.show-sql=true
```
## Endpoints


| Método | Endpoint        | Descripción                        | Rol requerido |
|--------|-----------------|------------------------------------|---------------|
| GET    | `/lists`        | Obtiene todas las playlists        | Público       |
| GET    | `/lists/{name}` | Obtiene una playlist por su nombre | Público       |
| POST   | `/lists`        | Crea una nueva playlist            | ADMIN         |
| DELETE | `/lists/{name}` | Elimina una playlist por su nombre | ADMIN         |

## Seguridad

Este proyecto utiliza **Spring Security** con autenticación en memoria.  
Se definen dos usuarios por defecto:

- **Usuario con rol USER**
    - username: `user`
    - password: `user123`

- **Usuario con rol ADMIN**
    - username: `admin`
    - password: `admin123`

### 🔑 Permisos de los endpoints

- `GET /lists` → Público (no requiere autenticación).
- `GET /lists/**` → Público (no requiere autenticación).
- `POST /lists` → Requiere rol **ADMIN**.
- `DELETE /lists/**` → Requiere rol **ADMIN**.
- Cualquier otro endpoint requiere autenticación básica (**Basic Auth**).