# Project Learning Guide — Battlefield Resource Management System

This guide explains the entire project to a complete beginner. Read it top to bottom.

---

## 1. Problem Statement

Defence organisations need to track and manage battlefield resources — weapons, personnel, vehicles, and exercises. Spreadsheets don't work when multiple people need simultaneous access. A web application solves this by providing a shared, real-time database that anyone with access can use from a browser.

---

## 2. Project Purpose

This project is a **web-based resource management system** for DRDO (Defence Research and Development Organisation) internship purposes.

It allows authorised users to:
- Manage **employees** (staff records)
- Manage **weapons** (type, stock levels, player assignment)
- Manage **players** (participants in exercises)
- Manage **exercises** (battlefield training missions)
- Manage **systems** (resource allocations like vehicles, medical units)

All of this is done through a browser-based dashboard — no desktop software required.

---

## 3. Features

| Feature | Description |
|---|---|
| Secure login | Username/password login with session management |
| Dashboard | Live counts and resource allocation chart |
| CRUD for 5 modules | Create, Read, Update, Delete for all entities |
| Relationship management | Assign weapons to players, players to exercises |
| Search/filter | Client-side search in every table |
| Validation | Invalid data rejected with clear error messages |
| Consistent errors | All API errors return standard JSON |
| Dark/light mode | Toggle between dark and light themes |

---

## 4. Architecture — The Big Picture

```
Browser (your laptop)
    │  sends HTTP requests (GET, POST, PUT, DELETE)
    ▼
Spring Security
    │  checks: are you logged in?
    ▼
Spring MVC Controller
    │  validates input, calls service
    ▼
Spring Service
    │  business logic, throws errors
    ▼
Spring Data JPA Repository
    │  talks to database
    ▼
Hibernate ORM
    │  converts Java objects to SQL
    ▼
PostgreSQL Database
    │  stores everything
    ▼
Response comes back up the same chain as JSON
```

---

## 5. Folder Structure

```
springboot-backend/
├── src/main/java/net/javaguides/springboot/
│   ├── config/          SecurityConfig — Spring Security setup
│   ├── controller/      5 REST controllers — handle HTTP requests
│   ├── exception/       Custom exceptions + global error handler
│   ├── model/           5 JPA entities — Java classes mapped to DB tables
│   ├── repository/      5 repository interfaces — database access
│   └── service/         5 service interfaces + 5 implementations
├── src/main/resources/
│   ├── application.properties    Database + JPA configuration
│   └── static/
│       ├── index.html    Main dashboard
│       ├── login.html    Login page
│       ├── style.css     All styles
│       └── app.js        All JavaScript
└── src/test/            Unit and controller tests
```

---

## 6. Java Concepts Used

### Classes and Interfaces

An **interface** defines what methods must exist. A **class** provides the actual implementation.

```java
// Interface — defines the contract
public interface EmployeeService {
    Employee saveEmployee(Employee employee);
}

// Implementation — provides the actual code
@Service
public class EmployeeServiceImpl implements EmployeeService {
    public Employee saveEmployee(Employee employee) {
        return repository.save(employee);
    }
}
```

**Why use interfaces?** So that in tests, we can swap the real implementation with a fake (mock) one.

### Constructor Injection

```java
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repo;

    public EmployeeServiceImpl(EmployeeRepository repo) {
        this.repo = repo;  // Spring injects the repo automatically
    }
}
```

Spring sees the constructor parameter and automatically provides (`injects`) the right object. This is called **Dependency Injection**.

### Annotations

Annotations are notes you put on classes/methods that tell frameworks what to do.

| Annotation | Meaning |
|---|---|
| `@Entity` | This class is a database table |
| `@RestController` | This class handles HTTP requests and returns JSON |
| `@Service` | This class is a service (business logic) |
| `@Repository` | This class is a database access object |
| `@Autowired` / constructor injection | Inject this dependency automatically |
| `@Value("${X}")` | Inject value from application properties |
| `@Valid` | Validate this method parameter |
| `@NotBlank`, `@Email` | Validation rules on fields |

---

## 7. Spring Boot

**What it is:** Spring Boot is a framework that makes it easy to build Java web applications. It auto-configures a lot of things so you don't write boilerplate.

**Why it's used:** Without Spring Boot, you'd have to configure a web server, database connection, security, etc. manually. Spring Boot does this for you based on what dependencies you add to `pom.xml`.

**Where in this project:** Everything. The main class:

```java
@SpringBootApplication  // tells Spring Boot to start here
public class SpringbootBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootBackendApplication.class, args);
    }
}
```

---

## 8. REST (Representational State Transfer)

**What it is:** A standard way to build web APIs. Data is exchanged as JSON. HTTP methods describe what action to take.

| HTTP Method | Action | Example |
|---|---|---|
| `GET` | Read | `GET /api/employees` — get all employees |
| `POST` | Create | `POST /api/employees` — create new employee |
| `PUT` | Update | `PUT /api/employees/1` — update employee with id 1 |
| `DELETE` | Delete | `DELETE /api/employees/1` — delete employee with id 1 |

