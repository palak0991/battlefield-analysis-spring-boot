# IMPROVEMENTS.md — Changelog for Interview Preparation

Each entry documents a real change made to the project, why it was made, and how to explain it.

---

## 1. Validation Was Not Enforced

**Problem:** `spring-boot-starter-validation` was in pom.xml. `@NotBlank` and `@Email` were imported in `Employee.java` but NOT applied to any fields. `@Valid` was missing from all controllers. Invalid data was silently saved.

**Before:**
```java
@Column(name = "first_name")
private String firstName; // no @NotBlank — blank names saved silently
```

```java
public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) // no @Valid
```

**After:**
```java
@NotBlank(message = "First name is required")
@Column(name = "first_name")
private String firstName;
```

```java
public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee)
```

**Why:** Validation is a fundamental requirement of any API. Without `@Valid`, the annotations on the entity fields do nothing.

**Files changed:** `Employee.java`, `WeaponConfiguration.java`, `PlayerConfiguration.java`, `ExerciseConfiguration.java`, `SystemConfiguration.java`, all 5 controllers.

**Technical concept:** Jakarta Bean Validation — annotation-driven field validation that runs automatically when `@Valid` is on the parameter.

**Interview explanation:** "The project had the validation library and annotations, but `@Valid` was missing from the controller parameters, so requests were never actually validated. I added `@Valid` to every `@RequestBody` parameter and moved the validation annotations onto the correct fields."

---

## 2. No Global Exception Handler

**Problem:** `ResourceNotFoundException` used `@ResponseStatus(NOT_FOUND)` which produces Spring's default HTML/JSON error format. There was no consistent error response structure for validation errors, duplicates, or unexpected errors.

**Before:** No `@RestControllerAdvice` existed. Different errors returned different formats.

**After:** Created `GlobalExceptionHandler.java` with handlers for:
- `ResourceNotFoundException` → 404
- `MethodArgumentNotValidException` → 400
- `DataIntegrityViolationException` → 409 (duplicate email)
- `IllegalArgumentException` → 400
- `Exception` (catch-all) → 500 (no stack trace exposed)

All return a consistent JSON:
```json
{ "timestamp": "...", "status": 404, "error": "Not Found", "message": "...", "path": "..." }
```

**Files changed/added:** `GlobalExceptionHandler.java` (new), `ResourceNotFoundException.java` (cleaned up).

**Technical concept:** `@RestControllerAdvice` — a Spring component that intercepts exceptions thrown anywhere in the controller layer and handles them centrally.

**Interview explanation:** "I added a global exception handler using `@RestControllerAdvice` so all API errors return a consistent JSON structure. Before, validation errors looked different from 404s and there was no protection against stack traces being sent to the client."

---

## 3. Hardcoded Password in Source Code

**Problem:** `SecurityConfig.java` contained `battlefield123` as a raw string literal committed to git.

**Before:**
```java
.password(encoder.encode("battlefield123"))
```

**After:**
```java
@Value("${APP_PASSWORD}")
private String appPassword;
// ...
.password(encoder.encode(appPassword))
```

**Why:** Credentials in source code are a security vulnerability. If the repository is public (it is), anyone can read the password.

**Files changed:** `SecurityConfig.java`, `application-example.properties` (new).

**Technical concept:** Externalized configuration — using environment variables or property files to keep secrets out of source code.

**Interview explanation:** "The admin password was hardcoded in the source file, which is a security risk especially for a public repository. I moved it to an environment variable `APP_PASSWORD` using Spring's `@Value` annotation. The application now fails to start if the variable isn't set, which forces developers to configure it properly."

---

## 4. Dashboard Employee Count Was Always Zero

**Problem:** `loadDashboardStats()` in `app.js` fetched weapons, players, exercises, and systems — but NOT employees. The `statEmployees` div in the dashboard always showed 0.

**Before:** `const [weapons, players, exercises, systems] = await Promise.all([...])`

**After:** `const [employees, weapons, players, exercises, systems] = await Promise.all([fetch('/api/employees')...])`

**Files changed:** `app.js`

**Technical concept:** JavaScript `Promise.all()` — runs multiple fetch requests concurrently and waits for all of them.

**Interview explanation:** "The dashboard had a card for employees but the JavaScript code forgot to include the employee API call. I added `fetch('/api/employees')` to the parallel fetch calls and wired the result to the `statEmployees` element."

---

## 5. `filterTable()` Called But Never Defined

**Problem:** Every section's search input called `filterTable('employees', this.value)` in the HTML. But the function didn't exist in `app.js`. This caused a JavaScript `ReferenceError` on every keystroke.

**Before:** Function not defined — runtime error on search.

**After:**
```javascript
function filterTable(entityKey, searchValue) {
    const table = document.getElementById('table-' + entityKey);
    const rows = table.querySelectorAll('tbody tr');
    const query = searchValue.toLowerCase().trim();
    rows.forEach(row => {
        row.style.display = (query === '' || row.textContent.toLowerCase().includes(query)) ? '' : 'none';
    });
}
```

**Files changed:** `app.js`

**Technical concept:** Client-side filtering — hiding/showing DOM elements based on search without making new API requests.

**Interview explanation:** "The HTML had search inputs calling a `filterTable()` function that didn't exist, causing JavaScript errors on every keystroke. I implemented it as a simple DOM-based filter that hides rows that don't match the search term."

---

## 6. FK Relationships Not Updated on Edit

**Problem:** When updating a weapon, player, or system, the foreign key relationships (weapon→player, player→exercise, system→exercise) were never updated. If you changed "Assign to Player" in the UI, the old assignment remained in the database.

