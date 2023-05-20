package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.MathUtils;

public class dcp_magellan_VelocityVarOnFire implements OnFireEffectPlugin {
   public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
      String projid = projectile.getProjectileSpecId();
      byte var8 = -1;
      switch(projid.hashCode()) {
      case -1355660860:
         if (projid.equals("dcp_magellan_eflak_shot")) {
            var8 = 0;
         }
         break;
      case -1183910284:
         if (projid.equals("dcp_magellan_bonesaw_shot")) {
            var8 = 1;
         }
         break;
      case 227094684:
         if (projid.equals("dcp_magellan_bonegrinder_shot")) {
            var8 = 2;
         }
      }

      float velocitymin_mult;
      float velocitymax_mult;
      switch(var8) {
      case 0:
         velocitymin_mult = 0.97F;
         velocitymax_mult = 1.03F;
         break;
      case 1:
         velocitymin_mult = 0.95F;
         velocitymax_mult = 1.05F;
         break;
      case 2:
         velocitymin_mult = 0.9F;
         velocitymax_mult = 1.1F;
         break;
      default:
         return;
      }

      projectile.getVelocity().scale(MathUtils.getRandomNumberInRange(velocitymin_mult, velocitymax_mult));
   }
}
