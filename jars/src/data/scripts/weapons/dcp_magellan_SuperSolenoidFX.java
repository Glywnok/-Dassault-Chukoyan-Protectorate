package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;

public class dcp_magellan_SuperSolenoidFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR_1 = new Color(125, 215, 245, 100);
   private static final Color FLASH_COLOR_2 = new Color(25, 170, 245, 125);

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      String projid = proj.getProjectileSpecId();
      byte var9 = -1;
      switch(projid.hashCode()) {
      case -124460084:
         if (projid.equals("magellan_supersolenoid_sm_shot")) {
            var9 = 0;
         }
         break;
      case 372238349:
         if (projid.equals("magellan_supersolenoid_shot")) {
            var9 = 1;
         }
      }

      float flash_size;
      float flash_dur;
      switch(var9) {
      case 0:
         flash_size = 32.0F;
         flash_dur = 0.2F;
         break;
      case 1:
         flash_size = 60.0F;
         flash_dur = 0.3F;
         break;
      default:
         return;
      }

      engine.spawnExplosion(proj.getLocation(), ship.getVelocity(), FLASH_COLOR_1, flash_size, flash_dur);
      engine.spawnExplosion(proj.getLocation(), ship.getVelocity(), FLASH_COLOR_2, flash_size / 2.0F, flash_dur * 0.6F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
