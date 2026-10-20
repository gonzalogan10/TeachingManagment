# Teaching Management – Backend

## Project Overview

This repository contains the **backend part** of the *Teaching Management* application.

The backend provides a **RESTful API** and a **data model** to support the management of courses, activities, tasks, and users.
All data is exchanged in **JSON format**.

The current state of the project corresponds to a **Backend MVP (Minimum Viable Product)**:
a minimal but fully functional backend that demonstrates the core concept of the application and can be extended in future iterations.

---

## Implemented Features

### Backend Functionality
- Spring Boot backend application
- REST API (JSON-based)
- Data persistence using Spring Data JPA
- In-memory database (H2)
- Entity model:
  - `Course`
  - `Activity` (updated)
  - `Task` (updated to include a list of solvers and a main solver)
  - `Solver` (new)
  - `Report` (new)
  - `ActivityLoad` (new - DTO for load calculation results)

- Basic user management:
  - `User` entity
  - Roles: `TEACHER`, `STUDENT`
- Application security:
  - Spring Security
  - HTTP Basic authentication
  - Password hashing using BCrypt
- Load calculation service:
  - Calculate total hours spent on activities
  - Calculate total students evaluated
  - Calculate total proportion of work
  - Parse time strings (e.g., "2h", "3 hours") to numeric values

- Controllers (REST API):
  - `UserController` - User management
  - `CourseController` - Course management
  - `ActivityController` - Activity management
  - `TaskController` - Task management
  - `SolverController` - Solver management
  - `ReportController` - Report management
  - `CriterionController` - Criterion management
  - `LoadCalculationController` - Load calculation for activities

- Services (Business Logic):
  - `CustomUserDetailsService` - User authentication
  - `CourseService` - Course operations
  - `ActivityService` - Activity operations
  - `TaskService` - Task operations
  - `SolverService` - Solver operations (assign to tasks, main solver)
  - `ReportService` - Report operations (by task, by solver, by activity)
  - `CriterionService` - Criterion operations
  - `LoadCalculationService` - Calculate hours, students, and proportion for activities

- Repositories (Data Access):
  - `UserRepository`
  - `CourseRepository`
  - `ActivityRepository`
  - `TaskRepository`
  - `SolverRepository`
  - `ReportRepository`
  - `CriterionRepository`

---
## Test (new)
 - For repositories
 - For the domain


## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Security
- H2 Database (in-memory)
- Maven

---

## Project Structure

src/main/java/com/example/teachingmanagement
│
├── controller // REST API endpoints
├── service // Business logic and security services
├── repository // Data access layer (JPA repositories)
├── model // JPA entities and enums
├── security // Spring Security configuration
└── TeachingManagementApplication.java
---

## How to Run the Application

### Requirements
- Java 21
- Maven or Maven Wrapper

### Start the Application

From the project root directory:

mvnw.cmd spring-boot:run


When the application is ready, the following message appears in the console:

Started TeachingManagementApplication

The backend is then available at:

http://localhost:8080

API Usage & Testing
User Management & Authentication
The backend uses HTTP Basic authentication.
Passwords are hashed using BCrypt before being stored in the database.

Create a User (public endpoint)
Endpoint

POST /api/users
Request body

{
  "username": "teacher1",
  "password": "test",
  "role": "TEACHER"
}
PowerShell example

Invoke-RestMethod -Method POST `
  -Uri http://localhost:8080/api/users `
  -ContentType "application/json" `
  -Body '{"username":"teacher1","password":"test","role":"TEACHER"}'


Course Management (secured endpoints)
All course-related endpoints require authentication.

Create a Course :

Endpoint

POST /api/courses
Request body

{
  "name": "IVS",
  "code": "IVS2025"
}
PowerShell example

Invoke-RestMethod -Method POST `
  -Uri http://localhost:8080/api/courses `
  -Credential (Get-Credential) `
  -ContentType "application/json" `
  -Body '{"name":"IVS","code":"IVS2025"}'


Get All Courses
Endpoint

GET /api/courses
PowerShell example

Invoke-RestMethod `
 -Uri http://localhost:8080/api/courses `
 -Credential (Get-Credential)
Database Access (H2 Console)
The application uses an in-memory H2 database for development and testing purposes.

Access URL :
http://localhost:8080/h2
Connection Parameters

JDBC URL : jdbc:h2:mem:teachingdb
User     : sa
Password : (empty)

Example SQL Queries :

SELECT * FROM USERS;
SELECT * FROM COURSE;
SELECT * FROM ACTIVITY;
SELECT * FROM TASK;
SELECT * FROM SOLVER;
SELECT * FROM TASK_SOLVERS;
SELECT * FROM REPORT;

The H2 console is enabled for development only and explicitly allowed in the security configuration.

### NEW Endpoints 

#### Solver Endpoints
- `POST /api/solvers` → Create a new solver
- `GET /api/solvers` → Find all solvers
- `POST /api/solvers/{solverId}/tasks/{taskId}` → Adds a solver to a task in the solver list if it is not already present
- `POST /api/solvers/{solverId}/main-solver/{taskId}` → Assign a solver as main solver for a task (adds to list if not already present)

#### Report Endpoints
- `POST /api/reports` → Create a new report
- `GET /api/reports` → Get all reports
- `POST /api/reports/task/{taskId}/solver/{solverId}` → Create a report for a specific task and solver
- `GET /api/reports/solver/{solverId}` → Get reports by solver
- `GET /api/reports/task/{taskId}` → Get reports by task

#### Load Calculation Endpoints
- `GET /api/activities/{activityId}/reports` → Get all reports for a specific activity
- `GET /api/activities/{activityId}/load` → Calculate the total load (hours, students, proportion) for an activity
- `GET /api/activities/{activityId}/load/hours` → Get total hours spent on the activity
- `GET /api/activities/{activityId}/load/students` → Get total students evaluated in the activity
- `GET /api/activities/{activityId}/load/proportion` → Get total proportion of work on the activity
