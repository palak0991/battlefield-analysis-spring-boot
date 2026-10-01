# Architecture — Battlefield Resource Management System

## Request Lifecycle

```
User (Browser)
    │
    │  HTTP Request (GET/POST/PUT/DELETE)
    ▼
Spring Security FilterChain
    │  Checks: Is the user authenticated?
    │  If NOT → redirect to /login.html
    │  If YES → pass request to the next layer
    ▼
DispatcherServlet (Spring MVC)
    │  Routes the request to the right Controller
    ▼
@RestController (Controller Layer)
    │  Validates request body (@Valid)
    │  Calls the Service layer
    │  Returns ResponseEntity with JSON + HTTP status
    ▼
@Service (Service Layer)
    │  Contains business logic
    │  Calls the Repository
    │  Throws ResourceNotFoundException if needed
    ▼
@Repository (Repository Layer / Spring Data JPA)
    │  Extends JpaRepository — provides save(), findAll(), findById(), delete()
    │  No SQL written manually
    ▼
JPA / Hibernate (ORM)
    │  Converts Java objects ↔ database rows
    │  Manages relationships (FK columns)
    ▼
PostgreSQL Database
    │  Stores data in tables
    │  Hibernate generates/updates schema automatically (ddl-auto=update)
    ▼
Response flows back up the same chain
```

---

## Layers Explained

| Layer | Technology | Responsibility |
|---|---|---|
| Frontend | HTML, CSS, Vanilla JS, Chart.js | User interface, API calls via `fetch()` |
| Security | Spring Security | Authentication, session management, route protection |
| Controller | `@RestController` | HTTP request/response handling, input validation |
| Service | `@Service` | Business logic, exception throwing |
| Repository | `JpaRepository` | Database queries via Spring Data JPA |
| ORM | Hibernate | Java objects ↔ database tables |
| Database | PostgreSQL 17 | Persistent data storage |

---

## Package Structure

```
net.javaguides.springboot/
├── config/
│   └── SecurityConfig.java        Spring Security setup
├── controller/
│   ├── EmployeeController.java     /api/employees
│   ├── WeaponConfigurationController.java  /api/weapons
│   ├── PlayerConfigurationController.java  /api/players
│   ├── ExerciseConfigurationController.java /api/exercises
│   └── SystemConfigurationController.java  /api/systems
├── exception/
│   ├── ResourceNotFoundException.java  Custom 404 exception
│   └── GlobalExceptionHandler.java     @RestControllerAdvice — catches all exceptions
├── model/
│   ├── Employee.java
│   ├── WeaponConfiguration.java
│   ├── PlayerConfiguration.java
│   ├── ExerciseConfiguration.java
│   └── SystemConfiguration.java
├── repository/
│   ├── EmployeeRepository.java         extends JpaRepository<Employee, Long>
│   ├── WeaponConfigurationRepository.java
│   ├── PlayerConfigurationRepository.java
│   ├── ExerciseConfigurationRepository.java
│   └── SystemConfigurationRepository.java
└── service/
    ├── EmployeeService.java            (interface)
    ├── WeaponConfigurationService.java
    ├── PlayerConfigurationService.java
    ├── ExerciseConfigurationService.java
    ├── SystemConfigurationService.java
    └── impl/
        ├── EmployeeServiceImpl.java     (implementation)
        ├── WeaponConfigurationServiceImpl.java
        ├── PlayerConfigurationServiceImpl.java
        ├── ExerciseConfigurationServiceImpl.java
        └── SystemConfigurationServiceImpl.java
```

---

## Entity Relationships (ER Diagram)

```
┌─────────────────────────────┐
│     ExerciseConfiguration   │
│  PK: id                     │
│  name, location, date,      │
│  commander                  │
└────────┬──────────┬─────────┘
         │ 1        │ 1
         │          │
         │ *        │ *
┌────────▼────────┐ ┌─────────────────────┐
│  PlayerConfig   │ │  SystemConfiguration │
│  PK: id         │ │  PK: id              │
│  playerName,    │ │  resourceType,       │
│  role, unit,    │ │  total, current      │
│  totalLoginTime │ │  FK: exercise_id     │
│  FK: exercise_id│ └─────────────────────┘
└────────┬────────┘
         │ 1
         │
         │ *
┌────────▼────────┐
│ WeaponConfig    │
│ PK: id          │
│ weaponType,     │
│ range, total,   │
│ current         │
│ FK: player_id   │
└─────────────────┘

┌─────────────────┐
│    Employee     │   (independent module, no relations)
│  PK: id         │
│  firstName,     │
│  lastName,      │
│  email (unique) │
└─────────────────┘
```

---

## Cascade Behavior

| Relationship | Cascade | Explanation |
|---|---|---|
| Exercise → Players | ALL | Deleting an exercise removes its players (they belong to the exercise) |
| Exercise → Systems | ALL | Deleting an exercise removes its system allocations |
| Player → Weapons | ALL | Deleting a player removes their weapons |

> **Note:** CascadeType.ALL is intentional here — players and system resources in this simulation are tightly coupled to their exercise. If this were a production military system, cascade behavior would need more careful review.

---

## Security Architecture

```
HTTP Request
    │
Spring Security Filter (before any Controller)
    │
    ├─ URL in public list? (/login.html, /style.css, /app.js, /images/**)
    │     └─ YES → Allow through
    │
    └─ NO → Is there a valid session?
          ├─ YES → Allow through to Controller
          └─ NO  → Redirect to /login.html
```

**Authentication:** In-memory single user (admin). Credentials from environment variables.  
**Password storage:** BCrypt-encoded (never stored in plain text).  
**Session:** Standard HTTP session (JSESSIONID cookie).  
**CSRF:** Disabled — this is a REST API consumed by a same-origin SPA.

---

## Frontend Architecture

```
index.html
    │
    ├─ style.css      (design tokens, layout, components)
    ├─ Chart.js CDN   (bar chart for dashboard)
    └─ app.js
           │
           ├─ entityConfigs{}  — config-driven module definitions
           ├─ loadDashboardStats()  — fetches all 5 counts
           ├─ loadTable(entityKey)  — renders table from API
           ├─ filterTable(entityKey, value)  — client-side search
           ├─ openModal(entityKey, record)   — builds form dynamically
           ├─ submitForm(entityKey)          — POST or PUT to API
           └─ deleteRecord(entityKey, id)   — DELETE with confirmation
```

The frontend uses a **config-driven** pattern — each module (weapons, players, etc.) is described by a small JavaScript config block. The same generic table/modal/CRUD functions handle all modules. Adding a new module requires only a new config entry.
