package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DMEUtils;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_FuelrodFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(173, 255, 47, 255);
   private static final float FLASH_SIZE = 35.0F;
   private static final float FLASH_DUR = 0.2F;
   private static final float OFFSET = 10.0F;
   private static final float FIRE_DURATION = 0.3F;
   private static final float PARTICLE_COUNT = 24.0F;
   private static final Color PARTICLE_COLOR = new Color(173, 255, 47, 155);
   private float elapsed = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      engine.spawnExplosion(proj.getLocation(), ship.getVelocity(), FLASH_COLOR, 35.0F, 0.2F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            this.elapsed += amount;
            Vector2f particle_offset = DMEUtils.translate_polar(weapon_location, 10.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(24.0F * (0.3F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DMEUtils.get_random(3.0F, 5.0F);
               float speed = DMEUtils.get_random(5.0F, 105.0F);
               float angle = weapon.getCurrAngle() + DMEUtils.get_random(-7.0F, 7.0F);
               Vector2f velocity = DMEUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.6F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
