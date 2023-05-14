package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PulserMuzzleFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(125, 100, 255, 175);
   private static final float FLASH_SIZE = 25.0F;
   private static final float FLASH_DUR = 0.12F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_COLOR, 25.0F, 0.12F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
