package net.javaguides.springboot.service.impl;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.ExerciseConfiguration;
import net.javaguides.springboot.repository.ExerciseConfigurationRepository;
import net.javaguides.springboot.service.ExerciseConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for Exercise Configuration business logic.
 */
@Service
public class ExerciseConfigurationServiceImpl implements ExerciseConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseConfigurationServiceImpl.class);

    private final ExerciseConfigurationRepository exerciseConfigurationRepository;

    public ExerciseConfigurationServiceImpl(ExerciseConfigurationRepository exerciseConfigurationRepository) {
        this.exerciseConfigurationRepository = exerciseConfigurationRepository;
    }

    @Override
    public ExerciseConfiguration saveExerciseConfiguration(ExerciseConfiguration exercise) {
        log.debug("Saving exercise: {}", exercise.getName());
        return exerciseConfigurationRepository.save(exercise);
    }

    @Override
    public List<ExerciseConfiguration> getAllExerciseConfigurations() {
        return exerciseConfigurationRepository.findAll();
    }

    @Override
    public ExerciseConfiguration getExerciseConfigurationById(long id) {
        return exerciseConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseConfiguration", "Id", id));
    }

    @Override
    public ExerciseConfiguration updateExerciseConfiguration(ExerciseConfiguration exercise, long id) {
        ExerciseConfiguration existing = exerciseConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseConfiguration", "Id", id));

        existing.setName(exercise.getName());
        existing.setLocation(exercise.getLocation());
        existing.setDate(exercise.getDate());
        existing.setCommander(exercise.getCommander());

        log.debug("Updating exercise id: {}", id);
        return exerciseConfigurationRepository.save(existing);
    }

    @Override
    public void deleteExerciseConfiguration(long id) {
        exerciseConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseConfiguration", "Id", id));
        log.debug("Deleting exercise id: {}", id);
        exerciseConfigurationRepository.deleteById(id);
    }
}