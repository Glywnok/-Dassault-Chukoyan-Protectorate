package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DMEUtils;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_ShockArtilleryFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final float OFFSET = 18.0F;
   private static final Color FLASH_COLOR = new Color(140, 125, 255, 255);
   private static final float FLASH_SIZE = 60.0F;
   private static final float FLASH_DUR = 0.2F;
   private static final float FIRE_DURATION = 0.24F;
   private static final float PARTICLE_COUNT = 9.0F;
   private static final Color PARTICLE_COLOR = new Color(75, 100, 255, 200);
   private float elapsed = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      float ship_facing = ship.getFacing();
      Vector2f ship_velocity = ship.getVelocity();
      MathUtils.getPointOnCircumference(weapon_location, 18.0F, ship_facing);
      Vector2f explosion_offset = DMEUtils.translate_polar(weapon_location, 25.0F, weapon.getCurrAngle());
      engine.spawnExplosion(explosion_offset, ship.getVelocity(), FLASH_COLOR, 60.0F, 0.2F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            this.elapsed += amount;
            Vector2f particle_offset = DMEUtils.translate_polar(weapon_location, 18.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(9.0F * (0.24F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DMEUtils.get_random(3.0F, 9.0F);
               float speed = DMEUtils.get_random(75.0F, 150.0F);
               float angle = weapon.getCurrAngle() + DMEUtils.get_random(-45.0F, 45.0F);
               Vector2f velocity = DMEUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.6F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
