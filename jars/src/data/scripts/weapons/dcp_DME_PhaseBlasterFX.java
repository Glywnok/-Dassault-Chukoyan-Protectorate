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

public class dcp_DME_PhaseBlasterFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color NEBULA_COLOR = new Color(15, 255, 135, 200);
   private static final float NEBULA_SIZE = 6.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 9.0F;
   private static final float NEBULA_DUR = 0.5F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final Color FLASH_COLOR = new Color(0, 100, 155, 255);
   private static final float OFFSET = 6.0F;
   private static final float FIRE_DURATION = 0.2F;
   private static final float PARTICLE_COUNT = 20.0F;
   private static final Color PARTICLE_COLOR = new Color(75, 255, 155, 255);
   private float elapsed = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addSwirlyNebulaParticle(proj_location, ship_velocity, NEBULA_SIZE, 9.0F, 0.1F, 0.2F, 0.5F, NEBULA_COLOR, true);
      engine.addSmoothParticle(proj_location, ship_velocity, NEBULA_SIZE * 4.0F, 0.75F, 0.1F, 0.25F, FLASH_COLOR);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            this.elapsed += amount;
            Vector2f particle_offset = DCPUtils.translate_polar(weapon_location, 2.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(20.0F * (0.2F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DCPUtils.get_random(3.0F, 5.0F);
               float speed = DCPUtils.get_random(5.0F, 250.0F);
               float angle = weapon.getCurrAngle() + DCPUtils.get_random(-6.0F, 6.0F);
               Vector2f velocity = DCPUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.6F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