**Before (WeaponConfigurationServiceImpl):**
```java
existing.setWeaponType(...);
existing.setRange(...);
// player FK never set!
```

**After:**
```java
existing.setWeaponType(...);
existing.setRange(...);
existing.setPlayer(weapon.getPlayer()); // BUG FIX
```

**Files changed:** `WeaponConfigurationServiceImpl.java`, `PlayerConfigurationServiceImpl.java`, `SystemConfigurationServiceImpl.java`

**Technical concept:** JPA bidirectional relationships — when updating a `@ManyToOne` field, you must explicitly call `setX()` on the managed entity.

**Interview explanation:** "The update methods only copied primitive fields but ignored the foreign key relationships. So if a weapon was originally assigned to Player A and the user changed it to Player B in the UI, the database still showed Player A. I added `existing.setPlayer(weapon.getPlayer())` to all three affected services."

---

## 7. Thymeleaf Dependency Removed

**Problem:** `spring-boot-starter-thymeleaf` was in pom.xml but the project has no Thymeleaf templates. The frontend is served as plain static files.

**Before:** pom.xml included Thymeleaf.

**After:** Thymeleaf dependency removed. H2 (test-scope) and spring-security-test added.

**Why:** Unused dependencies increase build size and pull in unnecessary transitive dependencies.

**Files changed:** `pom.xml`

**Technical concept:** Maven dependency management — keeping only needed dependencies.

**Interview explanation:** "I found the Thymeleaf starter was included but never used — the entire frontend is plain HTML served as static resources. I removed it and verified the application still builds and runs correctly. I also took this opportunity to add the Spring Security Test and H2 test dependencies which were actually needed."

---

## 8. `spring.jpa.open-in-view=false` Configured

**Problem:** `open-in-view` defaults to `true` in Spring Boot, which keeps a database connection open for the entire HTTP request — including view rendering. This can cause N+1 query problems and is considered bad practice.

**After:** `spring.jpa.open-in-view=false` added to `application.properties`.

**Technical concept:** OSIV (Open Session In View) — a pattern that keeps the JPA session open across the full HTTP lifecycle. Disabling it forces all data loading to happen within the service/transaction layer.

**Interview explanation:** "I disabled Open Session In View because it was keeping database sessions open unnecessarily. It can mask lazy-loading bugs and cause performance issues at scale. With it disabled, any lazy-loaded data must be explicitly loaded in the service layer."

---

## 9. Meaningful Tests Added

**Problem:** Only one test existed: `contextLoads()`, which required a live PostgreSQL connection and would fail in CI.

**After:**
- `SpringbootBackendApplicationTests` — fixed to use H2 in-memory DB via `@TestPropertySource`
- `EmployeeServiceTest` — 8 unit tests with Mockito (no DB required)
- `WeaponServiceRelationshipTest` — 3 tests specifically for the FK bug fix
- `EmployeeControllerTest` — 9 MockMvc controller tests including validation and auth

**Total: 21 tests, all passing.**

**Technical concept:** Unit testing with Mockito, controller testing with `@WebMvcTest` + `MockMvc`, integration testing with `@SpringBootTest`.

**Interview explanation:** "I replaced the single ineffective context test with a suite of 21 tests. Service tests use Mockito to mock the repository — no database needed. Controller tests use MockMvc with `@WebMvcTest` to test HTTP layer behavior including validation failures and authentication."

---

## 10. Employee Email Uniqueness Enforced

**Problem:** Duplicate emails could be saved to the database because there was no unique constraint.

**After:**
```java
@Column(name = "email", unique = true)
```

And in `GlobalExceptionHandler`:
```java
@ExceptionHandler(DataIntegrityViolationException.class)
// returns HTTP 409 Conflict with clean message
```

**Technical concept:** Database-level unique constraint + application-level error handling.

**Interview explanation:** "Employee email had no uniqueness constraint at the database level, so duplicates could be saved. I added `unique = true` to the column and added a handler in the global exception handler to return HTTP 409 with a readable message when a duplicate is detected."

---

## 11. SLF4J Logging Added

**Problem:** Zero logging — impossible to debug issues in production.

**After:** Added `private static final Logger log = LoggerFactory.getLogger(...)` to all service implementations and controllers. Logs INFO for creates/updates/deletes, DEBUG for reads. Passwords are never logged.

**Technical concept:** SLF4J — a logging facade that works with any logging backend (Logback in Spring Boot by default).

**Interview explanation:** "There was no logging at all. I added SLF4J logging to all services and controllers. INFO level for state-changing operations, DEBUG for queries, WARN/ERROR for problems. I was careful not to log any credential values."

---

## 12. Dead Code Removed

**Removed:**
- `index-old.html` — old draft of the dashboard, not linked anywhere
- Stale comment in `app.js`: `"// players, exercises, systems, employees configs go here next session"` — all configs already existed above it
- Unnecessary `super()` calls in constructors

**Technical concept:** Code hygiene — dead code confuses future developers and wastes time.

**Interview explanation:** "I removed an unused `index-old.html` file, cleaned up stale comments that were misleading (claiming things weren't implemented when they actually were), and removed unnecessary `super()` constructor calls."

---

## 13. Consistent Controller Response Types

**Before:** `getAllEmployees()` returned `List<Employee>` directly (no `ResponseEntity`). All other methods returned `ResponseEntity`.

**After:** All controller methods return `ResponseEntity<...>` consistently.

**Interview explanation:** "The `getAllEmployees` method was inconsistent — it returned a raw List while every other method returned `ResponseEntity`. I standardised all controller methods to use `ResponseEntity` for consistent HTTP status control."
