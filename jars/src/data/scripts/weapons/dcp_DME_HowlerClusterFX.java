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

public class dcp_DME_HowlerClusterFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(255, 95, 50, 255);
   private static final float FLASH_SIZE = 4.0F;
   private static final float FLASH_DUR = 0.12F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f loc = proj.getLocation();
      Vector2f vel = proj.getVelocity();
      int shotCount = 5;

      Vector2f ship_velocity;
      for(int j = 0; j < shotCount; ++j) {
         ship_velocity = MathUtils.getRandomPointOnCircumference((Vector2f)null, MathUtils.getRandomNumberInRange(25.0F, 75.0F));
         ship_velocity.x += vel.x;
         ship_velocity.y += vel.y;
         engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "dcp_DME_howler_lbx", loc, proj.getFacing(), ship_velocity);
      }

      engine.removeEntity(proj);
      ShipAPI ship = weapon.getShip();
      ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_COLOR, 4.0F, 0.12F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
