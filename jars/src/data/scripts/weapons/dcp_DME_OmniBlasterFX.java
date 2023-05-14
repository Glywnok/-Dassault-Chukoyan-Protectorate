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

public class dcp_DME_OmniBlasterFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_CORE_COLOR = new Color(125, 100, 255, 175);
   private static final Color FLASH_FRINGE_COLOR = new Color(75, 60, 255, 125);
   private static final float FLASH_SIZE = 40.0F;
   private static final float FLASH_DUR = 0.3F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f loc = proj.getLocation();
      Vector2f vel = proj.getVelocity();
      int shotCount1 = 4;

      for(int j = 0; j < shotCount1; ++j) {
         Vector2f randomVel = MathUtils.getRandomPointOnCircumference((Vector2f)null, MathUtils.getRandomNumberInRange(30.0F, 60.0F));
         randomVel.x += vel.x;
         randomVel.y += vel.y;
         engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "istl_omniblaster_sub", loc, proj.getFacing(), randomVel);
      }

      int shotCount2 = 1;

      for(int j = 0; j < shotCount2; ++j) {
         engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "istl_omniblaster_core", loc, proj.getFacing(), (Vector2f)null);
      }

      engine.removeEntity(proj);
      ShipAPI ship = weapon.getShip();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_CORE_COLOR, 20.0F, 0.18F);
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_FRINGE_COLOR, 40.0F, 0.3F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
