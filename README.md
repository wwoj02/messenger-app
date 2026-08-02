# Messenger API

Backend for a messenger application built with Spring Boot. The project exposes a REST API for user registration and login, conversation management, and sending and retrieving messages. Authentication is based on JWT.

> Status: work in progress. WebSockets and real-time messaging have not been implemented yet.

## Tech stack

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Security + JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Springdoc OpenAPI / Swagger UI
- Maven

## Features

- User registration
- Login and JWT token generation
- Bearer JWT authentication for protected endpoints
- Retrieving and updating user data
- Creating private and group conversations
- Conversation membership verification
- Sending messages and retrieving message history
- Interactive API documentation with Swagger UI

## Requirements

- JDK 21
- PostgreSQL

## Configuration

Create a local PostgreSQL database, for example:

```sql
CREATE DATABASE messenger_app;
```

Then create `src/main/resources/application.properties` and configure the database connection and JWT secret:

```properties
spring.application.name=messenger

spring.datasource.url=jdbc:postgresql://localhost:5432/messenger-app
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

jwt.secret=YOUR_BASE64_ENCODED_SECRET
jwt.expiration-ms=900000
```

`jwt.secret` must be Base64-encoded and long enough for an HMAC key. Do not commit real passwords or secrets to the repository.

## Running the application

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bat
mvnw.cmd spring-boot:run
```

By default, the application runs at `http://localhost:8080`.

## Swagger UI

Interactive API documentation is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

### Testing protected endpoints with JWT

1. Call `POST /api/auth/register` to create a user.
2. Call `POST /api/auth/login` and copy the `token` field from the response.
3. Click **Authorize** in Swagger UI.
4. Paste the token into `bearerAuth` without the `Bearer` prefix.
5. Swagger will automatically attach the following header to protected requests:

```http
Authorization: Bearer <token>
```

## Endpoints

| Method | Endpoint | JWT required | Description |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | No | Register a user |
| POST | `/api/auth/login` | No | Log in and obtain a JWT |
| GET | `/api/users` | Yes | Get all users |
| GET | `/api/user/{id}` | Yes | Get a user by ID |
| PUT | `/api/user/{id}` | Yes | Update a user profile |
| DELETE | `/api/user/{id}` | Yes | Delete a user |
| POST | `/api/conversations` | Yes | Create a conversation |
| GET | `/api/conversations/{id}` | Yes | Get a conversation |
| POST | `/api/messages` | Yes | Send a message |
| GET | `/api/messages/{conversationId}` | Yes | Get a conversation's message history |

## Roadmap

- [ ] WebSocket / STOMP support for real-time messaging
- [ ] JWT authentication for WebSocket connections
- [ ] Message history pagination
- [ ] API integration tests
- [ ] Consistent error handling
- [ ] Roles and more granular user permissions
