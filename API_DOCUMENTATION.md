# API Documentation — Battlefield Resource Management System

All endpoints require authentication (Spring Security session).
Unauthenticated requests are redirected to `/login.html`.

**Base URL:** `http://localhost:8080`

**Authentication:** Form-based login at `/login.html`. Session cookie is sent automatically by the browser.

---

## Error Response Format

All errors return a consistent JSON structure:

```json
{
  "timestamp": "2024-10-02T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with Id: '10'",
  "path": "/api/employees/10"
}
```

| HTTP Code | Meaning |
|---|---|
| 200 | Success |
| 201 | Created |
| 400 | Validation error / bad request |
| 401 | Not authenticated (redirected to login) |
| 404 | Resource not found |
| 409 | Conflict (e.g. duplicate email) |
| 500 | Unexpected server error |

---

## Employees

### GET /api/employees
**Purpose:** Retrieve all employees  
**Auth:** Required  
**Request Body:** None  
**Response:** `[{ "id": 1, "firstName": "Priya", "lastName": "Sharma", "email": "priya@example.com" }]`  
**Status:** 200

### GET /api/employees/{id}
**Purpose:** Retrieve a single employee by ID  
**Status:** 200 (found), 404 (not found)

### POST /api/employees
**Purpose:** Create a new employee  
**Request Body:** `{ "firstName": "Priya", "lastName": "Sharma", "email": "priya@example.com" }`  
**Validation:** firstName required (max 100), email must be valid and unique, lastName max 100  
**Status:** 201 (created), 400 (validation), 409 (duplicate email)

### PUT /api/employees/{id}
**Purpose:** Update an employee  
**Status:** 200 (updated), 400 (validation), 404 (not found)

### DELETE /api/employees/{id}
**Purpose:** Delete an employee  
**Status:** 200 (deleted), 404 (not found)

---

## Weapons

### GET /api/weapons
**Response:** `[{ "id": 1, "weaponType": "AK-47", "range": 800, "total": 50, "current": 42, "player": {"id": 3, "playerName": "Alpha Squad"} }]`  
**Status:** 200

### GET /api/weapons/{id}
**Status:** 200 (found), 404 (not found)

### POST /api/weapons
**Request Body:** `{ "weaponType": "AK-47", "range": 800, "total": 50, "current": 42, "player": {"id": 3} }`  
**Validation:** weaponType required (max 100), range/total/current >= 0, player optional  
**Status:** 201 (created), 400 (validation)

### PUT /api/weapons/{id}
**Note:** Player FK correctly updated on edit (bug fixed).  
**Status:** 200, 400, 404

### DELETE /api/weapons/{id}
**Status:** 200 (deleted), 404 (not found)

---

## Players

### GET /api/players
**Response:** `[{ "id": 1, "playerName": "Alpha Squad", "role": "Infantry", "unit": "7th Bn", "totalLoginTime": 12, "exercise": {"id": 2, "name": "Winter Storm"} }]`  
**Status:** 200

### GET /api/players/{id}
**Status:** 200, 404

### POST /api/players
**Request Body:** `{ "playerName": "Alpha Squad", "role": "Infantry", "unit": "7th Bn", "totalLoginTime": 12, "exercise": {"id": 2} }`  
**Validation:** playerName required (max 100), totalLoginTime >= 0, exercise optional  
**Status:** 201, 400

### PUT /api/players/{id}
**Note:** Exercise FK correctly updated on edit (bug fixed).  
**Status:** 200, 400, 404

### DELETE /api/players/{id}
**Note:** Also deletes associated weapons (cascade).  
**Status:** 200, 404

---

## Exercises

### GET /api/exercises
**Response:** `[{ "id": 1, "name": "Winter Storm", "location": "Rajasthan", "date": "2024-11-15", "commander": "Col. Mehta" }]`  
**Status:** 200

### GET /api/exercises/{id}
**Status:** 200, 404

### POST /api/exercises
**Request Body:** `{ "name": "Winter Storm", "location": "Rajasthan", "date": "2024-11-15", "commander": "Col. Mehta" }`  
**Validation:** name required (max 150), location max 200, commander max 100  
**Status:** 201, 400

### PUT /api/exercises/{id}
**Status:** 200, 400, 404

### DELETE /api/exercises/{id}
**Note:** Also deletes associated players and system resources (cascade).  
**Status:** 200, 404

---

## Systems

### GET /api/systems
**Response:** `[{ "id": 1, "resourceType": "Medical Unit", "total": 20, "current": 15, "exercise": {"id": 2} }]`  
**Status:** 200

### GET /api/systems/{id}
**Status:** 200, 404

### POST /api/systems
**Request Body:** `{ "resourceType": "Medical Unit", "total": 20, "current": 15, "exercise": {"id": 2} }`  
**Validation:** resourceType required (max 100), total/current >= 0, exercise optional  
**Status:** 201, 400

### PUT /api/systems/{id}
**Note:** Exercise FK correctly updated on edit (bug fixed).  
**Status:** 200, 400, 404

### DELETE /api/systems/{id}
**Status:** 200, 404
