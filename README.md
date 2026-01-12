# LCWD Foodies – Backend Service

LCWD Foodies is a **containerized backend system** for a food delivery platform, built to demonstrate **real-world backend engineering practices**. The project focuses on clean architecture, security, database design, and reproducible deployments using Docker.

This is a **backend-only project** with complete API documentation via Swagger and a one-command local setup using Docker Compose.

---

## 🚀 Features

### 🔐 Authentication & Authorization

* JWT-based stateless authentication
* Custom JWT authentication filter for token validation and request authorization
* Role-based access control (RBAC)

  * `ADMIN`
  * `RESTAURANT_ADMIN`
  * `USER`
  * `DELIVERY_BOY`
* Method-level security using Spring Security

### 📧 Email Integration

* Email workflows implemented using **JavaMailSender**
* OTP / token-based flows (e.g., password reset)
* SMTP configuration externalized via environment variables

### 💳 Payment Integration

* Razorpay payment gateway integration
* Secure key handling via environment variables
* Backend-driven payment order creation and validation

### 🗄 Database & Migrations

* MySQL database
* Schema versioning using **Flyway migrations**
* Safe and controlled database evolution

### ⏱ Schedulers

* Background jobs using Spring `@Scheduled`
* Timezone-aware execution (Asia/Kolkata)
* Periodic cleanup and maintenance tasks

### 🧯 Global Exception Handling

* Centralized exception handling using `@RestControllerAdvice`
* Consistent and structured error responses
* Covers validation, security, and business exceptions

### 🖼 File Uploads

* Image upload support for restaurant banners and food items
* Multipart file handling with validation
* File storage paths configurable via environment variables

### 📄 API Documentation

* Swagger / OpenAPI integration using Springdoc
* Auto-generated API documentation
* Interactive API testing via Swagger UI

### 📦 Dockerized Setup

* Spring Boot backend container
* MySQL database container with persistent volumes
* One-command startup using Docker Compose
* Adminer included for database inspection

### 🩺 Health Checks

* Spring Boot Actuator integration
* `/actuator/health` endpoint for monitoring

---

## 🛠 Tech Stack

* **Java 21**
* **Spring Boot**
* **Spring Security (JWT)**
* **MySQL 8**
* **Flyway**
* **Docker & Docker Compose**
* **Swagger / OpenAPI (Springdoc)**

---

## 🧱 Architecture Overview

The application follows a layered architecture:

```
Controller → Service → Repository → Database
```

* Controllers handle HTTP requests and responses
* Services contain business logic
* Repositories handle data access
* Security is enforced via custom JWT filters and role-based rules

All components are containerized for consistency and portability and are designed to be **cloud-ready via environment-based configuration**.

---

## ▶️ How to Run Locally

### Prerequisites

* Docker
* Docker Compose

### Start the application

```bash
docker-compose up -d
```

This starts:

* Spring Boot backend
* MySQL database
* Adminer (DB UI)

---

## 🌐 Local Access URLs

* **Backend API**: [http://localhost:8063](http://localhost:8063)
* **Swagger UI**: [http://localhost:8063/swagger-ui/index.html](http://localhost:8063/swagger-ui/index.html)
* **Health Check**: [http://localhost:8063/actuator/health](http://localhost:8063/actuator/health)
* **Adminer**: [http://localhost:8081](http://localhost:8081)

---

## 🔐 Security Notes

* Public endpoints are explicitly whitelisted
* All sensitive configuration is handled via environment variables
* JWT tokens are required for secured endpoints

---

## ⚙️ Environment Configuration

The application is fully environment-driven. Key variables include:

* Database credentials
* JWT secret
* Mail credentials
* Payment gateway keys
* Active Spring profile

This allows seamless switching between **local**, **Docker**, and **cloud** environments without code changes.

---

## 🎯 Interview Notes

For interviews:

* The backend and database are run locally using Docker Compose
* Data persistence is handled via Docker volumes
* The system is cloud-ready and can connect to any managed database via env vars
* Swagger UI is used to demonstrate and test APIs

---

## 🔮 Future Enhancements

* Redis caching
* Rate limiting
* CI/CD pipeline
* Production deployment with managed database
* Microservice expansion

---

## 👨‍💻 Author

**Rohan Mujumdar**

This project was built to demonstrate practical backend engineering skills, clean design, and production-aligned development practices.
