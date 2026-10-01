package net.javaguides.springboot.controller;

import jakarta.validation.Valid;
import net.javaguides.springboot.model.SystemConfiguration;
import net.javaguides.springboot.service.SystemConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for System Configuration CRUD operations.
 * Base URL: /api/systems
 */
@RestController
@RequestMapping("/api/systems")
public class SystemConfigurationController {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigurationController.class);

    private final SystemConfigurationService systemConfigurationService;

    public SystemConfigurationController(SystemConfigurationService systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;
    }

    // POST /api/systems
    @PostMapping
    public ResponseEntity<SystemConfiguration> createSystem(
            @Valid @RequestBody SystemConfiguration system) {
        log.info("Request to create system resource: {}", system.getResourceType());
        return new ResponseEntity<>(systemConfigurationService.saveSystemConfiguration(system), HttpStatus.CREATED);
    }

    // GET /api/systems
    @GetMapping
    public ResponseEntity<List<SystemConfiguration>> getAllSystems() {
        List<SystemConfiguration> systems = systemConfigurationService.getAllSystemConfigurations();
        log.debug("Returning {} system configurations", systems.size());
        return ResponseEntity.ok(systems);
    }

    // GET /api/systems/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SystemConfiguration> getSystemById(@PathVariable long id) {
        log.debug("Request to get system with id: {}", id);
        return ResponseEntity.ok(systemConfigurationService.getSystemConfigurationById(id));
    }

    // PUT /api/systems/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SystemConfiguration> updateSystem(
            @PathVariable long id,
            @Valid @RequestBody SystemConfiguration system) {
        log.info("Request to update system with id: {}", id);
        return ResponseEntity.ok(systemConfigurationService.updateSystemConfiguration(system, id));
    }

    // DELETE /api/systems/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSystem(@PathVariable long id) {
        log.info("Request to delete system with id: {}", id);
        systemConfigurationService.deleteSystemConfiguration(id);
        return ResponseEntity.ok("System configuration deleted successfully");
    }
}