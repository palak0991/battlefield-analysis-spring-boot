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
@Table(name = "weapon_configuration")
public class WeaponConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "weapon_type", nullable = false)
    private String weaponType;

    @Column(name = "range_value")
    private int range;

    @Column(name = "total")
    private int total;

    @Column(name = "current_count")
    private int current;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerConfiguration player;
}