**Where in this project:** All 5 controllers expose REST endpoints.

---

## 9. JPA (Java Persistence API)

**What it is:** A specification (a set of rules) for how Java should talk to relational databases. JPA defines annotations like `@Entity`, `@Id`, `@Column`.

**Where in this project:** All 5 entity classes use JPA annotations.

```java
@Entity               // This class is a DB table
@Table(name="employees")
public class Employee {
    @Id               // This field is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto-increment
    private long id;

    @Column(name = "first_name")  // maps to the "first_name" column
    private String firstName;
}
```

---

## 10. Hibernate

**What it is:** The most popular implementation of JPA. Hibernate does the actual SQL generation behind the scenes.

**How it works:**
- When you call `repository.save(employee)`, Hibernate generates:
  `INSERT INTO employees (first_name, last_name, email) VALUES (?, ?, ?)`
- When you call `repository.findAll()`, Hibernate generates:
  `SELECT * FROM employees`

You never write SQL manually in this project — Hibernate handles it.

**Where in this project:** `application.properties` sets:
```properties
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```
This tells Hibernate to generate PostgreSQL-compatible SQL.

---

## 11. PostgreSQL

**What it is:** A powerful, open-source relational database.

**Tables created by Hibernate:**

| Table | Entity |
|---|---|
| `employees` | Employee |
| `weapon_configuration` | WeaponConfiguration |
| `player_configuration` | PlayerConfiguration |
| `exercise_configuration` | ExerciseConfiguration |
| `system_configuration` | SystemConfiguration |

**Connection config** (from environment variables):
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

---

## 12. Spring Security

**What it is:** A framework for adding authentication and authorisation to Spring applications.

**Authentication:** Proving who you are (login with username/password).  
**Authorisation:** Checking what you're allowed to do.

**In this project:**
- Single admin user stored **in memory** (not in the database)
- Password is BCrypt encoded: `BCryptPasswordEncoder` turns `mypassword` into a hash like `$2a$10$...`. The hash cannot be reversed.
- Credentials come from environment variables `APP_USERNAME` and `APP_PASSWORD`
- All API routes require login
- Login page (`/login.html`) and CSS/JS are public

---

## 13. Frontend

**What it is:** Pure HTML, CSS, and JavaScript. No React, no Angular.

**How it works:**

1. `index.html` defines the page structure — sidebar, sections, modal overlay
2. `style.css` defines all visual styling using CSS custom properties (design tokens)
3. `app.js` does everything dynamic:
   - Fetches data from the REST API using `fetch()`
   - Renders tables dynamically based on entity config
   - Handles modal forms for add/edit
   - Handles delete with confirmation
   - Filters table rows on search

**Key pattern — config-driven design:**
```javascript
const entityConfigs = {
    employees: {
        apiPath: '/api/employees',
        columns: [...],
        fields: [...]
    }
    // Same structure for all 5 modules
};
```
One set of functions handles ALL modules. Adding a new module = add one config block.

---

## 14. Request Lifecycle (Full Example)

User clicks "Add Weapon" and fills in the form. Here's what happens:

1. **Browser** — JavaScript collects form data, calls `fetch('/api/weapons', {method:'POST', body: JSON})`
2. **Spring Security** — checks the session cookie. User is logged in → allow through
3. **WeaponConfigurationController** — receives the POST. `@Valid` triggers validation on the body
4. **GlobalExceptionHandler** — if validation fails, immediately returns HTTP 400 with error JSON
5. **WeaponConfigurationService** — `saveWeaponConfiguration(weapon)` called
6. **WeaponConfigurationRepository** — `repository.save(weapon)` called
7. **Hibernate** — generates `INSERT INTO weapon_configuration (...) VALUES (...)`
8. **PostgreSQL** — executes the SQL, returns the new record with auto-generated ID
9. **Response** — flows back up: Controller returns HTTP 201 with the created weapon JSON
10. **Browser** — receives 201, shows success toast, reloads the table

---

## 15. Database Relationships

### One-to-Many

One exercise can have many players. Many players belong to one exercise.

```java
// ExerciseConfiguration
@OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
private List<PlayerConfiguration> players;

// PlayerConfiguration
@ManyToOne
@JoinColumn(name = "exercise_id")  // creates exercise_id FK column in DB
private ExerciseConfiguration exercise;
```

**In the database:**
```
exercise_configuration          player_configuration
   id | name                      id | playerName | exercise_id (FK)
-------|--------                   ---|------------|----------------
    1  | Winter Storm               1 | Alpha Squad|       1
    2  | Desert Fox                 2 | Beta Squad |       1
                                    3 | Gamma Squad|       2
```

### Cascade

`CascadeType.ALL` means: when you delete an exercise, automatically delete its players and systems too.

### @JsonIgnore

Prevents infinite loops in JSON. Without it:
- Exercise has list of Players
- Each Player has reference back to Exercise
- Exercise has list of Players again...
- → Infinite loop → StackOverflowError

`@JsonIgnore` on the `List<PlayerConfiguration> players` field tells Jackson "don't include this in JSON output".

