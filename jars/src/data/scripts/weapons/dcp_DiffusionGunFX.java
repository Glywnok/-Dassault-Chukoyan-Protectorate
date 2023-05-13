package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.MagellanUtils;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 * @author HarmfulMechanic
 * Based on scripts by Trylobot, Uomoz and Cycerin
 */
public class dcp_DiffusionGunFX implements EveryFrameWeaponEffectPlugin {
    
    private static final float FIRE_DURATION = 0.12f; // Firing cycle time
    private static final Color FLASH_COLOR = new Color(100,110,255,255);
    private static final float OFFSET = 4f;
    private static final Color PARTICLE_COLOR = new Color(100,110,255,255); // Particle color

    private float elapsed = 0f;

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (engine.isPaused()) {
            return;
        }

        if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            // explosion (frame 0 only)
            if (elapsed <= 0f) {
                Vector2f explosion_offset = MagellanUtils.translate_polar(weapon_location, OFFSET +
                                                                          ((0.05f * 100f) - 2f), weapon.getCurrAngle());
                engine.spawnExplosion(explosion_offset, ship.getVelocity(), FLASH_COLOR, 20f, 0.07f);
            }

            elapsed += amount;

            // particles
            Vector2f particle_offset = MagellanUtils.translate_polar(weapon_location, OFFSET, weapon.getCurrAngle());
            float size, speed, angle;
            Vector2f velocity;
            // more particles to start with, fewer later on
            int particle_count_this_frame = (int) (12f * (FIRE_DURATION - elapsed));
            for (int x = 0; x < particle_count_this_frame; x++) {
                size = MagellanUtils.get_random(3f, 6f);
                speed = MagellanUtils.get_random(50f, 100f);
                angle = weapon.getCurrAngle() + MagellanUtils.get_random(-60f, 60f);
                velocity = MagellanUtils.translate_polar(ship.getVelocity(), speed, angle);
                engine.addHitParticle(particle_offset, velocity, size, 1.5f, 0.6f, PARTICLE_COLOR);
            }
        } else {
            elapsed = 0f;
        }
    }
}
