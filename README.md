# Petrol Management System

A small full-stack application for managing a fuel station: **fuel products** (types and price per liter), **storage tanks** (capacity and current level), and **sales** (dispensed volume with automatic stock deduction and totals). The backend is **Java (Spring Boot)**; the UI is **React (Vite)**.

## Architecture

| Layer | Stack |
|--------|--------|
| API | Spring Boot 3, Spring Web, Spring Data JPA, Bean Validation |
| Database | H2 (in-memory for development; H2 console at `/h2-console`) |
| UI | React 18, React Router, Vite 5 |

REST API base path: **`/api`**. CORS allows the Vite dev server (`http://localhost:5173`) when the UI talks to the API directly; in development, the UI can also use the **Vite proxy** so requests go to the same origin.

## Features

- **Dashboard**: today’s total sales amount, transaction count, tank and product counts.
- **Fuel products**: CRUD with unique names and price per liter (INR in the UI).
- **Tanks**: linked to a fuel product; capacity and current liters; manual level updates (e.g. after delivery).
- **Sales**: choose product and tank, enter liters; total uses the product’s current price; tank stock decreases.

Demo data (Petrol, Diesel, two tanks) is inserted on first startup when the database is empty.

## Prerequisites

- **Java 17+** and **Maven 3.6+** (or use your IDE’s Maven integration).
- **Node.js 18+** and npm for the frontend.

## Run the backend

```bash
cd backend
mvn spring-boot:run
```

API: **http://localhost:8080**

Useful endpoints:

- `GET /api/dashboard/stats`
- `GET/POST /api/fuel-products`, `PUT/DELETE /api/fuel-products/{id}`
- `GET/POST /api/tanks`, `PATCH /api/tanks/{id}/level`, `DELETE /api/tanks/{id}`
- `GET/POST /api/sales`

Optional: `app.cors.allowed-origins` in `backend/src/main/resources/application.properties` (comma-separated) if your UI runs on another origin.

## Run the frontend

```bash
cd frontend
npm install
npm run dev
```

App: **http://localhost:5173** — the dev server proxies `/api` to `http://localhost:8080` (see `frontend/vite.config.js`).

### Production build

```bash
cd frontend
npm run build
```

Serve the `frontend/dist` folder behind any static host. Set **`VITE_API_BASE`** to your API origin if the UI and API are on different hosts (e.g. `VITE_API_BASE=https://api.example.com`).

## Project layout

```
petrol-management-system/
├── backend/                 # Spring Boot application
│   ├── pom.xml
│   └── src/main/java/com/petrolmgmt/
│       ├── PetrolManagementApplication.java
│       ├── config/          # CORS, exception handling, seed data
│       ├── controller/      # REST controllers
│       ├── dto/
│       ├── model/           # JPA entities
│       ├── repository/
│       └── service/
└── frontend/                # React + Vite SPA
    ├── index.html
    ├── vite.config.js
    └── src/
        ├── api.js
        ├── App.jsx
        └── pages/
```

## License

Use and modify as needed for your project.