---

## 16. Error Handling

### Before this project was improved:
Different errors returned different formats. Stack traces could leak to users.

### After:
`GlobalExceptionHandler` catches every error and returns:
```json
{
  "timestamp": "2024-10-02T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with Id: '99'",
  "path": "/api/employees/99"
}
```

| Scenario | HTTP Status |
|---|---|
| Resource not found | 404 |
| Validation failed | 400 |
| Duplicate email | 409 |
| Unexpected error | 500 (no stack trace) |

---

## 17. Validation

**Bean Validation** — annotate fields, Spring validates automatically.

```java
@NotBlank(message = "First name is required")
private String firstName;

@Email(message = "Email must be a valid email address")
private String email;
```

For validation to run, the controller must have `@Valid`:
```java
public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee)
```

Without `@Valid`, the annotations do nothing.

---

## 18. Testing

### Unit Tests (fast, no database)
Use **Mockito** to fake the repository:
```java
when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
Employee result = employeeService.getEmployeeById(1L);
assertThat(result.getFirstName()).isEqualTo("Priya");
```

### Controller Tests (MockMvc, no real server)
```java
@WebMvcTest(EmployeeController.class)
mockMvc.perform(get("/api/employees/999"))
       .andExpect(status().isNotFound());
```

### Spring Context Test (with H2 in-memory DB)
```java
@SpringBootTest
@TestPropertySource(properties = { "spring.datasource.url=jdbc:h2:mem:testdb", ... })
void contextLoads() { }
```

**Total: 21 tests, 0 failures.**

---

## 19. Important Annotations

| Annotation | Layer | Purpose |
|---|---|---|
| `@SpringBootApplication` | App | Bootstrap Spring Boot |
| `@RestController` | Controller | HTTP handler that returns JSON |
| `@RequestMapping("/api/x")` | Controller | Base URL prefix |
| `@GetMapping`, `@PostMapping`, etc. | Controller | HTTP method mapping |
| `@PathVariable` | Controller | Extract `{id}` from URL |
| `@RequestBody` | Controller | Parse JSON request body |
| `@Valid` | Controller | Trigger Bean Validation |
| `@Service` | Service | Business logic component |
| `@RestControllerAdvice` | Exception | Global exception handler |
| `@ExceptionHandler` | Exception | Handle specific exception type |
| `@Entity` | Model | JPA entity (maps to DB table) |
| `@Id`, `@GeneratedValue` | Model | Primary key + auto-increment |
| `@Column` | Model | Map field to DB column |
| `@ManyToOne`, `@OneToMany` | Model | JPA relationships |
| `@JoinColumn` | Model | Foreign key column |
| `@JsonIgnore` | Model | Exclude from JSON serialization |
| `@NotBlank`, `@Email`, `@Min` | Model | Validation rules |
| `@Repository` | Repository | Spring Data repository |
| `@Configuration` | Config | Spring configuration class |
| `@Bean` | Config | Register a Spring bean |
| `@Value("${X}")` | Config | Inject property/env var |

---

## 20. Common Interview Questions

**Q: What is Spring Boot?**
A: A framework that auto-configures a Spring application based on dependencies. It embeds a Tomcat server so you can run the app as a standalone JAR.

**Q: What is the difference between JPA and Hibernate?**
A: JPA is a specification (a standard interface). Hibernate is an implementation of that specification. Spring Boot uses Hibernate as the JPA provider by default.

**Q: What is dependency injection?**
A: Instead of a class creating its own dependencies, Spring provides (injects) them. This makes code more testable and loosely coupled.

**Q: What is `@RestController` vs `@Controller`?**
A: `@Controller` is for Thymeleaf/view-based apps. `@RestController` = `@Controller` + `@ResponseBody` — automatically serializes return values as JSON.

**Q: What is Spring Security session-based authentication?**
A: After successful login, Spring creates a server-side session and sends the browser a `JSESSIONID` cookie. On each request, the browser sends this cookie and Spring verifies the session.

**Q: Why is BCrypt used for passwords?**
A: BCrypt is a slow hashing algorithm designed for passwords. It's slow on purpose — makes brute-force attacks impractical. It also uses a random salt so the same password produces different hashes each time.

**Q: What is `@Valid` and what happens without it?**
A: `@Valid` tells Spring to run Bean Validation on the annotated parameter. Without it, validation annotations like `@NotBlank` are completely ignored — invalid data passes through.

**Q: What is CascadeType.ALL?**
A: Operations on the parent entity (save, delete, merge) are cascaded to the child entities. In this project, deleting an Exercise also deletes its Players and Systems.

**Q: What is `@JsonIgnore` used for?**
A: To prevent infinite recursion in JSON serialization when entities have bidirectional relationships. Without it, serializing an Exercise would include its Players, which would include the Exercise again, causing a StackOverflowError.

**Q: What is `@RestControllerAdvice`?**
A: A Spring component that intercepts exceptions thrown by any `@RestController` and handles them centrally. Used to return consistent error JSON instead of different formats from different places.
