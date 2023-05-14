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

public class dcp_magellan_QuenchCannonFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color DIM_COLOR = new Color(200, 200, 255, 155);
   private static final Color BRIGHT_COLOR = new Color(175, 175, 225, 225);
   private static final float FLASH_SIZE = 60.0F;
   private static final float NEBULA_SIZE = 9.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 11.0F;
   private static final float NEBULA_DUR = 0.75F;
   private static final float NEBULA_RAMPUP = 0.15F;
   private static final float OFFSET = 30.0F;
   private static final float FIRE_DURATION = 0.12F;
   private static final float PARTICLE_COUNT = 20.0F;
   private float elapsed = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addNebulaParticle(proj_location, ship_velocity, NEBULA_SIZE, 11.0F, 0.15F, 0.2F, 0.75F, BRIGHT_COLOR, true);
      engine.spawnExplosion(proj_location, ship_velocity, DIM_COLOR, 60.0F, 0.25F);
      engine.addHitParticle(proj_location, ship_velocity, 90.0F, 1.0F, 0.125F, BRIGHT_COLOR);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            this.elapsed += amount;
            Vector2f particle_offset = DMEUtils.translate_polar(weapon_location, 30.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(20.0F * (0.12F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DMEUtils.get_random(3.0F, 6.0F);
               float speed = DMEUtils.get_random(140.0F, 225.0F);
               float angle = weapon.getCurrAngle() + DMEUtils.get_random(-80.0F, 80.0F);
               Vector2f velocity = DMEUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.6F, DIM_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
