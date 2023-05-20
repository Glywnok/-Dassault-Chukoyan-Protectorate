package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DCPUtils;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_HEShotgunFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(255, 225, 165, 125);
   private static final float FLASH_SIZE = 32.0F;
   private static final float FLASH_DUR = 0.24F;
   private static final float OFFSET = 27.0F;
   private static final float FIRE_DURATION = 0.2F;
   private static final float PARTICLE_COUNT = 20.0F;
   private static final Color PARTICLE_COLOR = new Color(255, 225, 165, 175);
   private float elapsed = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f loc = proj.getLocation();
      Vector2f proj_vel = proj.getVelocity();
      Vector2f ship_vel = proj.getWeapon().getShip().getVelocity();
      int shotCount1 = 5;

      Vector2f weapon_location;
      for(int j = 0; j < shotCount1; ++j) {
         weapon_location = MathUtils.getRandomPointOnCircumference((Vector2f)null, MathUtils.getRandomNumberInRange(24.0F, 40.0F));
         weapon_location.x += proj_vel.x + ship_vel.x;
         weapon_location.y += proj_vel.y + ship_vel.y;
         engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "dcp_magellan_beehive_sub", loc, proj.getFacing(), weapon_location);
      }

      int shotCount2 = 1;

      for(int j = 0; j < shotCount2; ++j) {
         proj_vel.x += ship_vel.x;
         proj_vel.y += ship_vel.y;
         engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "dcp_magellan_beehive_core", loc, proj.getFacing(), proj_vel);
      }

      engine.removeEntity(proj);
      weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      Vector2f explosion_offset = DCPUtils.translate_polar(weapon_location, 34.0F, weapon.getCurrAngle());
      Vector2f explosion_offset2 = DCPUtils.translate_polar(weapon_location, 30.0F, weapon.getCurrAngle());
      engine.spawnExplosion(explosion_offset, ship.getVelocity(), FLASH_COLOR, 32.0F, 0.24F);
      engine.spawnExplosion(explosion_offset2, ship.getVelocity(), PARTICLE_COLOR, 16.0F, 0.14400001F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            this.elapsed += amount;
            Vector2f particle_offset = DCPUtils.translate_polar(weapon_location, 27.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(20.0F * (0.2F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DCPUtils.get_random(3.0F, 6.0F);
               float speed = DCPUtils.get_random(15.0F, 120.0F);
               float angle = weapon.getCurrAngle() + DCPUtils.get_random(-15.0F, 15.0F);
               Vector2f velocity = DCPUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.6F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
