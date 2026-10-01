package net.javaguides.springboot.service.impl;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.SystemConfiguration;
import net.javaguides.springboot.repository.SystemConfigurationRepository;
import net.javaguides.springboot.service.SystemConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for system configuration business logic.
 */
@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigurationServiceImpl.class);

    private final SystemConfigurationRepository systemConfigurationRepository;

    public SystemConfigurationServiceImpl(SystemConfigurationRepository systemConfigurationRepository) {
        this.systemConfigurationRepository = systemConfigurationRepository;
    }

    @Override
    public SystemConfiguration saveSystemConfiguration(SystemConfiguration system) {
        log.debug("Saving system resource: {}", system.getResourceType());
        return systemConfigurationRepository.save(system);
    }

    @Override
    public List<SystemConfiguration> getAllSystemConfigurations() {
        return systemConfigurationRepository.findAll();
    }

    @Override
    public SystemConfiguration getSystemConfigurationById(long id) {
        return systemConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SystemConfiguration", "Id", id));
    }

    @Override
    public SystemConfiguration updateSystemConfiguration(SystemConfiguration system, long id) {
        SystemConfiguration existing = systemConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SystemConfiguration", "Id", id));

        existing.setResourceType(system.getResourceType());
        existing.setTotal(system.getTotal());
        existing.setCurrent(system.getCurrent());
        // BUG FIX: Previously the exercise FK was never updated on edit.
        // Now the system's exercise assignment is correctly persisted.
        existing.setExercise(system.getExercise());

        log.debug("Updating system id: {}", id);
        return systemConfigurationRepository.save(existing);
    }

    @Override
    public void deleteSystemConfiguration(long id) {
        systemConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SystemConfiguration", "Id", id));
        log.debug("Deleting system id: {}", id);
        systemConfigurationRepository.deleteById(id);
    }
}