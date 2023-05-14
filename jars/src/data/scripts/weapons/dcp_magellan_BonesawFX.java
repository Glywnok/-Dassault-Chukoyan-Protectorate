package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_BonesawFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_BRIGHT = new Color(255, 235, 200, 255);
   private static final Color FLASH_DIM = new Color(255, 235, 200, 155);

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      String projid = proj.getProjectileSpecId();
      byte var8 = -1;
      switch(projid.hashCode()) {
      case -1488217268:
         if (projid.equals("magellan_edefensor_shot")) {
            var8 = 0;
         }
         break;
      case -1183910284:
         if (projid.equals("magellan_bonesaw_shot")) {
            var8 = 1;
         }
         break;
      case 304188873:
         if (projid.equals("magellan_boneshaker_shot")) {
            var8 = 2;
         }
      }

      float flash_size;
      float flash_dur;
      switch(var8) {
      case 0:
         flash_size = 9.0F;
         flash_dur = 0.2F;
         break;
      case 1:
         flash_size = 10.0F;
         flash_dur = 0.25F;
         break;
      case 2:
         flash_size = 12.0F;
         flash_dur = 0.3F;
         break;
      default:
         return;
      }

      ShipAPI ship = weapon.getShip();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addSmoothParticle(proj_location, ship_velocity, flash_size * 2.5F, 1.0F, 0.25F, flash_dur / 1.5F, FLASH_BRIGHT);
      engine.addHitParticle(proj_location, ship_velocity, flash_size, 1.0F, 0.25F, flash_dur, FLASH_DIM);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
