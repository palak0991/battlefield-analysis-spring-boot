package net.javaguides.springboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;

@Data
@Entity
@Table(name = "exercise_configuration")
public class ExerciseConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "exercise_date")
    private String date;

    @Column(name = "commander")
    private String commander;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PlayerConfiguration> players;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SystemConfiguration> systems;
}