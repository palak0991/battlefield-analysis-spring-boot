package net.javaguides.springboot.model;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "system_configuration")
public class SystemConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "resource_type", nullable = false)
    private String resourceType;

    @Column(name = "total")
    private int total;

    @Column(name = "current_count")
    private int current;
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExerciseConfiguration exercise;
}