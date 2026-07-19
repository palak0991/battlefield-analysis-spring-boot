# Simulated Battlefield Analysis Tool

A web-based Spring Boot application for configuring and managing simulated battlefield exercises — weapons, players, exercises, and systems — through a REST API backend and a tactical-themed dashboard frontend.

Developed during a DRDO internship (Institute for Systems Studies and Analyses, Metcalfe House, Delhi) as part of enterprise Java application development training.

## Current status

This project is under active, incremental development. The core CRUD backend and dashboard frontend are fully functional. Authentication, entity relationships, and further polish are in progress — see "Roadmap" below for what's planned but not yet built.

## Tech stack

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Data JPA** / **Hibernate 6.6** (ORM)
- **PostgreSQL 17**
- **Maven** (with Maven Wrapper — no local Maven install required)
- **Lombok** (reduces boilerplate getter/setter/constructor code)
- **Vanilla HTML/CSS/JavaScript** frontend (no framework)
- **Chart.js** for dashboard data visualization

> Note: `spring-boot-starter-thymeleaf` is present as a dependency but is not actively used — the frontend is plain HTML/CSS/JS served as static resources, not server-rendered Thymeleaf templates.

## Architecture

Layered architecture, consistent across every module:

```
Browser (HTML/CSS/JS)
    │  fetch() → REST calls
    ▼
Controller  (@RestController — handles HTTP requests/responses)
    ▼
Service     (interface + implementation — business logic)
    ▼
Repository  (Spring Data JPA — extends JpaRepository, no manual SQL)
    ▼
PostgreSQL Database
```

Each entity (Employee, Weapon, Player, Exercise, System configuration) follows this same five-file pattern: Entity → Repository → Service → ServiceImpl → Controller.

## Project structure

```
src/main/java/net/javaguides/springboot/
├── controller/     REST endpoints for each module
├── service/         Business logic interfaces
├── service/impl/    Business logic implementations
├── repository/      Spring Data JPA repositories
├── model/            JPA entities
└── exception/        Custom exception handling (404-style responses)

src/main/resources/
├── static/
│   ├── index.html    Dashboard UI (tactical HUD theme)
│   ├── style.css      Styling — glassmorphism, dark/light mode
│   ├── app.js          Config-driven table/modal/CRUD logic
│   └── images/         Background imagery
└── application.properties
```

## Features (currently working)

- **Dashboard** — live stat cards + Chart.js bar chart pulling real data from all four battlefield APIs
- **Employee management** — full CRUD (original template module)
- **Weapon Configuration** — full CRUD (type, range, total, current stock)
- **Player Configuration** — full CRUD (name, role, unit, login time)
- **Exercise Configuration** — full CRUD (name, location, date, commander)
- **System Configuration** — full CRUD (resource type, total, current allocation)
- Dark / Light "Ops Mode" toggle
- Toast notifications for create/update/delete actions
- Responsive layout (mobile breakpoint included)

Each module's frontend is built on a **reusable, config-driven table + modal system** — adding a new entity to the UI requires only a small config block, not new UI code from scratch.

## Not yet implemented

- User login / authentication (no Spring Security yet)
- Relationships between entities (e.g., Players assigned to Exercises — currently four independent tables)
- Role-based access control
- Automated tests beyond the default generated test class

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
mvnw.cmd spring-boot:run
```

**Linux/macOS:**
```
./mvnw spring-boot:run
```

The application runs at `http://localhost:8080`.

Requires: JDK 17+, PostgreSQL 16+ (with a database named `ems` created beforehand), Maven not required locally (wrapper included).

## Roadmap

- Add `@OneToMany` / `@ManyToOne` relationships between Player/Weapon/Exercise entities
- Login/authentication page
- Migrate the Employee module into the same config-driven frontend system used by the battlefield entities
- Additional automated tests

## Author

**Palak Kulshreshtha**
B.Tech Computer Science & Engineering, VIT Bhopal University
DRDO Internship — Institute for Systems Studies and Analyses (ISSA)