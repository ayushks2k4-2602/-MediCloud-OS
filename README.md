# Enterprise Multi-Tenant SaaS Platform

A production-ready enterprise SaaS web application built with **Java 21**, **Spring Boot 3.2**, **PostgreSQL**, **Redis**, **RabbitMQ**, **Stripe**, and **React (TypeScript + Vite + Tailwind CSS)**.

---

## System Features & Architecture

### Backend (Clean Architecture)
- **Java 21 & Spring Boot 3.2.3**
- **Multi-Tenant Engine**: Discriminator isolation via custom `TenantContext` & `TenantResolverFilter` with `X-Tenant-ID` header.
- **Spring Security & JWT**: Access tokens (HMAC-SHA256) + Refresh token rotation stored in database.
- **Role-Based Access Control (RBAC)**: `ROLE_SUPER_ADMIN`, `ROLE_ORG_OWNER`, `ROLE_ADMIN`, `ROLE_MANAGER`, `ROLE_EMPLOYEE`, `ROLE_CUSTOMER`, `ROLE_GUEST`.
- **Database & Migrations**: PostgreSQL primary database, Flyway SQL migrations (`V1__init_core_schema.sql`).
- **Subscription & Payment Engine**: Stripe Java SDK integration for subscriptions, checkout, and webhook handlers.
- **Async Messaging & Auditing**: RabbitMQ topic exchanges for async audit logs & email notifications.
- **OpenAPI 3.0 Documentation**: Swagger UI at `/swagger-ui.html`.

### Frontend (React Dashboard)
- **Vite + TypeScript + Tailwind CSS**
- **Responsive Multi-Tenant Dashboards**: Overview metrics, Organization profile & branding, Team & RBAC management, Subscriptions matrix, CRM Deals board, Kanban project tasks, and Super Admin analytics.

---

## Directory Structure

```
D:\Project1\
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│       ├── main/
│       │   ├── java/com/saas/platform/
│       │   │   ├── SaaSApplication.java
│       │   │   ├── common/ (Entities, DTOs, Exception Handlers)
│       │   │   ├── infrastructure/ (Audit, Security Config, Redis, RabbitMQ, Swagger)
│       │   │   ├── tenant/ (Tenant Context, Resolver Filter, Repositories, Controllers)
│       │   │   ├── user/ (RBAC User Entities, Services, Controllers)
│       │   │   ├── auth/ (JWT Provider, Login, Register, Token Rotation)
│       │   │   ├── billing/ (Stripe Subscriptions & Invoices)
│       │   │   └── project/ (CRM Deals & Kanban Tasks)
│       │   └── resources/
│       │       ├── application.yml
│       │       ├── application-test.yml
│       │       └── db/migration/V1__init_core_schema.sql
└── frontend/
    ├── package.json
    ├── vite.config.ts
    ├── index.html
    └── src/
        ├── index.css
        ├── main.tsx
        └── App.tsx
```

---

## How to Run

### 1. Run Backend Server
```bash
cd D:\Project1\backend
mvn spring-boot:run "-Dspring-boot.run.profiles=test"
```
Backend will start on `http://localhost:8080` (Swagger UI: `http://localhost:8080/swagger-ui.html`).

### 2. Run Docker Stack (Postgres + Redis + RabbitMQ + App)
```bash
cd D:\Project1
docker-compose up --build -d
```
