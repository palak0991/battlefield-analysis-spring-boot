# Simulated Battlefield Analysis Tool

A web-based Spring Boot application for configuring and managing simulated battlefield exercises — weapons, players, exercises, and systems — through a REST API backend and a tactical-themed dashboard frontend.

Developed during a DRDO internship (Institute for Systems Studies and Analyses, Metcalfe House, Delhi) as part of enterprise Java application development training.

## Current status

Core CRUD backend, relational data model, Spring Security authentication, and the dashboard frontend are all fully functional. Role-based access control and automated tests are still on the roadmap — see below.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.4.13 |
| ORM | Spring Data JPA / Hibernate 6 |
| Security | Spring Security 6 (form login, BCrypt) |
| Validation | Spring Boot Starter Validation |
| Database | PostgreSQL 17 |
| Build | Maven (wrapper included — no local install needed) |
| Boilerplate reduction | Lombok 1.18.38 |
| Frontend | Vanilla HTML / CSS / JavaScript |
| Charts | Chart.js |

> `spring-boot-starter-thymeleaf` is present as a dependency but is not actively used — the frontend is plain HTML/CSS/JS served as static resources, not server-rendered Thymeleaf templates.

## Architecture

Layered architecture, consistent across every module:

```
Browser (HTML / CSS / JS)
    │  fetch() → REST calls
    ▼
SecurityFilterChain  (Spring Security — protects all routes, custom login page)
    ▼
Controller  (@RestController — handles HTTP requests/responses)
    ▼
Service     (interface + implementation — business logic)
    ▼
Repository  (Spring Data JPA — extends JpaRepository, no manual SQL)
    ▼
PostgreSQL Database
```

Each entity follows the same five-file pattern:
**Entity → Repository → Service interface → ServiceImpl → Controller**

## Entity relationships

The data model now uses JPA associations instead of four independent tables:

```
ExerciseConfiguration (1)
    ├──< PlayerConfiguration (many)   [@ManyToOne exercise_id FK]
    │       └──< WeaponConfiguration (many)  [@ManyToOne player_id FK]
    └──< SystemConfiguration (many)   [@ManyToOne exercise_id FK]
```

| Entity | Key fields |
|---|---|
| `ExerciseConfiguration` | name, location, date, commander |
| `PlayerConfiguration` | playerName, role, unit, totalLoginTime, exercise (FK) |
| `WeaponConfiguration` | weaponType, range, total, current, player (FK) |
| `SystemConfiguration` | resourceType, total, current, exercise (FK) |
| `Employee` | firstName, lastName, emailId |

## Project structure

```
src/main/java/net/javaguides/springboot/
├── config/          SecurityConfig.java — Spring Security filter chain & in-memory users
├── controller/      REST endpoints for each module
├── service/         Business logic interfaces
├── service/impl/    Business logic implementations
├── repository/      Spring Data JPA repositories
├── model/           JPA entities (with JPA relationships)
└── exception/       Custom exception handling (404-style responses)

src/main/resources/
├── static/
│   ├── index.html    Dashboard UI (tactical HUD theme)
│   ├── login.html    Custom login page (styled to match dashboard)
│   ├── style.css     Glassmorphism / dark-light mode styling
│   ├── app.js        Config-driven table / modal / CRUD logic
│   └── images/       Background imagery
└── application.properties
```

## Features (currently working)

- **Authentication** — Spring Security form login with a custom `login.html` page; BCrypt password hashing; logout support
- **Protected routes** — all pages except `login.html`, `style.css`, `app.js`, and `/images/**` require authentication
- **Dashboard** — live stat cards + Chart.js bar chart pulling real data from all four battlefield APIs
- **Employee management** — full CRUD (original template module)
- **Weapon Configuration** — full CRUD (type, range, total, current stock); linked to a Player via FK
- **Player Configuration** — full CRUD (name, role, unit, login time); linked to an Exercise via FK; owns a list of Weapons
- **Exercise Configuration** — full CRUD (name, location, date, commander); owns lists of Players and Systems
- **System Configuration** — full CRUD (resource type, total, current allocation); linked to an Exercise via FK
- Dark / Light "Ops Mode" toggle
- Toast notifications for create / update / delete actions
- Responsive layout (mobile breakpoint included)
- Config-driven frontend table + modal system — adding a new entity to the UI requires only a small config block

## Authentication (default credentials)

The application ships with a single in-memory user for development:

| Username | Password | Role |
|---|---|---|
| `admin` | `battlefield123` | ADMIN |

> **Change these before any deployment.** The credentials are set in `SecurityConfig.java` and encoded with BCrypt at startup.

## Not yet implemented

- Role-based access control (RBAC) — multiple user roles with different permissions
- API-level relationship endpoints (e.g., assign a Player to an Exercise through the UI)
- Automated tests beyond the default generated test class
- Database-backed user management (currently in-memory only)

## Database configuration

Set your own local PostgreSQL credentials via environment variables (do not hardcode passwords in `application.properties`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ems?ssl=false
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

`DB_PASSWORD` must be set as an environment variable (e.g., in your IDE's run configuration) — there is no default, intentionally, to avoid committing secrets.

## Running the application

```bash
git clone https://github.com/palak0991/battlefield-analysis-spring-boot.git
cd battlefield-analysis-spring-boot
```

Set the `DB_PASSWORD` environment variable, then:

**Windows:**
```
set DB_PASSWORD=your_password
mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
export DB_PASSWORD=your_password
./mvnw spring-boot:run
```

The application runs at `http://localhost:8080`. You will be redirected to the login page automatically.

**Prerequisites:** JDK 17+, PostgreSQL 16+ with a database named `ems` created beforehand. Maven is not required locally (wrapper included).

## Roadmap

- Role-based access control (viewer vs. admin)
- UI support for entity relationships (e.g., assign Players to Exercises, Weapons to Players)
- Migrate the Employee module into the config-driven frontend system used by the battlefield entities
- Database-backed user management (replace in-memory users)
- Automated integration and unit tests

## Author

**Palak Kulshreshtha**  
B.Tech Computer Science & Engineering, VIT Bhopal University  
DRDO Internship — Institute for Systems Studies and Analyses (ISSA), Metcalfe House, Delhi