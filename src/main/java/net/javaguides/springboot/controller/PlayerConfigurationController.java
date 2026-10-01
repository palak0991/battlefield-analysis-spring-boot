package net.javaguides.springboot.controller;

import jakarta.validation.Valid;
import net.javaguides.springboot.model.PlayerConfiguration;
import net.javaguides.springboot.service.PlayerConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Player Configuration CRUD operations.
 * Base URL: /api/players
 */
@RestController
@RequestMapping("/api/players")
public class PlayerConfigurationController {

    private static final Logger log = LoggerFactory.getLogger(PlayerConfigurationController.class);

    private final PlayerConfigurationService playerConfigurationService;

    public PlayerConfigurationController(PlayerConfigurationService playerConfigurationService) {
        this.playerConfigurationService = playerConfigurationService;
    }

    // POST /api/players
    @PostMapping
    public ResponseEntity<PlayerConfiguration> createPlayer(
            @Valid @RequestBody PlayerConfiguration player) {
        log.info("Request to create player: {}", player.getPlayerName());
        return new ResponseEntity<>(playerConfigurationService.savePlayerConfiguration(player), HttpStatus.CREATED);
    }

    // GET /api/players
    @GetMapping
    public ResponseEntity<List<PlayerConfiguration>> getAllPlayers() {
        List<PlayerConfiguration> players = playerConfigurationService.getAllPlayerConfigurations();
        log.debug("Returning {} player configurations", players.size());
        return ResponseEntity.ok(players);
    }

    // GET /api/players/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PlayerConfiguration> getPlayerById(@PathVariable long id) {
        log.debug("Request to get player with id: {}", id);
        return ResponseEntity.ok(playerConfigurationService.getPlayerConfigurationById(id));
    }

    // PUT /api/players/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PlayerConfiguration> updatePlayer(
            @PathVariable long id,
            @Valid @RequestBody PlayerConfiguration player) {
        log.info("Request to update player with id: {}", id);
        return ResponseEntity.ok(playerConfigurationService.updatePlayerConfiguration(player, id));
    }

    // DELETE /api/players/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable long id) {
        log.info("Request to delete player with id: {}", id);
        playerConfigurationService.deletePlayerConfiguration(id);
        return ResponseEntity.ok("Player configuration deleted successfully");
    }
}