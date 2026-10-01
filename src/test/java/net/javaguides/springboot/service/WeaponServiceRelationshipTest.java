package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.ExerciseConfiguration;
import net.javaguides.springboot.model.PlayerConfiguration;
import net.javaguides.springboot.model.WeaponConfiguration;
import net.javaguides.springboot.repository.WeaponConfigurationRepository;
import net.javaguides.springboot.service.impl.WeaponConfigurationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WeaponConfigurationServiceImpl.
 *
 * Key focus: verifying that the FK relationship (weapon → player) is correctly
 * updated when updateWeaponConfiguration() is called.
 * This was a bug in the original code — these tests document and verify the fix.
 */
@ExtendWith(MockitoExtension.class)
class WeaponServiceRelationshipTest {

    @Mock
    private WeaponConfigurationRepository weaponConfigurationRepository;

    @InjectMocks
    private WeaponConfigurationServiceImpl weaponService;

    private WeaponConfiguration existingWeapon;
    private PlayerConfiguration playerA;
    private PlayerConfiguration playerB;

    @BeforeEach
    void setUp() {
        playerA = new PlayerConfiguration();
        playerA.setId(10L);
        playerA.setPlayerName("Alpha Squad");

        playerB = new PlayerConfiguration();
        playerB.setId(20L);
        playerB.setPlayerName("Beta Squad");

        existingWeapon = new WeaponConfiguration();
        existingWeapon.setId(1L);
        existingWeapon.setWeaponType("Rifle");
        existingWeapon.setRange(500);
        existingWeapon.setTotal(10);
        existingWeapon.setCurrent(8);
        existingWeapon.setPlayer(playerA); // originally assigned to Player A
    }

    @Test
    @DisplayName("updateWeaponConfiguration should update the player FK (relationship fix)")
    void updateWeapon_ShouldUpdatePlayerRelationship() {
        // Prepare update request: same weapon data but reassigned to Player B
        WeaponConfiguration updateRequest = new WeaponConfiguration();
        updateRequest.setWeaponType("Rifle");
        updateRequest.setRange(600);
        updateRequest.setTotal(10);
        updateRequest.setCurrent(7);
        updateRequest.setPlayer(playerB); // changing to Player B

        when(weaponConfigurationRepository.findById(1L)).thenReturn(Optional.of(existingWeapon));
        when(weaponConfigurationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        WeaponConfiguration result = weaponService.updateWeaponConfiguration(updateRequest, 1L);

        // The player FK should have been updated to Player B
        assertThat(result.getPlayer()).isNotNull();
        assertThat(result.getPlayer().getId()).isEqualTo(20L);
        assertThat(result.getPlayer().getPlayerName()).isEqualTo("Beta Squad");
        assertThat(result.getRange()).isEqualTo(600);
    }

    @Test
    @DisplayName("updateWeaponConfiguration should allow clearing the player assignment (set to null)")
    void updateWeapon_ShouldAllowUnassigning() {
        WeaponConfiguration updateRequest = new WeaponConfiguration();
        updateRequest.setWeaponType("Rifle");
        updateRequest.setRange(500);
        updateRequest.setTotal(10);
        updateRequest.setCurrent(8);
        updateRequest.setPlayer(null); // unassign

        when(weaponConfigurationRepository.findById(1L)).thenReturn(Optional.of(existingWeapon));
        when(weaponConfigurationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        WeaponConfiguration result = weaponService.updateWeaponConfiguration(updateRequest, 1L);

        assertThat(result.getPlayer()).isNull();
    }

    @Test
    @DisplayName("updateWeaponConfiguration should throw ResourceNotFoundException for nonexistent weapon")
    void updateWeapon_NotFound() {
        when(weaponConfigurationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> weaponService.updateWeaponConfiguration(existingWeapon, 999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
