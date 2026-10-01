package net.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller-layer tests for EmployeeController.
 *
 * Uses @WebMvcTest to load only the web layer (no database, no full context).
 * Services are mocked with @MockBean.
 * @WithMockUser simulates an authenticated user to bypass Spring Security.
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------------------------------------
    // GET all employees — success
    // -------------------------------------------------------
    @Test
    @WithMockUser
    @DisplayName("GET /api/employees should return 200 and list of employees")
    void getAllEmployees_Returns200() throws Exception {
        Employee emp = new Employee();
        emp.setId(1L);
        emp.setFirstName("Priya");
        emp.setLastName("Sharma");
        emp.setEmail("priya@example.com");

        when(employeeService.getAllEmployees()).thenReturn(List.of(emp));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Priya"))
                .andExpect(jsonPath("$[0].email").value("priya@example.com"));
    }

    // -------------------------------------------------------
    // GET by ID — success
    // -------------------------------------------------------
    @Test
    @WithMockUser
    @DisplayName("GET /api/employees/{id} should return 200 when employee exists")
    void getEmployeeById_Returns200() throws Exception {
        Employee emp = new Employee();
        emp.setId(1L);
        emp.setFirstName("Priya");

        when(employeeService.getEmployeeById(1L)).thenReturn(emp);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Priya"));
    }

    // -------------------------------------------------------
    // GET by ID — 404
    // -------------------------------------------------------
    @Test
    @WithMockUser
    @DisplayName("GET /api/employees/{id} should return 404 when employee does not exist")
    void getEmployeeById_Returns404() throws Exception {
        when(employeeService.getEmployeeById(999L))
                .thenThrow(new ResourceNotFoundException("Employee", "Id", 999L));

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Employee not found with Id: '999'"));
    }

    // -------------------------------------------------------
    // POST — success
    // -------------------------------------------------------
    @Test
    @WithMockUser
    @DisplayName("POST /api/employees should return 201 with saved employee")
    void createEmployee_Returns201() throws Exception {
        Employee emp = new Employee();
        emp.setFirstName("Rahul");
        emp.setLastName("Gupta");
        emp.setEmail("rahul@example.com");

        Employee saved = new Employee();
        saved.setId(2L);
        saved.setFirstName("Rahul");
        saved.setLastName("Gupta");
        saved.setEmail("rahul@example.com");

        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(post("/api/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value("Rahul"));
    }

    // -------------------------------------------------------
    // POST — validation failure (blank firstName)
    // -------------------------------------------------------
    @Test
    @WithMockUser
    @DisplayName("POST /api/employees with blank firstName should return 400")
    void createEmployee_BlankFirstName_Returns400() throws Exception {
        Employee invalid = new Employee();
        invalid.setFirstName(""); // blank — violates @NotBlank
        invalid.setEmail("test@example.com");

        mockMvc.perform(post("/api/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(employeeService, never()).saveEmployee(any());
    }

    // -------------------------------------------------------
    // POST — validation failure (invalid email)
    // -------------------------------------------------------
    @Test
    @WithMockUser
    @DisplayName("POST /api/employees with invalid email should return 400")
    void createEmployee_InvalidEmail_Returns400() throws Exception {
        Employee invalid = new Employee();
        invalid.setFirstName("Test");
        invalid.setEmail("not-an-email"); // violates @Email

        mockMvc.perform(post("/api/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).saveEmployee(any());
    }

    // -------------------------------------------------------
    // PUT — success
    // -------------------------------------------------------
    @Test
    @WithMockUser
    @DisplayName("PUT /api/employees/{id} should return 200 with updated employee")
    void updateEmployee_Returns200() throws Exception {
        Employee update = new Employee();
        update.setFirstName("Updated");
        update.setEmail("updated@example.com");

        Employee result = new Employee();
        result.setId(1L);
        result.setFirstName("Updated");

        when(employeeService.updateEmployee(any(Employee.class), eq(1L))).thenReturn(result);

        mockMvc.perform(put("/api/employees/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    // -------------------------------------------------------
    // DELETE — success
    // -------------------------------------------------------
    @Test
    @WithMockUser
    @DisplayName("DELETE /api/employees/{id} should return 200")
    void deleteEmployee_Returns200() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1").with(csrf()))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    // -------------------------------------------------------
    // Unauthenticated request — MockMvc with @WebMvcTest returns 401
    // (In a real browser, Spring Security redirects to /login.html,
    //  but MockMvc does not follow redirects — it returns 401 Unauthorized)
    // -------------------------------------------------------
    @Test
    @DisplayName("GET /api/employees without authentication should return 401")
    void getAllEmployees_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isUnauthorized()); // 401 in MockMvc test context
    }
}
