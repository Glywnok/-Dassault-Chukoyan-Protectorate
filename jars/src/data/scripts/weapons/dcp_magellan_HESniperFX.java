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

public class dcp_magellan_HESniperFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(255, 225, 165, 225);
   private static final float FLASH_SIZE = 24.0F;
   private static final float FLASH_DUR = 0.2F;
   private static final float OFFSET = 31.0F;
   private static final float FIRE_DURATION = 0.24F;
   private static final float PARTICLE_COUNT = 20.0F;
   private static final Color PARTICLE_COLOR = new Color(255, 225, 165, 155);
   private float elapsed = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      float ship_facing = ship.getFacing();
      Vector2f ship_velocity = ship.getVelocity();
      MathUtils.getPointOnCircumference(weapon_location, 31.0F, ship_facing);
      Vector2f explosion_offset = DMEUtils.translate_polar(weapon_location, 34.0F, weapon.getCurrAngle());
      engine.spawnExplosion(explosion_offset, ship.getVelocity(), FLASH_COLOR, 24.0F, 0.2F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            this.elapsed += amount;
            Vector2f particle_offset = DMEUtils.translate_polar(weapon_location, 31.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(20.0F * (0.24F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DMEUtils.get_random(3.0F, 6.0F);
               float speed = DMEUtils.get_random(30.0F, 150.0F);
               float angle = weapon.getCurrAngle() + DMEUtils.get_random(-4.0F, 4.0F);
               Vector2f velocity = DMEUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.6F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
