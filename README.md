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

## Configuración

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

### Permisos de los endpoints

- `GET /lists` → Público (no requiere autenticación).
- `GET /lists/**` → Público (no requiere autenticación).
- `POST /lists` → Requiere rol **ADMIN**.
- `DELETE /lists/**` → Requiere rol **ADMIN**.
- Cualquier otro endpoint requiere autenticación básica (**Basic Auth**).
- 
## Probar con Postman

Para facilitar las pruebas de los endpoints, se incluye una colección de **Postman** exportada en el archivo:
*postman/Playlists_API.postman_collection.json*

### Importar la colección

1. Abre **Postman**.
2. Haz clic en el botón **Import** (arriba a la izquierda).
3. Selecciona el archivo `Playlists-Microservice.postman_collection.json` desde la carpeta `postman` del proyecto.
4. Una vez importada, verás la colección **Playlists Microservice** con todos los endpoints listos para probar.

⚠️ **Nota:**
- Algunos endpoints requieren autenticación **Basic Auth** con los usuarios configurados en la sección [Seguridad](#seguridad).
- La URL base utilizada es:  http://localhost:8080
