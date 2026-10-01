package net.javaguides.springboot.controller;

import jakarta.validation.Valid;
import net.javaguides.springboot.model.WeaponConfiguration;
import net.javaguides.springboot.service.WeaponConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Weapon Configuration CRUD operations.
 * Base URL: /api/weapons
 */
@RestController
@RequestMapping("/api/weapons")
public class WeaponConfigurationController {

    private static final Logger log = LoggerFactory.getLogger(WeaponConfigurationController.class);

    private final WeaponConfigurationService weaponConfigurationService;

    public WeaponConfigurationController(WeaponConfigurationService weaponConfigurationService) {
        this.weaponConfigurationService = weaponConfigurationService;
    }

    // POST /api/weapons
    @PostMapping
    public ResponseEntity<WeaponConfiguration> createWeapon(
            @Valid @RequestBody WeaponConfiguration weapon) {
        log.info("Request to create weapon: {}", weapon.getWeaponType());
        return new ResponseEntity<>(weaponConfigurationService.saveWeaponConfiguration(weapon), HttpStatus.CREATED);
    }

    // GET /api/weapons
    @GetMapping
    public ResponseEntity<List<WeaponConfiguration>> getAllWeapons() {
        List<WeaponConfiguration> weapons = weaponConfigurationService.getAllWeaponConfigurations();
        log.debug("Returning {} weapon configurations", weapons.size());
        return ResponseEntity.ok(weapons);
    }

    // GET /api/weapons/{id}
    @GetMapping("/{id}")
    public ResponseEntity<WeaponConfiguration> getWeaponById(@PathVariable long id) {
        log.debug("Request to get weapon with id: {}", id);
        return ResponseEntity.ok(weaponConfigurationService.getWeaponConfigurationById(id));
    }

    // PUT /api/weapons/{id}
    @PutMapping("/{id}")
    public ResponseEntity<WeaponConfiguration> updateWeapon(
            @PathVariable long id,
            @Valid @RequestBody WeaponConfiguration weapon) {
        log.info("Request to update weapon with id: {}", id);
        return ResponseEntity.ok(weaponConfigurationService.updateWeaponConfiguration(weapon, id));
    }

    // DELETE /api/weapons/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWeapon(@PathVariable long id) {
        log.info("Request to delete weapon with id: {}", id);
        weaponConfigurationService.deleteWeaponConfiguration(id);
        return ResponseEntity.ok("Weapon configuration deleted successfully");
    }
}