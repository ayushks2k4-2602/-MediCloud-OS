# 🏥 MediCloud OS - Multi-Tenant Healthcare SaaS Platform

MediCloud OS is an enterprise-grade, multi-tenant hospital management operating system designed for **Ayush Health Network** (Chief Medical Officer: **Dr. Vishnu Tiwari, VT**).

Built using **Java 21**, **Spring Boot 3.2**, **PostgreSQL**, **Redis**, **RabbitMQ**, **Stripe**, and **React 18 (TypeScript + Vite + Tailwind CSS)**.

---

## 🔗 Official Repository & Release Baseline

- **GitHub Repository**: [https://github.com/ayushks2k4-2602/-MediCloud-OS.git](https://github.com/ayushks2k4-2602/-MediCloud-OS.git)
- **Current Milestone Tag**: **[`v3.0.0-m4`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v3.0.0-m4)** (Milestone 4 Complete - Pharmacy Inventory & Fulfillment)
- **Production Baseline Tag**: [`v2.0.0`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v2.0.0)
- **Phase 1 Baseline Tag**: [`v1.0.0-phase1`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v1.0.0-phase1)

---

## 🛠️ Technology Stack & Architecture

### Backend Stack
- **Language & Runtime**: Java 21 (JDK 21 LTS)
- **Framework**: Spring Boot 3.2.3, Spring Data JPA, Spring Security 6
- **ORM & Database Engine**: Hibernate 6.4, PostgreSQL 16 (H2 in-memory mode for rapid local integration testing)
- **Database Migrations**: Flyway DB Schema Migrations (`V1__init_core_schema.sql` to `V9__pharmacy_inventory_fulfillment.sql`)
- **Security & Multi-Tenancy**: JJWT (HMAC-SHA256), `TenantResolverFilter` for `X-Tenant-ID` header resolution with `ThreadLocal<UUID>` isolation
- **Caching & Messaging**: Redis, RabbitMQ
- **Payment Abstraction**: Stripe Payment Gateway Abstraction Layer
- **API Documentation**: SpringDoc OpenAPI 3 / Interactive Swagger UI

- **Framework**: React 18, TypeScript 5.2, Vite 5.4 (12 lazy-loaded page modules)
- **Styling & Layout**: Vanilla CSS Design System (`index.css`), CSS Custom Properties, Dark Mode, Minimal Teal Medical Theme, Mobile-First Responsive Breakpoints
- **Architecture**: Modular Component Architecture (`components/`, `pages/`, `hooks/`, `api.ts`, `types.ts`, `constants.ts`)
- **API Client**: Centralized Fetch Client with Automatic JWT (`Authorization: Bearer <token>`), Multi-Tenant Header (`X-Tenant-ID`), Timeout, Retry & 401 Expiration Handling

---

## 📂 Project Directory Structure (`D:\Project1`)

```
D:\Project1\
├── README.md                              <-- Complete Architecture & Step-by-Step Procedure
├── PRODUCTION_AUDIT.md                    <-- Production Readiness Audit Document
├── RELEASE_NOTES_Phase2.md                <-- Phase 2 Release Notes
├── FINAL_PROJECT_REPORT.md                <-- Comprehensive Final System Report
├── docker-compose.yml                     <-- Docker Container Orchestration
├── backend/
│   ├── pom.xml                            <-- Maven Dependencies & Build Configuration
│   ├── Dockerfile
│   └── src/
│       ├── main/
│       │   ├── java/com/saas/platform/
│       │   │   ├── SaaSApplication.java
│       │   │   ├── common/                <-- BaseEntity, ApiResponse, PageResponse, GlobalExceptionHandler
│       │   │   ├── infrastructure/        <-- AuditLogger, RedisConfig, RabbitMQConfig, OpenApiConfig
│       │   │   ├── tenant/                <-- TenantContext, TenantResolverFilter, TenantController
│       │   │   ├── user/                  <-- User, Role, Permission entities & Security Controllers
│       │   │   ├── auth/                  <-- JwtTokenProvider, SecurityConfig, AuthController
│       │   │   └── hospital/              <-- Patients, Doctors, Specializations, Shifts, Appointments,
│       │   │                              │   Waiting List, Reminders, Billing, Payments, Insurance Claims,
│       │   │                              │   Laboratory Information System (LIS), Pharmacy, EHR APIs
│       │   └── resources/
│       │       ├── application.yml        <-- Main Configuration
│       │       ├── application-test.yml   <-- Test Environment Config (Port 8082)
│       │       └── db/migration/
│       │           ├── V1__init_core_schema.sql
│       │           ├── V2__init_hospital_schema.sql
│       │           ├── V3__init_pharmacy_lab_schema.sql
│       │           ├── V4__init_telehealth_emergency_schema.sql
│       │           ├── V5__doctor_specialization_shifts_ehr.sql
│       │           ├── V6__appointment_scheduling_waiting_list.sql
│       │           ├── V7__billing_insurance_claims.sql
│       │           ├── V8__laboratory_information_system.sql
│       │           └── V9__pharmacy_inventory_fulfillment.sql
└── frontend/
    ├── package.json
    ├── vite.config.ts                     <-- Proxy Target http://localhost:8082, Port 3000
    ├── index.html
    └── src/
        ├── index.css                      <-- Core Healthcare Design System
        ├── main.tsx
        └── App.tsx                        <-- Ayush Health Network React Portal
```

---

## 📖 Complete Feature Modules & Capabilities

### 1. Patient Directory & Registration
- **Auto-Generated Code**: Auto-generates unique codes (`PAT-XXXXX`).
- **Demographic Record**: Captures name, email, phone, DOB, gender, blood group (`A+`, `O+`, `B+`, `AB+`), emergency contacts, insurance details.
- **Directory Search & Filter**: Live search by name, code, or phone, blood group dropdown filtering, server-side pagination (`PageResponse<PatientDto>`).
- **Endpoints**: `POST /api/v1/hospital/patients`, `GET /api/v1/hospital/patients`, `GET /api/v1/hospital/patients/{id}`.

### 2. Doctor Directory & Specialization Catalog
- **Specializations Pre-Seeded**: 21 standard medical specializations (*Cardiology*, *Neurology*, *Orthopedics*, *General Medicine*, *Dermatology*, *ENT*, *Radiology*, *Psychiatry*, *Pediatrics*, *Nephrology*, *Oncology*, *Urology*, *Emergency Medicine*, *Gynecology*, *Anesthesiology*, *Pulmonology*, *Pathology*, *Ophthalmology*, *Dentistry*, *Gastroenterology*).
- **Doctor Profiles**: Qualification, experience years, contact number, license number, consultation fee, employment status (`FULL_TIME`, `PART_TIME`, `VISITING`, `ON_CALL`), availability toggle.
- **Endpoints**: `POST /api/v1/hospital/doctors`, `PUT /api/v1/hospital/doctors/{id}`, `DELETE /api/v1/hospital/doctors/{id}`, `GET /api/v1/hospital/doctors`, `GET /api/v1/hospital/specializations`.

### 3. Doctor Availability & Work Shifts
- **Weekly Schedule**: Configurable availability by day (`MONDAY` to `SUNDAY`) with start time, end time, and slot duration (30 mins).
- **Shifts**: Morning (`08:00 AM - 04:00 PM`), Afternoon (`04:00 PM - 12:00 AM`), Night (`12:00 AM - 08:00 AM`), and Custom shifts.
- **Endpoints**: `POST /api/v1/hospital/doctors/availability`, `GET /api/v1/hospital/doctors/{doctorId}/availability`, `POST /api/v1/hospital/shifts`, `GET /api/v1/hospital/shifts`.

### 4. Appointment Booking, Rescheduling & Waiting Queue
- **Status Lifecycle**: `SCHEDULED`, `CONFIRMED`, `COMPLETED`, `CANCELLED`, `NO_SHOW`.
- **Rescheduling Engine**: Links old cancelled appointment to new rescheduled appointment (`rescheduledFromId`).
- **Waiting Queue**: Patient waiting list for fully booked doctor dates with preferred time slots and priority notes.
- **Multi-Channel Reminders**: Provider-independent Email & SMS notification dispatch with delivery audit logs (`reminder_logs`).
- **Endpoints**: `POST /api/v1/hospital/appointments`, `PUT /api/v1/hospital/appointments/{id}/status`, `POST /api/v1/hospital/appointments/reschedule/{id}`, `POST /api/v1/hospital/appointments/waiting-list`, `GET /api/v1/hospital/appointments/waiting-list`, `POST /api/v1/hospital/appointments/{id}/remind?channel=SMS`.

### 5. Billing, Invoicing & Stripe Payment Abstraction
- **Invoice Generator**: Auto-generated invoice numbers (`INV-XXXXXX`), itemized line items, subtotal calculation, 10% automated tax calculation, discount deduction, total amount calculation.
- **Stripe Payment Gateway**: Abstraction layer supporting `CASH`, `CREDIT_CARD`, `STRIPE`, `INSURANCE`, `BANK_TRANSFER` payments, automatically updating parent invoice status to `PAID`.
- **Endpoints**: `POST /api/v1/hospital/invoices`, `GET /api/v1/hospital/invoices`, `POST /api/v1/hospital/payments`, `GET /api/v1/hospital/payments`.

### 6. Insurance Provider Catalog & Claims Workflow
- **Insurance Providers**: Catalog of registered insurance providers.
- **Claim Lifecycle**: Auto-generated claim numbers (`CLM-XXXXXX`), claim submission against invoices, status workflow (`SUBMITTED`, `UNDER_REVIEW`, `APPROVED`, `REJECTED`, `PAID`).
- **Endpoints**: `POST /api/v1/hospital/insurance/providers`, `GET /api/v1/hospital/insurance/providers`, `POST /api/v1/hospital/insurance/claims`, `GET /api/v1/hospital/insurance/claims`.

### 7. Laboratory Information System (LIS)
- **Lab Test Catalog**: Master catalog across categories (*Hematology*, *Biochemistry*, *Microbiology*, *Pathology*, *Serology*) with prices, normal reference ranges, and units.
- **Lab Orders**: Auto-generated order numbers (`LAB-XXXXXX`), order status lifecycle (`ORDERED`, `SAMPLE_COLLECTED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`).
- **Specimen Sample Tracking**: Auto-generated barcode sample code (`SMP-XXXXXX`), specimen type (Blood, Urine, Swab, Tissue), sample collection status (`COLLECTED`, `RECEIVED`, `IN_TESTING`, `REJECTED`).
- **Pathologist Result Entry**: Diagnostic value entry, critical flag alerts (`is_critical`), pathologist notes, and approval timestamp (`approvedAt`).
- **Endpoints**: `POST /api/v1/hospital/lab/tests`, `GET /api/v1/hospital/lab/tests`, `POST /api/v1/hospital/lab/orders`, `GET /api/v1/hospital/lab/orders`, `POST /api/v1/hospital/lab/samples`, `GET /api/v1/hospital/lab/samples`, `POST /api/v1/hospital/lab/results`, `GET /api/v1/hospital/lab/results`.

### 8. Pharmacy Inventory & Prescription Fulfillment
- **Medicine Inventory**: Medicine catalog tracking generic names, batch numbers (`BAT-XXXXX`), stock quantity, unit prices, expiry dates, manufacturers.
- **Pharmacy Suppliers**: Supplier directory and purchase order management (`PO-XXXXXX`).
- **Prescription Fulfillment & Auto-Deduction**: Auto-generated fulfillment numbers (`FUL-XXXXXX`), unit price resolution, total billing, and **automatic inventory stock deduction** (e.g. 100 → 90 units) with audit logging (`stock_movements`).
- **Endpoints**: `POST /api/v1/hospital/medicines`, `GET /api/v1/hospital/medicines`, `POST /api/v1/hospital/pharmacy/suppliers`, `GET /api/v1/hospital/pharmacy/suppliers`, `POST /api/v1/hospital/pharmacy/purchase-orders`, `GET /api/v1/hospital/pharmacy/purchase-orders`, `POST /api/v1/hospital/pharmacy/fulfillments`, `GET /api/v1/hospital/pharmacy/fulfillments`, `POST /api/v1/hospital/pharmacy/stock-movements`.

### 9. Advanced Electronic Medical Records (EHR)
- **SOAP Clinical Notes**: Subjective complaints, Objective vitals JSON (BP, HR, Temp, Weight), Assessment, Treatment Plan.
- **Medical History**: Allergy history, immunizations, surgery history, family history, PDF attachments.
- **Endpoints**: `POST /api/v1/hospital/ehr/record`, `GET /api/v1/hospital/ehr/patient/{patientId}/records`.

---

## ⚡ Step-by-Step Procedure for Local Installation & Execution

### Prerequisites
- **Java**: JDK 21 installed and added to `PATH`
- **Node.js**: v18+ or v20+ with `npm`
- **Git**: Git version control installed

---

### Step 1: Clone Repository
```powershell
git clone https://github.com/ayushks2k4-2602/-MediCloud-OS.git D:\Project1
cd D:\Project1
```

---

### Step 2: Build & Start Backend Application
Navigate to `backend/` directory and compile with Maven:
```powershell
cd D:\Project1\backend

# Run Maven Test-Compile to verify 142 Java source files compile clean
mvn test-compile

# Start Backend Server on Port 8082
mvn spring-boot:run "-Dspring-boot.run.profiles=test"
```

#### Verification:
- **Base REST Engine**: Open [http://localhost:8082/](http://localhost:8082/) in browser. Output:
  `{"success":true,"message":"MediCloud OS REST API Engine is running"}`
- **Interactive Swagger UI**: Open [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

---

### Step 3: Install & Start React Frontend
Open a new terminal, navigate to `frontend/` directory, install npm dependencies, and start Vite dev server:
```powershell
cd D:\Project1\frontend

# Install frontend dependencies
npm install

# Test TypeScript compilation and production build
npm run build

# Start Vite Development Server on Port 3000
npm run dev
```

#### Verification:
- Open [http://localhost:3000](http://localhost:3000) in browser to interact with **Ayush Health Network** portal.

---

## 🐳 Step-by-Step Procedure for Docker Deployment

To run the complete production containerized stack (PostgreSQL, Redis, RabbitMQ, Spring Boot, React):

```powershell
cd D:\Project1

# Build and start all containerized services in background
docker-compose up --build -d

# Check running container statuses
docker-compose ps
```

---

## 🧪 Quality Gate Verification Commands

To perform full automated verification of the entire project:

```powershell
# 1. Run Backend Quality Gate (Compiles 142 Java classes, runs Flyway migrations V1-V9, verifies JPA mappings)
cd D:\Project1\backend
mvn clean verify

# 2. Run Frontend Quality Gate (Compiles TypeScript, checks React modules, generates dist bundle)
cd D:\Project1\frontend
npm run build
```

---

## 🛡️ Security, Multi-Tenancy & Tenant Isolation Rules

1. **Header-Based Tenant Isolation**:
   Every REST request includes the `X-Tenant-ID` header (e.g. `00000000-0000-0000-0000-000000000001`). `TenantResolverFilter` extracts this header and sets it in `TenantContext` (`ThreadLocal<UUID>`).
2. **Context Cleanup Safety**:
   `TenantResolverFilter` clears the `TenantContext` in a `finally` block after every request, preventing cross-tenant thread leaks.
3. **Database Security**:
   All JPQL & SQL queries filter strictly by `tenant_id`.

---

## 📊 Summary of Git Tags & Release History

| Tag Name | Description | Status |
|---|---|---|
| **[`v1.0.0-phase1`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v1.0.0-phase1)** | Phase 1 Baseline - Audit & Fixes for Patient Directory, Doctors, EHR | ✅ **PUSHED** |
| **[`v2.0.0`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v2.0.0)** | Production Baseline Release | ✅ **PUSHED** |
| **[`v3.0.0-alpha`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v3.0.0-alpha)** | Phase 3 Alpha Architecture Baseline | ✅ **PUSHED** |
| **[`v3.0.0-m1`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v3.0.0-m1)** | Milestone 1 - Appointment & Scheduling System, Waiting List, Reminders | ✅ **PUSHED** |
| **[`v3.0.0-m2`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v3.0.0-m2)** | Milestone 2 - Billing, Invoicing, Stripe Integration, Insurance Claims | ✅ **PUSHED** |
| **[`v3.0.0-m3`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v3.0.0-m3)** | Milestone 3 - Laboratory Information System (LIS) | ✅ **PUSHED** |
| **[`v3.0.0-m4`](https://github.com/ayushks2k4-2602/-MediCloud-OS/releases/tag/v3.0.0-m4)** | Milestone 4 - Pharmacy Inventory & Prescription Fulfillment | ✅ **PUSHED** |

---

## 👨‍⚕️ Hospital Administrative Branding

- **Organization**: `🏥 Ayush Health Network`
- **Chief Medical Officer**: `Dr. Vishnu Tiwari (VT)`
- **CMO Medical Email**: `dr.vishnu@ayushhealth.com`
- **System Platform**: `MediCloud OS Multi-Tenant Healthcare SaaS System`
