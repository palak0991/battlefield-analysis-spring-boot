package net.javaguides.springboot.service;

import java.util.List;

import net.javaguides.springboot.model.PlayerConfiguration;

public interface PlayerConfigurationService {
    PlayerConfiguration savePlayerConfiguration(PlayerConfiguration playerConfiguration);
    List<PlayerConfiguration> getAllPlayerConfigurations();
    PlayerConfiguration getPlayerConfigurationById(long id);
    PlayerConfiguration updatePlayerConfiguration(PlayerConfiguration playerConfiguration, long id);
    void deletePlayerConfiguration(long id);
}