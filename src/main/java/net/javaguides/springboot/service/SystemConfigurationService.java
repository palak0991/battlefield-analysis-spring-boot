package net.javaguides.springboot.service;

import java.util.List;

import net.javaguides.springboot.model.SystemConfiguration;

public interface SystemConfigurationService {
    SystemConfiguration saveSystemConfiguration(SystemConfiguration systemConfiguration);
    List<SystemConfiguration> getAllSystemConfigurations();
    SystemConfiguration getSystemConfigurationById(long id);
    SystemConfiguration updateSystemConfiguration(SystemConfiguration systemConfiguration, long id);
    void deleteSystemConfiguration(long id);
}