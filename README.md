# Student Management System

A production-ready, full-stack Student Management System web application built with **Spring Boot 3**, **Spring Data JPA**, **PostgreSQL**, and **Thymeleaf**. 

This application follows a clean layered architecture (Controller → Service → Repository → Database) and uses the Data Transfer Object (DTO) pattern to decouple database schemas from API and UI presentation layers.

---

## 📷 Screenshots

<p align="center">
  <img src="screenshots/VK.png" alt="Dashboard" width="800">
  <br><i>Dashboard Overview</i><br><br>
  <img src="screenshots/VK1.png" alt="Students List" width="800">
  <br><i>Students List & Search</i><br><br>
  <img src="screenshots/VK2.png" alt="Add Student" width="800">
  <br><i>Add Student Form</i><br><br>
  <img src="screenshots/VK3.png" alt="Update Student" width="800">
  <br><i>Update Student Form</i><br><br>
  <img src="screenshots/vk5.png" alt="Delete Student" width="800">
  <br><i>Delete / Validation Error Alerts</i><br>
</p>

---

## 🚀 Key Features

* **Dashboard Overview**: Metrics widgets showing total enrolled students, total courses, average student age, and dynamic course distribution charts.
* **Student Directory**: Paginated, searchable, and sortable database of all registered students.
* **Layered Architecture**: Decoupled database models and API contracts using Lombok-based DTOs and mapper components.
* **Input Validation**: Strict validation rules for student registration (unique enrollment numbers, non-empty fields, valid emails, and past birthdates).
* **Global Exception Handling**: Consolidated JSON error responses for APIs and clean flash-message redirects for the Web portal.
* **Interactive APIs**: Full OpenAPI/Swagger documentation integrated and ready to test.

---

## 🛠️ Technology Stack

* **Backend**: Spring Boot 3.4.0 (Java 21/25)
* **Persistence**: Spring Data JPA & Hibernate
* **Database**: PostgreSQL (v12+)
* **Frontend**: Thymeleaf templates, Bootstrap 5, Chart.js, and custom responsive CSS
* **Utilities**: Project Lombok (v1.18.40) & SLF4J logging
* **Documentation**: Springdoc OpenAPI UI (Swagger)

---

## ⚙️ Setup Instructions

### 1. Prerequisites
* **Java**: JDK 21 or JDK 25 installed.
* **Database**: A running PostgreSQL instance.

### 2. Database Setup
Create a PostgreSQL database named `postgres` (or your preferred database name):
```sql
CREATE DATABASE postgres;
```

### 3. Application Configuration
Create environment variables or configure your settings in [src/main/resources/application.properties](src/main/resources/application.properties):
```properties
spring.datasource.url=jdbc:postgresql://localhost:2178/postgres
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
```

---

## 🏃 Running the Application

To run the application locally, set your database password in the `DB_PASSWORD` environment variable and use the local Maven wrapper:

### Windows (PowerShell)
```powershell
$env:DB_PASSWORD="your_postgres_password"
.\mvnw.cmd spring-boot:run
```

### macOS / Linux
```bash
export DB_PASSWORD="your_postgres_password"
./mvnw spring-boot:run
```

Once started, the application will be accessible at:
* **Web UI Portal**: [http://localhost:8080](http://localhost:8080)
* **Swagger Documentation**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **API OpenAPI Spec Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 📖 REST API Endpoints

The application exposes the following endpoints (all requests/responses use `StudentDto` format):

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/api/students` | Retrieve all students (with optional sorting/searching parameters) |
| **GET** | `/api/students/{id}` | Retrieve a student by database ID |
| **GET** | `/api/students/enrollment/{enrollmentNo}` | Retrieve a student by unique enrollment number |
| **GET** | `/api/students/search?query=...` | Search for students by name or course |
| **POST** | `/api/students` | Register a new student record (performs validation) |
| **PUT** | `/api/students/{id}` | Update student details by ID |
| **DELETE** | `/api/students/{id}` | Remove a student record by ID |
