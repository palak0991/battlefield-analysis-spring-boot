package net.javaguides.springboot.service.impl;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.WeaponConfiguration;
import net.javaguides.springboot.repository.WeaponConfigurationRepository;
import net.javaguides.springboot.service.WeaponConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for weapon configuration business logic.
 * All database access goes through WeaponConfigurationRepository.
 */
@Service
public class WeaponConfigurationServiceImpl implements WeaponConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(WeaponConfigurationServiceImpl.class);

    private final WeaponConfigurationRepository weaponConfigurationRepository;

    public WeaponConfigurationServiceImpl(WeaponConfigurationRepository weaponConfigurationRepository) {
        this.weaponConfigurationRepository = weaponConfigurationRepository;
    }

    @Override
    public WeaponConfiguration saveWeaponConfiguration(WeaponConfiguration weapon) {
        log.debug("Saving weapon: {}", weapon.getWeaponType());
        return weaponConfigurationRepository.save(weapon);
    }

    @Override
    public List<WeaponConfiguration> getAllWeaponConfigurations() {
        return weaponConfigurationRepository.findAll();
    }

    @Override
    public WeaponConfiguration getWeaponConfigurationById(long id) {
        return weaponConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WeaponConfiguration", "Id", id));
    }

    @Override
    public WeaponConfiguration updateWeaponConfiguration(WeaponConfiguration weapon, long id) {
        WeaponConfiguration existing = weaponConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WeaponConfiguration", "Id", id));

        existing.setWeaponType(weapon.getWeaponType());
        existing.setRange(weapon.getRange());
        existing.setTotal(weapon.getTotal());
        existing.setCurrent(weapon.getCurrent());
        // BUG FIX: Previously the player (FK) was never updated on edit.
        // Now we update the player assignment so frontend changes to "Assign to Player" are persisted.
        existing.setPlayer(weapon.getPlayer());

        log.debug("Updating weapon id: {}", id);
        return weaponConfigurationRepository.save(existing);
    }

    @Override
    public void deleteWeaponConfiguration(long id) {
        weaponConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WeaponConfiguration", "Id", id));
        log.debug("Deleting weapon id: {}", id);
        weaponConfigurationRepository.deleteById(id);
    }
}