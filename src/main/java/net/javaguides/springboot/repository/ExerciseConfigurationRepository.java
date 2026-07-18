package net.javaguides.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.springboot.model.ExerciseConfiguration;

public interface ExerciseConfigurationRepository extends JpaRepository<ExerciseConfiguration, Long> {
}