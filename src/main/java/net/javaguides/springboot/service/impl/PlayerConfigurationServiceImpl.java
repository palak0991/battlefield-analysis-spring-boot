package net.javaguides.springboot.service.impl;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.PlayerConfiguration;
import net.javaguides.springboot.repository.PlayerConfigurationRepository;
import net.javaguides.springboot.service.PlayerConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for player configuration business logic.
 */
@Service
public class PlayerConfigurationServiceImpl implements PlayerConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(PlayerConfigurationServiceImpl.class);

    private final PlayerConfigurationRepository playerConfigurationRepository;

    public PlayerConfigurationServiceImpl(PlayerConfigurationRepository playerConfigurationRepository) {
        this.playerConfigurationRepository = playerConfigurationRepository;
    }

    @Override
    public PlayerConfiguration savePlayerConfiguration(PlayerConfiguration player) {
        log.debug("Saving player: {}", player.getPlayerName());
        return playerConfigurationRepository.save(player);
    }

    @Override
    public List<PlayerConfiguration> getAllPlayerConfigurations() {
        return playerConfigurationRepository.findAll();
    }

    @Override
    public PlayerConfiguration getPlayerConfigurationById(long id) {
        return playerConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlayerConfiguration", "Id", id));
    }

    @Override
    public PlayerConfiguration updatePlayerConfiguration(PlayerConfiguration player, long id) {
        PlayerConfiguration existing = playerConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlayerConfiguration", "Id", id));

        existing.setPlayerName(player.getPlayerName());
        existing.setRole(player.getRole());
        existing.setUnit(player.getUnit());
        existing.setTotalLoginTime(player.getTotalLoginTime());
        // BUG FIX: Previously the exercise FK was never updated on edit.
        // Now the player's exercise assignment is correctly persisted.
        existing.setExercise(player.getExercise());

        log.debug("Updating player id: {}", id);
        return playerConfigurationRepository.save(existing);
    }

    @Override
    public void deletePlayerConfiguration(long id) {
        playerConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlayerConfiguration", "Id", id));
        log.debug("Deleting player id: {}", id);
        playerConfigurationRepository.deleteById(id);
    }
}