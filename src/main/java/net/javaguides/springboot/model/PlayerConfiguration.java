package net.javaguides.springboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

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
}