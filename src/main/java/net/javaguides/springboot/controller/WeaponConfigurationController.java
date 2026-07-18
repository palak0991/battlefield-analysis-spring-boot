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

import net.javaguides.springboot.model.WeaponConfiguration;
import net.javaguides.springboot.service.WeaponConfigurationService;

@RestController
@RequestMapping("/api/weapons")
public class WeaponConfigurationController {

    private WeaponConfigurationService weaponConfigurationService;

    public WeaponConfigurationController(WeaponConfigurationService weaponConfigurationService) {
        this.weaponConfigurationService = weaponConfigurationService;
    }

    @PostMapping()
    public ResponseEntity<WeaponConfiguration> saveWeaponConfiguration(@RequestBody WeaponConfiguration weaponConfiguration) {
        return new ResponseEntity<>(weaponConfigurationService.saveWeaponConfiguration(weaponConfiguration), HttpStatus.CREATED);
    }

    @GetMapping
    public List<WeaponConfiguration> getAllWeaponConfigurations() {
        return weaponConfigurationService.getAllWeaponConfigurations();
    }

    @GetMapping("{id}")
    public ResponseEntity<WeaponConfiguration> getWeaponConfigurationById(@PathVariable("id") long id) {
        return new ResponseEntity<>(weaponConfigurationService.getWeaponConfigurationById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<WeaponConfiguration> updateWeaponConfiguration(@PathVariable("id") long id, @RequestBody WeaponConfiguration weaponConfiguration) {
        return new ResponseEntity<>(weaponConfigurationService.updateWeaponConfiguration(weaponConfiguration, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteWeaponConfiguration(@PathVariable("id") long id) {
        weaponConfigurationService.deleteWeaponConfiguration(id);
        return new ResponseEntity<>("Weapon configuration deleted successfully!", HttpStatus.OK);
    }
}