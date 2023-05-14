package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.MathUtils;

public class dcp_DME_VelocityVarOnFire implements OnFireEffectPlugin {
   private static final float VELOCITYMIN = 0.92F;
   private static final float VELOCITYMAX = 1.08F;

   public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
      projectile.getVelocity().scale(MathUtils.getRandomNumberInRange(0.92F, 1.08F));
   }
}
