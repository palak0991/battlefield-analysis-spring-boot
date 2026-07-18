package net.javaguides.springboot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.springboot.model.SystemConfiguration;
import net.javaguides.springboot.service.SystemConfigurationService;

@RestController
@RequestMapping("/api/systems")
public class SystemConfigurationController {

    private SystemConfigurationService systemConfigurationService;

    public SystemConfigurationController(SystemConfigurationService systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;
    }

    @PostMapping()
    public ResponseEntity<SystemConfiguration> saveSystemConfiguration(@RequestBody SystemConfiguration systemConfiguration) {
        return new ResponseEntity<>(systemConfigurationService.saveSystemConfiguration(systemConfiguration), HttpStatus.CREATED);
    }

    @GetMapping
    public List<SystemConfiguration> getAllSystemConfigurations() {
        return systemConfigurationService.getAllSystemConfigurations();
    }

    @GetMapping("{id}")
    public ResponseEntity<SystemConfiguration> getSystemConfigurationById(@PathVariable("id") long id) {
        return new ResponseEntity<>(systemConfigurationService.getSystemConfigurationById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<SystemConfiguration> updateSystemConfiguration(@PathVariable("id") long id, @RequestBody SystemConfiguration systemConfiguration) {
        return new ResponseEntity<>(systemConfigurationService.updateSystemConfiguration(systemConfiguration, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSystemConfiguration(@PathVariable("id") long id) {
        systemConfigurationService.deleteSystemConfiguration(id);
        return new ResponseEntity<>("System configuration deleted successfully!", HttpStatus.OK);
    }
}