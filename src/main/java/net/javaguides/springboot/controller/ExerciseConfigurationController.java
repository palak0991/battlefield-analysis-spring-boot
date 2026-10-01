package net.javaguides.springboot.controller;

import jakarta.validation.Valid;
import net.javaguides.springboot.model.ExerciseConfiguration;
import net.javaguides.springboot.service.ExerciseConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Exercise Configuration CRUD operations.
 * Base URL: /api/exercises
 */
@RestController
@RequestMapping("/api/exercises")
public class ExerciseConfigurationController {

    private static final Logger log = LoggerFactory.getLogger(ExerciseConfigurationController.class);

    private final ExerciseConfigurationService exerciseConfigurationService;

    public ExerciseConfigurationController(ExerciseConfigurationService exerciseConfigurationService) {
        this.exerciseConfigurationService = exerciseConfigurationService;
    }

    // POST /api/exercises
    @PostMapping
    public ResponseEntity<ExerciseConfiguration> createExercise(
            @Valid @RequestBody ExerciseConfiguration exercise) {
        log.info("Request to create exercise: {}", exercise.getName());
        return new ResponseEntity<>(exerciseConfigurationService.saveExerciseConfiguration(exercise), HttpStatus.CREATED);
    }

    // GET /api/exercises
    @GetMapping
    public ResponseEntity<List<ExerciseConfiguration>> getAllExercises() {
        List<ExerciseConfiguration> exercises = exerciseConfigurationService.getAllExerciseConfigurations();
        log.debug("Returning {} exercise configurations", exercises.size());
        return ResponseEntity.ok(exercises);
    }

    // GET /api/exercises/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseConfiguration> getExerciseById(@PathVariable long id) {
        log.debug("Request to get exercise with id: {}", id);
        return ResponseEntity.ok(exerciseConfigurationService.getExerciseConfigurationById(id));
    }

    // PUT /api/exercises/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseConfiguration> updateExercise(
            @PathVariable long id,
            @Valid @RequestBody ExerciseConfiguration exercise) {
        log.info("Request to update exercise with id: {}", id);
        return ResponseEntity.ok(exerciseConfigurationService.updateExerciseConfiguration(exercise, id));
    }

    // DELETE /api/exercises/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable long id) {
        log.info("Request to delete exercise with id: {}", id);
        exerciseConfigurationService.deleteExerciseConfiguration(id);
        return ResponseEntity.ok("Exercise configuration deleted successfully");
    }
}