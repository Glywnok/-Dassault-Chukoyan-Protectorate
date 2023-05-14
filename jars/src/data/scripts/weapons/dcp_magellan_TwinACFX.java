package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_TwinACFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_CORE = new Color(25, 170, 245, 125);
   private static final Color FLASH_FRINGE = new Color(125, 215, 245, 100);
   private static final float FLASH_SIZE = 15.0F;
   private static final float FLASH_DUR = 0.2F;
   private static final float CHARGE_PARTICLE_BRIGHTNESS = 1.0F;
   private static final float CHARGE_PARTICLE_SIZE_MAX = 7.0F;
   private static final float CHARGE_PARTICLE_SIZE_MIN = 3.0F;
   private static final float CHARGE_PARTICLE_ANGLE_SPREAD = 360.0F;
   private static final float CHARGE_PARTICLE_COUNT_FACTOR = 12.0F;
   private static final float CHARGE_PARTICLE_DISTANCE_MAX = 30.0F;
   private static final float CHARGE_PARTICLE_DISTANCE_MIN = 5.0F;
   private static final float CHARGE_PARTICLE_DURATION = 0.3F;
   private float last_charge_level = 0.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      float weapon_facing = weapon.getCurrAngle();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      float charge_level = weapon.getChargeLevel();
      if (charge_level > this.last_charge_level && weapon.isFiring()) {
         int particle_count = (int)(12.0F * charge_level);

         for(int i = 0; i < particle_count; ++i) {
            float distance = MathUtils.getRandomNumberInRange(5.0F, 30.0F);
            float size = MathUtils.getRandomNumberInRange(3.0F, 7.0F);
            float angle = MathUtils.getRandomNumberInRange(-180.0F, 180.0F);
            Vector2f spawn_location = MathUtils.getPointOnCircumference(proj_location, distance, angle + weapon_facing);
            float speed = distance / 0.3F;
            Vector2f particle_velocity = MathUtils.getPointOnCircumference(ship_velocity, speed, 180.0F + angle + weapon_facing);
            engine.addHitParticle(spawn_location, particle_velocity, size, 1.0F, 0.3F, FLASH_CORE);
         }
      }

      engine.addSmoothParticle(proj_location, ship_velocity, 45.0F, 1.0F, 0.3F, 0.15F, FLASH_CORE);
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_FRINGE, 15.0F, 0.2F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
