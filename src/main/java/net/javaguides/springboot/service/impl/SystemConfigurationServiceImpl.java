package net.javaguides.springboot.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.SystemConfiguration;
import net.javaguides.springboot.repository.SystemConfigurationRepository;
import net.javaguides.springboot.service.SystemConfigurationService;

@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private SystemConfigurationRepository systemConfigurationRepository;

    public SystemConfigurationServiceImpl(SystemConfigurationRepository systemConfigurationRepository) {
        this.systemConfigurationRepository = systemConfigurationRepository;
    }

    @Override
    public SystemConfiguration saveSystemConfiguration(SystemConfiguration systemConfiguration) {
        return systemConfigurationRepository.save(systemConfiguration);
    }

    @Override
    public List<SystemConfiguration> getAllSystemConfigurations() {
        return systemConfigurationRepository.findAll();
    }

    @Override
    public SystemConfiguration getSystemConfigurationById(long id) {
        return systemConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("SystemConfiguration", "Id", id));
    }

    @Override
    public SystemConfiguration updateSystemConfiguration(SystemConfiguration systemConfiguration, long id) {
        SystemConfiguration existing = systemConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("SystemConfiguration", "Id", id));
        existing.setResourceType(systemConfiguration.getResourceType());
        existing.setTotal(systemConfiguration.getTotal());
        existing.setCurrent(systemConfiguration.getCurrent());
        systemConfigurationRepository.save(existing);
        return existing;
    }

    @Override
    public void deleteSystemConfiguration(long id) {
        systemConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("SystemConfiguration", "Id", id));
        systemConfigurationRepository.deleteById(id);
    }
}