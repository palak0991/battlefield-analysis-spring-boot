package net.javaguides.springboot.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.WeaponConfiguration;
import net.javaguides.springboot.repository.WeaponConfigurationRepository;
import net.javaguides.springboot.service.WeaponConfigurationService;

@Service
public class WeaponConfigurationServiceImpl implements WeaponConfigurationService {

    private WeaponConfigurationRepository weaponConfigurationRepository;

    public WeaponConfigurationServiceImpl(WeaponConfigurationRepository weaponConfigurationRepository) {
        this.weaponConfigurationRepository = weaponConfigurationRepository;
    }

    @Override
    public WeaponConfiguration saveWeaponConfiguration(WeaponConfiguration weaponConfiguration) {
        return weaponConfigurationRepository.save(weaponConfiguration);
    }

    @Override
    public List<WeaponConfiguration> getAllWeaponConfigurations() {
        return weaponConfigurationRepository.findAll();
    }

    @Override
    public WeaponConfiguration getWeaponConfigurationById(long id) {
        return weaponConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("WeaponConfiguration", "Id", id));
    }

    @Override
    public WeaponConfiguration updateWeaponConfiguration(WeaponConfiguration weaponConfiguration, long id) {
        WeaponConfiguration existing = weaponConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("WeaponConfiguration", "Id", id));
        existing.setWeaponType(weaponConfiguration.getWeaponType());
        existing.setRange(weaponConfiguration.getRange());
        existing.setTotal(weaponConfiguration.getTotal());
        existing.setCurrent(weaponConfiguration.getCurrent());
        weaponConfigurationRepository.save(existing);
        return existing;
    }

    @Override
    public void deleteWeaponConfiguration(long id) {
        weaponConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("WeaponConfiguration", "Id", id));
        weaponConfigurationRepository.deleteById(id);
    }
}