package net.javaguides.springboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * SystemConfiguration — represents a resource or system type and its allocation.
 * Examples: vehicles, radios, fuel supplies, medical units.
 *
 * Relationships:
 *   - Many systems can be allocated to one Exercise (exercise_id FK).
 *
 * Table: system_configuration
 */
@Data
@Entity
@Table(name = "system_configuration")
public class SystemConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Resource type is required")
    @Size(max = 100, message = "Resource type must not exceed 100 characters")
    @Column(name = "resource_type", nullable = false)
    private String resourceType;

    @Min(value = 0, message = "Total must be zero or greater")
    @Column(name = "total")
    private int total;

    @Min(value = 0, message = "Current allocation must be zero or greater")
    @Column(name = "current_count")
    private int current;

    /**
     * The exercise this system resource is allocated to. May be null (unallocated).
     */
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseConfiguration exercise;
}