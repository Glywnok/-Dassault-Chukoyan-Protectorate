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

public class dcp_magellan_BonecrackerFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(255, 235, 200, 75);
   private static final float FLASH_SIZE = 4.0F;
   private static final float FLASH_DUR = 0.15F;
   private static final float OFFSET = 19.0F;
   private static final float FIRE_DURATION = 0.2F;
   private static final float PARTICLE_COUNT = 18.0F;
   private static final Color PARTICLE_COLOR = new Color(255, 235, 200, 155);
   private float elapsed = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f loc = proj.getLocation();
      Vector2f proj_vel = proj.getVelocity();
      Vector2f ship_vel = proj.getWeapon().getShip().getVelocity();
      int shotCount1 = 6;

      Vector2f weapon_location;
      for(int j = 0; j < shotCount1; ++j) {
         weapon_location = MathUtils.getRandomPointOnCircumference((Vector2f)null, MathUtils.getRandomNumberInRange(18.0F, 24.0F));
         weapon_location.x += proj_vel.x + ship_vel.x;
         weapon_location.y += proj_vel.y + ship_vel.y;
         engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "magellan_bonecracker_sub", loc, proj.getFacing(), weapon_location);
      }

      int shotCount2 = 1;

      for(int j = 0; j < shotCount2; ++j) {
         proj_vel.x += ship_vel.x;
         proj_vel.y += ship_vel.y;
         engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "magellan_bonecracker_core", loc, proj.getFacing(), proj_vel);
      }

      engine.removeEntity(proj);
      weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      Vector2f explosion_offset = DMEUtils.translate_polar(weapon_location, 26.0F, weapon.getCurrAngle());
      Vector2f explosion_offset2 = DMEUtils.translate_polar(weapon_location, 22.0F, weapon.getCurrAngle());
      engine.spawnExplosion(explosion_offset, ship.getVelocity(), FLASH_COLOR, 4.0F, 0.15F);
      engine.spawnExplosion(explosion_offset2, ship.getVelocity(), PARTICLE_COLOR, 2.0F, 0.09F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            this.elapsed += amount;
            Vector2f particle_offset = DMEUtils.translate_polar(weapon_location, 19.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(18.0F * (0.2F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DMEUtils.get_random(3.0F, 5.0F);
               float speed = DMEUtils.get_random(20.0F, 80.0F);
               float angle = weapon.getCurrAngle() + DMEUtils.get_random(-18.0F, 18.0F);
               Vector2f velocity = DMEUtils.translate_polar(ship.getVelocity(), speed, angle);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.6F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

      }
   }
}
