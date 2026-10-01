# Battlefield Resource Management System

A web-based resource management dashboard built during a DRDO internship.
Manages battlefield resources including employees, weapons, players, exercises, and system allocations.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.4 |
| Web Framework | Spring MVC (REST APIs) |
| ORM | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Security | Spring Security 6 (form login, BCrypt, in-memory auth) |
| Build Tool | Maven |
| Frontend | HTML, CSS, Vanilla JavaScript, Chart.js |
| Testing | JUnit 5, Mockito, MockMvc, H2 (test only) |

---

## Features

- **Secure Login** — form-based authentication; credentials from environment variables
- **Dashboard** — live counts for all 5 modules + Chart.js resource allocation chart
- **Full CRUD** — Create, Read, Update, Delete for Employees, Weapons, Players, Exercises, and Systems
- **Relationship Management** — weapons assigned to players; players and systems assigned to exercises
- **Validation** — server-side Bean Validation with consistent 400 error responses
- **Global Error Handling** — all errors return standard JSON (no stack traces)
- **Search/Filter** — client-side live search in every table
- **Dark/Light Mode** — toggle between themes

---

## Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL (running locally or remote)

---

## Environment Variables

**Set these before running the application.**

| Variable | Required | Default | Description |
|---|---|---|---|
| `DB_PASSWORD` | ✅ Yes | none | PostgreSQL password |
| `APP_PASSWORD` | ✅ Yes | none | Web login password |
| `DB_URL` | No | `jdbc:postgresql://localhost:5432/ems?ssl=false` | Full JDBC URL |
| `DB_USERNAME` | No | `postgres` | PostgreSQL username |
| `APP_USERNAME` | No | `admin` | Web login username |

> See `src/main/resources/application-example.properties` for setup instructions.

### Windows (PowerShell)

```powershell
$env:DB_PASSWORD="your_db_password"
$env:APP_PASSWORD="your_app_password"
```

### Windows (Command Prompt)

```cmd
set DB_PASSWORD=your_db_password
set APP_PASSWORD=your_app_password
```

### Linux / macOS

```bash
export DB_PASSWORD=your_db_password
export APP_PASSWORD=your_app_password
```

---

## Running the Application

```bash
# 1. Clone the repository
git clone https://github.com/palak0991/battlefield-analysis-spring-boot.git
cd battlefield-analysis-spring-boot/springboot-backend

# 2. Set environment variables (see above)

# 3. Run
.\mvnw.cmd spring-boot:run        # Windows
./mvnw spring-boot:run            # Linux/macOS
```

Application starts at: **http://localhost:8080**

Login page: **http://localhost:8080/login.html**

---

## Running Tests

Tests use an H2 in-memory database — no PostgreSQL required.

```bash
.\mvnw.cmd test -DDB_PASSWORD=test -DAPP_PASSWORD=test -DAPP_USERNAME=admin
```

**Test suite: 21 tests, 0 failures**

| Test Class | Type | Tests |
|---|---|---|
| `SpringbootBackendApplicationTests` | Integration (H2) | 1 |
| `EmployeeServiceTest` | Unit (Mockito) | 8 |
| `WeaponServiceRelationshipTest` | Unit (Mockito) | 3 |
| `EmployeeControllerTest` | Controller (MockMvc) | 9 |

---

## API Endpoints

See [`API_DOCUMENTATION.md`](API_DOCUMENTATION.md) for complete endpoint documentation.

| Module | Base URL |
|---|---|
| Employees | `/api/employees` |
| Weapons | `/api/weapons` |
| Players | `/api/players` |
| Exercises | `/api/exercises` |
| Systems | `/api/systems` |

---

## Project Structure

See [`ARCHITECTURE.md`](ARCHITECTURE.md) for architecture diagrams and entity relationships.

---

## Improvements Made (vs. original)

See [`IMPROVEMENTS.md`](IMPROVEMENTS.md) for the full changelog with before/after and interview explanations.

Key fixes:
1. ✅ Bean Validation enforced (`@Valid` added to all controllers)
2. ✅ Global exception handler (`@RestControllerAdvice`)
3. ✅ Hardcoded password removed (now uses `APP_PASSWORD` env var)
4. ✅ Dashboard employee count fixed (was always 0)
5. ✅ `filterTable()` implemented (was called but never defined)
6. ✅ FK relationships fixed on update (weapon→player, player→exercise, system→exercise)
7. ✅ Thymeleaf dependency removed (was unused)
8. ✅ `spring.jpa.open-in-view=false` set
9. ✅ 21 meaningful tests added
10. ✅ Employee email uniqueness enforced (DB-level + 409 response)
11. ✅ SLF4J logging added throughout
12. ✅ Dead code removed (`index-old.html`, stale comments)

---

## Learning Resources

- [`PROJECT_LEARNING_GUIDE.md`](PROJECT_LEARNING_GUIDE.md) — Beginner-friendly explanation of every concept
- [`ARCHITECTURE.md`](ARCHITECTURE.md) — Architecture diagrams and request lifecycle
- [`IMPROVEMENTS.md`](IMPROVEMENTS.md) — Changelog with interview-ready explanations
- [`API_DOCUMENTATION.md`](API_DOCUMENTATION.md) — Complete API reference