package net.javaguides.springboot.service;

import java.util.List;

import net.javaguides.springboot.model.ExerciseConfiguration;

public interface ExerciseConfigurationService {
    ExerciseConfiguration saveExerciseConfiguration(ExerciseConfiguration exerciseConfiguration);
    List<ExerciseConfiguration> getAllExerciseConfigurations();
    ExerciseConfiguration getExerciseConfigurationById(long id);
    ExerciseConfiguration updateExerciseConfiguration(ExerciseConfiguration exerciseConfiguration, long id);
    void deleteExerciseConfiguration(long id);
}