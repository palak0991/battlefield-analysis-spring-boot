package net.javaguides.springboot.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "player_configuration")
public class PlayerConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name = "role")
    private String role;

    @Column(name = "unit")
    private String unit;

    @Column(name = "total_login_time")
    private int totalLoginTime;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @ToString.Exclude
    private ExerciseConfiguration exercise;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<WeaponConfiguration> weapons;
}