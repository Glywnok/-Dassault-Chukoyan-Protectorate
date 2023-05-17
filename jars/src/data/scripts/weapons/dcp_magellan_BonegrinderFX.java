package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DCPUtils;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_BonegrinderFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(255, 235, 200, 255);
   private static final float FLASH_SIZE = 1.5F;
   private static final float FLASH_DUR = 0.1F;
   private static final float OFFSET = 24.0F;
   private static final float FIRE_DURATION = 0.15F;
   private static final float PARTICLE_COUNT = 10.0F;
   private static final Color PARTICLE_COLOR = new Color(255, 235, 200, 155);
   private float elapsed = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      Vector2f explosion_offset = DCPUtils.translate_polar(weapon_location, 33.0F, weapon.getCurrAngle());
      Vector2f explosion_offset2 = DCPUtils.translate_polar(weapon_location, 27.0F, weapon.getCurrAngle());
      engine.spawnExplosion(explosion_offset, ship.getVelocity(), PARTICLE_COLOR, 1.5F, 0.1F);
      engine.spawnExplosion(explosion_offset2, ship.getVelocity(), FLASH_COLOR, 0.75F, 0.060000002F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            this.elapsed += amount;
            Vector2f particle_offset = DCPUtils.translate_polar(weapon_location, 24.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(10.0F * (0.15F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DCPUtils.get_random(3.0F, 7.0F);
               float speed = DCPUtils.get_random(20.0F, 200.0F);
               float angle = weapon.getCurrAngle() + DCPUtils.get_random(-9.0F, 9.0F);
               Vector2f velocity = DCPUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.3F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
