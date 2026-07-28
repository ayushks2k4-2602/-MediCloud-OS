# MediCloud OS - Multi-Tenant Hospital Management SaaS System

Production-ready healthcare operating system built with **Java 21**, **Spring Boot 3.2**, **PostgreSQL**, **Redis**, **RabbitMQ**, **Stripe**, and **React 18 (TypeScript + Vite + Tailwind CSS)**.

---

## Production Release Version: `v2.0.0`

- **Hospital Organization**: `🏥 Ayush Health Network`
- **Chief Medical Officer**: `Dr. Vishnu Tiwari (VT)`
- **GitHub Repository**: [https://github.com/ayushks2k4-2602/-MediCloud-OS.git](https://github.com/ayushks2k4-2602/-MediCloud-OS.git)

---

## Clinical & Administrative Modules

1. **Patient Management**: Registration (`PAT-XXXXX`), demographic details, blood group filtering, emergency contacts, insurance policies.
2. **Doctor Directory**: 21 pre-seeded specializations (*Cardiology*, *Neurology*, *Orthopedics*, etc.), license number verification, experience years, consultation fees.
3. **Shift Scheduling**: Morning, Afternoon, Evening, Night, and Custom shift management.
4. **Electronic Health Records (EHR)**: SOAP clinical notes, vitals recording (BP, HR, Temp, Weight), allergies, immunizations, surgery history.
5. **Prescription Generator**: Electronic prescription issuing with digital signature preview and print/PDF export capabilities.
6. **ICU & Ward Bed Matrix**: Live bed allocation across ICU, General, Private, and Deluxe suites.
7. **Hospital Billing & Insurance**: Itemized billing invoices, Stripe payment integration, insurance claim tracking.
8. **Pharmacy Inventory**: Medicine catalog, batch numbers, stock quantity, expiry dates.
9. **Telehealth Video Consultation**: Video room generator (`ROOM-XXXX`), doctor-patient access join tokens.
10. **Emergency Room & Ambulance Dispatch**: ER Triage levels (`CRITICAL`, `SEVERE`), ambulance fleet dispatch.

---

## Installation & Local Execution

### 1. Run Backend REST API Server
```powershell
cd D:\Project1\backend
mvn spring-boot:run "-Dspring-boot.run.profiles=test"
```
- **Base API**: [http://localhost:8082](http://localhost:8082)
- **Interactive Swagger UI**: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

### 2. Run React Web Portal
```powershell
cd D:\Project1\frontend
npm install
npm run dev
```
- **Portal Link**: [http://localhost:3000](http://localhost:3000)

---

## Production Docker Deployment

```powershell
cd D:\Project1
docker-compose up --build -d
```
