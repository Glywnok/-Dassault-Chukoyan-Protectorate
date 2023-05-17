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

public class dcp_magellan_FusbombFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(240, 30, 90, 255);
   private static final float FLASH_SIZE = 50.0F;
   private static final float FLASH_DUR = 0.25F;
   private static final float OFFSET = 2.0F;
   private static final float FIRE_DURATION = 0.3F;
   private static final float PARTICLE_COUNT = 20.0F;
   private static final Color PARTICLE_COLOR = new Color(240, 30, 90, 155);
   private float elapsed = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      engine.spawnExplosion(proj.getLocation(), ship.getVelocity(), FLASH_COLOR, 50.0F, 0.25F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            this.elapsed += amount;
            Vector2f particle_offset = DCPUtils.translate_polar(weapon_location, 2.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(20.0F * (0.3F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DCPUtils.get_random(3.0F, 6.0F);
               float speed = DCPUtils.get_random(50.0F, 150.0F);
               float angle = weapon.getCurrAngle() + DCPUtils.get_random(-5.0F, 5.0F);
               Vector2f velocity = DCPUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.6F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
