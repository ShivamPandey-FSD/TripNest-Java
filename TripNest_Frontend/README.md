# TripNest — Stage 2 React Frontend

## Requirements
Node.js 20.19+ or 22.12+, npm, and the TripNest Spring Boot backend running on port 8080.

## Start
1. Extract ZIP.
2. Open `TripNest_Frontend` in VS Code.
3. Run `npm install`.
4. Run `npm run dev`.
5. Open `http://localhost:5173`.

## Backend connection
The frontend defaults to `http://localhost:8080/api`.
If your backend uses another address, create `.env` in the frontend root containing:
`VITE_API_URL=http://localhost:8080/api`
Restart Vite after changing `.env`.
The backend's `app.cors.origin` must match your frontend origin (default `http://localhost:5173`).

## Demo accounts (seeded by backend)
- Admin: `admin@tripnest.com` / `Admin@123`
- Customer: `customer@tripnest.com` / `Customer@123`
Only use demo credentials locally. Change them before deployment.

## Screens
Home, Login/Register, Flights, Hotels, Booking, My Bookings, Admin Dashboard.

## Notes
- All bookings use the existing Spring Boot REST API; no mocked booking data.
- Customer JWT is kept in **sessionStorage** for this educational demo, not production-safe token storage.
- No real payments, email, refunds or date-aware hotel inventory.
- If a flight is in the past, the backend rejects booking. New seed data is generated relative to the backend's first startup date and will not refresh automatically.
- Admin deletion may fail when a record has existing bookings, due to foreign key constraints.
- Admin can view all bookings, add/edit/delete flights and hotels.
