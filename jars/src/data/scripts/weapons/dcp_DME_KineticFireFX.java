package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DCPUtils;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_KineticFireFX implements EveryFrameWeaponEffectPlugin {
   private static final float FIRE_DURATION = 0.3F;
   private static final Color FLASH_COLOR = new Color(75, 125, 255, 200);
   private static final float OFFSET = 0.0F;
   private static final Color PARTICLE_COLOR = new Color(125, 175, 255, 255);
   private float elapsed = 0.0F;

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            Vector2f particle_offset;
            if (this.elapsed <= 0.0F) {
               particle_offset = DCPUtils.translate_polar(weapon_location, 3.0F, weapon.getCurrAngle());
               engine.spawnExplosion(particle_offset, ship.getVelocity(), FLASH_COLOR, 75.0F, 0.25F);
            }

            this.elapsed += amount;
            particle_offset = DCPUtils.translate_polar(weapon_location, 0.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(15.0F * (0.3F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DCPUtils.get_random(3.0F, 15.0F);
               float speed = DCPUtils.get_random(150.0F, 300.0F);
               float angle = weapon.getCurrAngle() + DCPUtils.get_random(-7.0F, 7.0F);
               Vector2f velocity = DCPUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.6F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
