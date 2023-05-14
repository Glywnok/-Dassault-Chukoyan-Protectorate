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

public class dcp_magellan_ClusterACFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_CORE_COLOR = new Color(200, 200, 255, 125);
   private static final Color FLASH_FRINGE_COLOR = new Color(200, 200, 255, 75);
   private static final float FLASH_SIZE = 12.0F;
   private static final float FLASH_DUR = 0.2F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f loc = proj.getLocation();
      Vector2f proj_vel = proj.getVelocity();
      Vector2f ship_vel = proj.getWeapon().getShip().getVelocity();
      int shotCount = MathUtils.getRandomNumberInRange(9, 12);

      Vector2f ship_velocity;
      for(int j = 0; j < shotCount; ++j) {
         ship_velocity = MathUtils.getRandomPointOnCircumference((Vector2f)null, MathUtils.getRandomNumberInRange(2.0F * (float)shotCount, 5.0F * (float)shotCount));
         ship_velocity.x += proj_vel.x + ship_vel.x;
         ship_velocity.y += proj_vel.y + ship_vel.y;
         engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "magellan_autoshotgun_sub", loc, proj.getFacing(), ship_velocity);
      }

      engine.removeEntity(proj);
      ShipAPI ship = weapon.getShip();
      ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_CORE_COLOR, 6.0F, 0.120000005F);
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_FRINGE_COLOR, 12.0F, 0.2F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
