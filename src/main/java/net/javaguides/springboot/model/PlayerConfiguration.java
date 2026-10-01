package net.javaguides.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * PlayerConfiguration — represents a participant in a battlefield exercise.
 *
 * Relationships:
 *   - Many players belong to one Exercise (exercise_id FK).
 *   - One player can own many Weapons (weapons are assigned to a player).
 *
 * @ManyToOne exercise: FK exercise_id in player_configuration table.
 *   @ToString.Exclude prevents Lombok's toString() from triggering lazy loading
 *   and creating infinite recursion in logs.
 *
 * @OneToMany weapons: A player "owns" their weapons.
 *   @JsonIgnore prevents serialization loop (Player → Weapons → Player).
 *   CascadeType.ALL: deleting a player also removes their weapons.
 *
 * Table: player_configuration
 */
@Data
@Entity
@Table(name = "player_configuration")
public class PlayerConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Player name is required")
    @Size(max = 100, message = "Player name must not exceed 100 characters")
    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Size(max = 100, message = "Role must not exceed 100 characters")
    @Column(name = "role")
    private String role;

    @Size(max = 100, message = "Unit must not exceed 100 characters")
    @Column(name = "unit")
    private String unit;

    @Min(value = 0, message = "Total login time must be zero or greater")
    @Column(name = "total_login_time")
    private int totalLoginTime;

    /**
     * The exercise this player is assigned to. May be null (unassigned).
     * @ToString.Exclude: prevents Lombok toString from traversing back into Exercise.
     */
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @ToString.Exclude
    private ExerciseConfiguration exercise;

    /**
     * Weapons assigned to this player.
     * @JsonIgnore: prevents Player → Weapon → Player infinite JSON loop.
     * CascadeType.ALL: removing a player removes their associated weapons.
     */
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<WeaponConfiguration> weapons;
}