package net.javaguides.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * ExerciseConfiguration — represents a battlefield exercise or mission.
 *
 * Relationships:
 *   - One exercise can have many Players (players are assigned to exercises).
 *   - One exercise can have many Systems (resource systems are assigned to exercises).
 *
 * Cascade: ALL is used here intentionally — if an exercise is deleted, its associated
 *   players and system allocations are also removed, because they have no meaning
 *   without the exercise they belong to.
 *
 * @JsonIgnore on collections prevents infinite recursion in JSON serialization
 *   (Exercise → Players → Exercise → ... would loop forever without it).
 *
 * Table: exercise_configuration
 */
@Data
@Entity
@Table(name = "exercise_configuration")
public class ExerciseConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Exercise name is required")
    @Size(max = 150, message = "Exercise name must not exceed 150 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 200, message = "Location must not exceed 200 characters")
    @Column(name = "location")
    private String location;

    // Stored as a String for simplicity. Future improvement: use LocalDate.
    @Column(name = "exercise_date")
    private String date;

    @Size(max = 100, message = "Commander name must not exceed 100 characters")
    @Column(name = "commander")
    private String commander;

    /**
     * Players assigned to this exercise.
     * @JsonIgnore prevents serialization of this collection to avoid infinite recursion.
     * CascadeType.ALL: deleting an exercise removes its players too.
     */
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PlayerConfiguration> players;

    /**
     * System resources allocated to this exercise.
     * Same cascade and serialization reasoning as above.
     */
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SystemConfiguration> systems;
}