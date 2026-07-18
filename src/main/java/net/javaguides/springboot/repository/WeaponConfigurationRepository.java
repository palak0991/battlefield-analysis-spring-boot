package net.javaguides.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.springboot.model.WeaponConfiguration;

public interface WeaponConfigurationRepository extends JpaRepository<WeaponConfiguration, Long> {
}