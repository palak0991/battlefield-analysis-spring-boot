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
 * WeaponConfiguration — represents a weapon type and its stock levels.
 *
 * Relationships:
 *   - Many weapons can be assigned to one Player (player_id FK).
 *   - The @ManyToOne player relationship is included in JSON so the frontend
 *     can display which player a weapon is assigned to.
 *
 * Table: weapon_configuration
 */
@Data
@Entity
@Table(name = "weapon_configuration")
public class WeaponConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Weapon type is required")
    @Size(max = 100, message = "Weapon type must not exceed 100 characters")
    @Column(name = "weapon_type", nullable = false)
    private String weaponType;

    @Min(value = 0, message = "Range must be zero or greater")
    @Column(name = "range_value")
    private int range;

    @Min(value = 0, message = "Total must be zero or greater")
    @Column(name = "total")
    private int total;

    @Min(value = 0, message = "Current count must be zero or greater")
    @Column(name = "current_count")
    private int current;

    /**
     * The player this weapon is assigned to. May be null (unassigned).
     * Included in JSON (no @JsonIgnore) so the dashboard can show the assignment.
     */
    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerConfiguration player;
}