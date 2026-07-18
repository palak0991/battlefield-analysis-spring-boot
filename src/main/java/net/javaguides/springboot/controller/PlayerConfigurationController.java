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

import net.javaguides.springboot.model.PlayerConfiguration;
import net.javaguides.springboot.service.PlayerConfigurationService;

@RestController
@RequestMapping("/api/players")
public class PlayerConfigurationController {

    private PlayerConfigurationService playerConfigurationService;

    public PlayerConfigurationController(PlayerConfigurationService playerConfigurationService) {
        this.playerConfigurationService = playerConfigurationService;
    }

    @PostMapping()
    public ResponseEntity<PlayerConfiguration> savePlayerConfiguration(@RequestBody PlayerConfiguration playerConfiguration) {
        return new ResponseEntity<>(playerConfigurationService.savePlayerConfiguration(playerConfiguration), HttpStatus.CREATED);
    }

    @GetMapping
    public List<PlayerConfiguration> getAllPlayerConfigurations() {
        return playerConfigurationService.getAllPlayerConfigurations();
    }

    @GetMapping("{id}")
    public ResponseEntity<PlayerConfiguration> getPlayerConfigurationById(@PathVariable("id") long id) {
        return new ResponseEntity<>(playerConfigurationService.getPlayerConfigurationById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<PlayerConfiguration> updatePlayerConfiguration(@PathVariable("id") long id, @RequestBody PlayerConfiguration playerConfiguration) {
        return new ResponseEntity<>(playerConfigurationService.updatePlayerConfiguration(playerConfiguration, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePlayerConfiguration(@PathVariable("id") long id) {
        playerConfigurationService.deletePlayerConfiguration(id);
        return new ResponseEntity<>("Player configuration deleted successfully!", HttpStatus.OK);
    }
}