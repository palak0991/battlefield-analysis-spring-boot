package net.javaguides.springboot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.javaguides.springboot.model.ExerciseConfiguration;
import net.javaguides.springboot.service.ExerciseConfigurationService;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseConfigurationController {

    private ExerciseConfigurationService exerciseConfigurationService;

    public ExerciseConfigurationController(ExerciseConfigurationService exerciseConfigurationService) {
        this.exerciseConfigurationService = exerciseConfigurationService;
    }

    @PostMapping()
    public ResponseEntity<ExerciseConfiguration> saveExerciseConfiguration(@RequestBody ExerciseConfiguration exerciseConfiguration) {
        return new ResponseEntity<>(exerciseConfigurationService.saveExerciseConfiguration(exerciseConfiguration), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ExerciseConfiguration> getAllExerciseConfigurations() {
        return exerciseConfigurationService.getAllExerciseConfigurations();
    }

    @GetMapping("{id}")
    public ResponseEntity<ExerciseConfiguration> getExerciseConfigurationById(@PathVariable("id") long id) {
        return new ResponseEntity<>(exerciseConfigurationService.getExerciseConfigurationById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ExerciseConfiguration> updateExerciseConfiguration(@PathVariable("id") long id, @RequestBody ExerciseConfiguration exerciseConfiguration) {
        return new ResponseEntity<>(exerciseConfigurationService.updateExerciseConfiguration(exerciseConfiguration, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteExerciseConfiguration(@PathVariable("id") long id) {
        exerciseConfigurationService.deleteExerciseConfiguration(id);
        return new ResponseEntity<>("Exercise configuration deleted successfully!", HttpStatus.OK);
    }
}