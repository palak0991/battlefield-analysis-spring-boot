package net.javaguides.springboot.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.ExerciseConfiguration;
import net.javaguides.springboot.repository.ExerciseConfigurationRepository;
import net.javaguides.springboot.service.ExerciseConfigurationService;

@Service
public class ExerciseConfigurationServiceImpl implements ExerciseConfigurationService {

    private ExerciseConfigurationRepository exerciseConfigurationRepository;

    public ExerciseConfigurationServiceImpl(ExerciseConfigurationRepository exerciseConfigurationRepository) {
        this.exerciseConfigurationRepository = exerciseConfigurationRepository;
    }

    @Override
    public ExerciseConfiguration saveExerciseConfiguration(ExerciseConfiguration exerciseConfiguration) {
        return exerciseConfigurationRepository.save(exerciseConfiguration);
    }

    @Override
    public List<ExerciseConfiguration> getAllExerciseConfigurations() {
        return exerciseConfigurationRepository.findAll();
    }

    @Override
    public ExerciseConfiguration getExerciseConfigurationById(long id) {
        return exerciseConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("ExerciseConfiguration", "Id", id));
    }

    @Override
    public ExerciseConfiguration updateExerciseConfiguration(ExerciseConfiguration exerciseConfiguration, long id) {
        ExerciseConfiguration existing = exerciseConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("ExerciseConfiguration", "Id", id));
        existing.setName(exerciseConfiguration.getName());
        existing.setLocation(exerciseConfiguration.getLocation());
        existing.setDate(exerciseConfiguration.getDate());
        existing.setCommander(exerciseConfiguration.getCommander());
        exerciseConfigurationRepository.save(existing);
        return existing;
    }

    @Override
    public void deleteExerciseConfiguration(long id) {
        exerciseConfigurationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("ExerciseConfiguration", "Id", id));
        exerciseConfigurationRepository.deleteById(id);
    }
}