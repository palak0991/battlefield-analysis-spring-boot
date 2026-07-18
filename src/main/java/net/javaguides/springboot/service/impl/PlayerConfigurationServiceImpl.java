package net.javaguides.springboot.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.PlayerConfiguration;
import net.javaguides.springboot.repository.PlayerConfigurationRepository;
import net.javaguides.springboot.service.PlayerConfigurationService;

@Service
public class PlayerConfigurationServiceImpl implements PlayerConfigurationService {

    private PlayerConfigurationRepository playerConfigurationRepository;

    public PlayerConfigurationServiceImpl(PlayerConfigurationRepository playerConfigurationRepository) {
        this.playerConfigurationRepository = playerConfigurationRepository;
    }

    @Override
    public PlayerConfiguration savePlayerConfiguration(PlayerConfiguration playerConfiguration) {
        return playerConfigurationRepository.save(playerConfiguration);
    }

    @Override
    public List<PlayerConfiguration> getAllPlayerConfigurations() {
        return playerConfigurationRepository.findAll();
    }

    @Override
    public PlayerConfiguration getPlayerConfigurationById(long id) {
        return playerConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("PlayerConfiguration", "Id", id));
    }

    @Override
    public PlayerConfiguration updatePlayerConfiguration(PlayerConfiguration playerConfiguration, long id) {
        PlayerConfiguration existing = playerConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("PlayerConfiguration", "Id", id));
        existing.setPlayerName(playerConfiguration.getPlayerName());
        existing.setRole(playerConfiguration.getRole());
        existing.setUnit(playerConfiguration.getUnit());
        existing.setTotalLoginTime(playerConfiguration.getTotalLoginTime());
        playerConfigurationRepository.save(existing);
        return existing;
    }

    @Override
    public void deletePlayerConfiguration(long id) {
        playerConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("PlayerConfiguration", "Id", id));
        playerConfigurationRepository.deleteById(id);
    }
}