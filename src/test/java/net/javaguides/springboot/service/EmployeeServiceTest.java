package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployeeServiceImpl.
 *
 * These tests use Mockito to mock the EmployeeRepository so no database is needed.
 * Each test verifies the SERVICE behaviour in isolation.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee();
        sampleEmployee.setId(1L);
        sampleEmployee.setFirstName("Priya");
        sampleEmployee.setLastName("Sharma");
        sampleEmployee.setEmail("priya@example.com");
    }

    // -------------------------------------------------------
    // CREATE
    // -------------------------------------------------------
    @Test
    @DisplayName("saveEmployee should return saved employee")
    void saveEmployee_Success() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        Employee result = employeeService.saveEmployee(sampleEmployee);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Priya");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    // -------------------------------------------------------
    // READ — all
    // -------------------------------------------------------
    @Test
    @DisplayName("getAllEmployees should return list of employees")
    void getAllEmployees_ReturnsList() {
        when(employeeRepository.findAll()).thenReturn(List.of(sampleEmployee));

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("priya@example.com");
    }

    // -------------------------------------------------------
    // READ — by ID (found)
    // -------------------------------------------------------
    @Test
    @DisplayName("getEmployeeById should return employee when found")
    void getEmployeeById_Found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));

        Employee result = employeeService.getEmployeeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    // -------------------------------------------------------
    // READ — by ID (not found)
    // -------------------------------------------------------
    @Test
    @DisplayName("getEmployeeById should throw ResourceNotFoundException when employee does not exist")
    void getEmployeeById_NotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee")
                .hasMessageContaining("999");
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------
    @Test
    @DisplayName("updateEmployee should update and return the employee")
    void updateEmployee_Success() {
        Employee updatedData = new Employee();
        updatedData.setFirstName("Ananya");
        updatedData.setLastName("Verma");
        updatedData.setEmail("ananya@example.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee result = employeeService.updateEmployee(updatedData, 1L);

        assertThat(result.getFirstName()).isEqualTo("Ananya");
        assertThat(result.getEmail()).isEqualTo("ananya@example.com");
        verify(employeeRepository).save(sampleEmployee);
    }

    // -------------------------------------------------------
    // UPDATE — not found
    // -------------------------------------------------------
    @Test
    @DisplayName("updateEmployee should throw ResourceNotFoundException when employee does not exist")
    void updateEmployee_NotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(sampleEmployee, 999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------
    @Test
    @DisplayName("deleteEmployee should call deleteById when employee exists")
    void deleteEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    // -------------------------------------------------------
    // DELETE — not found
    // -------------------------------------------------------
    @Test
    @DisplayName("deleteEmployee should throw ResourceNotFoundException when employee does not exist")
    void deleteEmployee_NotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(employeeRepository, never()).deleteById(any());
    }
}
