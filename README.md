# Task Manager API

RESTful Task Manager service (Spring Boot + JWT)

## Features
- Registration & Login (JWT)
- Role-based access (USER, ADMIN)
- Task CRUD
- Update/Delete allowed only for author or ADMIN
- Filtering tasks by status / authorId / assigneeId
- Swagger UI
- H2 in-memory DB
- JUnit5 tests

## Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Security
- JWT
- Spring Data JPA
- H2 Database
- Swagger OpenAPI
- JUnit5

---

## Run project

```bash
mvn clean install
mvn spring-boot:run
```

---

## Swagger

http://localhost:8080/swagger-ui/index.html

---

## H2 Console

http://localhost:8080/h2-console  
JDBC URL: jdbc:h2:mem:taskdb  
Login: sa

---

## API Examples

### Register
POST /api/auth/register

```json
{
  "username": "user",
  "email": "user@mail.com",
  "password": "12345"
}
```

### Login
POST /api/auth/login

```json
{
  "email": "user@mail.com",
  "password": "12345"
}
```

Response:
```json
{
  "token": "JWT_TOKEN"
}
```

---

## Create Task
POST /api/tasks

Headers:
Authorization: Bearer JWT_TOKEN

```json
{
  "title": "My task",
  "description": "some description",
  "status": "TODO",
  "priority": "HIGH",
  "assigneeId": null
}
```

---

## Filter Tasks

GET /api/tasks?status=IN_PROGRESS  
GET /api/tasks?authorId=1  
GET /api/tasks?assigneeId=2  

---

## Notes
- All users register as USER.
- To make ADMIN you can update role directly in DB.

