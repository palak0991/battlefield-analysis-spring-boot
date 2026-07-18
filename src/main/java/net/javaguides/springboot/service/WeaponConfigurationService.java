package net.javaguides.springboot.service;

import java.util.List;

import net.javaguides.springboot.model.WeaponConfiguration;

public interface WeaponConfigurationService {
    WeaponConfiguration saveWeaponConfiguration(WeaponConfiguration weaponConfiguration);
    List<WeaponConfiguration> getAllWeaponConfigurations();
    WeaponConfiguration getWeaponConfigurationById(long id);
    WeaponConfiguration updateWeaponConfiguration(WeaponConfiguration weaponConfiguration, long id);
    void deleteWeaponConfiguration(long id);
}