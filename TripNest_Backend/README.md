# TripNest — React + Java Booking Project (Stage 1: Backend)

A minimal MakeMyTrip-inspired educational REST API. Java 17, Spring Boot 3.5.7, Spring Security/JWT, JPA and MySQL.

## Requirements
- Java 17 or later (`java -version`)
- Maven 3.9+ (`mvn -version`)
- MySQL 8.x running on port 3306
- Postman or another API client

## 1. Database
In MySQL Workbench run:
```sql
CREATE DATABASE IF NOT EXISTS tripnest_db;
```
Tables and sample data are created automatically when the backend starts. Do not reuse the Student Management System database.

## 2. Configuration
Edit `src/main/resources/application.properties`:
- `spring.datasource.url`: MySQL host, port, and database name
- `spring.datasource.username`: your MySQL username (default `root`)
- `spring.datasource.password`: your MySQL password (default empty)
- `app.jwt.secret`: use a **new random secret of at least 32 bytes** for anything outside local development
- `app.cors.origin`: React frontend URL (default `http://localhost:5173`)
- `app.seed.enabled`: set to `false` if you do not want sample data
- `app.seed.admin-password` / `app.seed.customer-password`: demo login passwords; change before sharing/deploying

Alternatively set environment variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `FRONTEND_URL`, `SEED_ENABLED`, `SEED_ADMIN_PASSWORD`, `SEED_CUSTOMER_PASSWORD`.

## 3. Run
From the `MMT_Backend` folder:
```bash
mvn spring-boot:run
```
Server: http://localhost:8080
Check: http://localhost:8080/api/health

If Maven is not installed, import the folder as a Maven project in IntelliJ IDEA or Eclipse and run `TripNestApplication.java`.

## 4. Demo logins (local development only)
- ADMIN: `admin@tripnest.com` / `Admin@123`
- CUSTOMER: `customer@tripnest.com` / `Customer@123`

These accounts are seeded only if their emails do not exist already. Changing demo passwords in properties later will **not** update existing accounts. Delete/recreate demo accounts in a disposable database if needed. Do not deploy with default demo credentials.

## 5. Endpoints
| Method | URL | Access | Purpose |
|---|---|---|---|
| GET | `/api/health` | Public | Health check |
| POST | `/api/auth/register` | Public | Register customer |
| POST | `/api/auth/login` | Public | Login |
| GET | `/api/flights?from=Delhi&to=Mumbai&date=2026-10-12` | Public | Search flights (date optional) |
| GET | `/api/flights/{id}` | Public | Flight details |
| GET | `/api/hotels?city=Goa` | Public | Search hotels |
| GET | `/api/hotels/{id}` | Public | Hotel details |
| POST | `/api/bookings` | Bearer token | Book flight or hotel |
| GET | `/api/bookings/my` | Bearer token | Own bookings |
| PUT | `/api/bookings/{id}/cancel` | Bearer token | Cancel own booking |
| GET | `/api/admin/bookings` | ADMIN | All bookings |
| POST/PUT/DELETE | `/api/admin/flights[/{id}]` | ADMIN | Manage flights |
| POST/PUT/DELETE | `/api/admin/hotels[/{id}]` | ADMIN | Manage hotels |

**Authentication:** In Postman add `Authorization: Bearer YOUR_TOKEN` to protected requests.

### Example: login
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{"email":"customer@tripnest.com","password":"Customer@123"}
```

### Example: book a flight
```http
POST http://localhost:8080/api/bookings
Authorization: Bearer YOUR_TOKEN
Content-Type: application/json

{"type":"FLIGHT","itemId":1,"quantity":2}
```

### Example: book a hotel
```http
POST http://localhost:8080/api/bookings
Authorization: Bearer YOUR_TOKEN
Content-Type: application/json

{"type":"HOTEL","itemId":1,"quantity":1,"checkIn":"2026-10-15","checkOut":"2026-10-17"}
```

### Example: register
```json
{"name":"New Customer","email":"new@example.com","password":"MyPassword123"}
```

## Project layout
- `entity/`: User, Flight, Hotel, Booking and enums
- `repository/`: Spring Data JPA interfaces
- `dto/`: Authentication and booking request DTOs
- `security/`: JWT generation and request filter
- `config/`: Security configuration and demo data
- `controller/`: REST endpoints and errors
- `service/`: Booking and cancellation logic

## Known limitations / scope
- Demonstration only: no real airline/hotel integrations, payments, refunds or emails.
- Hotel availability is a **simple shared room count**, not a date-aware room calendar. Overlapping stays are not checked.
- Admin deleting a flight/hotel with associated bookings may be rejected by database foreign keys.
- Availability updates are transactional but do not yet use pessimistic/optimistic locking; concurrent high-traffic booking protection is not implemented.
- Flight search uses exact city names (case insensitive) and exact dates.
- Prices are in INR; cancellation restores inventory but has no payment refund.
- Spring `ddl-auto=update` is for development, not production migrations.
- JWT in the React frontend should be stored/handled carefully; for production prefer secure cookie patterns and additional protections.

## Troubleshooting
- `Access denied for user`: verify MySQL username/password.
- `Communications link failure`: start MySQL and check DB_URL.
- `401 Unauthorized`: login and send Bearer token.
- `403 Forbidden`: only ADMIN can access `/api/admin/**`.
- `Port 8080 in use`: change `PORT` or stop the other backend.
- `CORS`: make sure React URL matches `app.cors.origin`.

## Stage 2
React + Vite + Tailwind UI will consume these APIs. The default frontend URL is http://localhost:5173.
