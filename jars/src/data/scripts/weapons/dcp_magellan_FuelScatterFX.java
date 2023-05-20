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

public class dcp_magellan_FuelScatterFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color BRIGHT_COLOR = new Color(173, 255, 47, 255);
   private static final Color DIM_COLOR = new Color(173, 255, 47, 155);
   private static final float FLASH_SIZE = 36.0F;
   private static final float FLASH_DUR = 0.3F;
   private static final float OFFSET = 22.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f loc = proj.getLocation();
      Vector2f proj_vel = proj.getVelocity();
      Vector2f ship_vel = proj.getWeapon().getShip().getVelocity();
      int shotCount = MathUtils.getRandomNumberInRange(9, 12);

      for(int j = 0; j < shotCount; ++j) {
         Vector2f randomVel = MathUtils.getRandomPointOnCircumference((Vector2f)null, MathUtils.getRandomNumberInRange(5.0F * (float)shotCount, 15.0F * (float)shotCount));
         randomVel.x += proj_vel.x + ship_vel.x;
         randomVel.y += proj_vel.y + ship_vel.y;
         engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "dcp_magellan_fuelscatter_sub", loc, proj.getFacing(), randomVel);
      }

      engine.removeEntity(proj);
      Vector2f weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      Vector2f explosion_offset = DCPUtils.translate_polar(weapon_location, 31.0F, weapon.getCurrAngle());
      Vector2f explosion_offset2 = DCPUtils.translate_polar(weapon_location, 25.0F, weapon.getCurrAngle());
      engine.spawnExplosion(explosion_offset, ship.getVelocity(), BRIGHT_COLOR, 36.0F, 0.3F);
      engine.spawnExplosion(explosion_offset2, ship.getVelocity(), DIM_COLOR, 18.0F, 0.18F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
