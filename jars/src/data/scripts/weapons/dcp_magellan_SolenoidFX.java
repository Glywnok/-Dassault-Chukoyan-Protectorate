package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_SolenoidFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(200, 200, 255, 155);

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      Vector2f proj_location = proj.getLocation();
      Vector2f ship_velocity = ship.getVelocity();
      String projid = proj.getProjectileSpecId();
      byte var11 = -1;
      switch(projid.hashCode()) {
      case -236207448:
         if (projid.equals("dcp_magellan_bigsolenoid_shot")) {
            var11 = 1;
         }
         break;
      case 720592249:
         if (projid.equals("dcp_magellan_lilsolenoid_shot")) {
            var11 = 0;
         }
      }

      float flash_size;
      float flash_dur;
      switch(var11) {
      case 0:
         flash_size = 30.0F;
         flash_dur = 0.2F;
         break;
      case 1:
         flash_size = 40.0F;
         flash_dur = 0.24F;
         break;
      default:
         return;
      }

      engine.spawnExplosion(proj_location, ship_velocity, FLASH_COLOR, flash_size, flash_dur);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
