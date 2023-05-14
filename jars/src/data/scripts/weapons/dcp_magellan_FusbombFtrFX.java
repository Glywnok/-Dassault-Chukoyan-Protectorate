package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class dcp_magellan_FusbombFtrFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      float speedMult = 0.75F + 0.25F * (float)Math.random();
      proj.getVelocity().scale(speedMult);
      float angVel = (float)((double)Math.signum((float)Math.random() - 0.5F) * (0.5D + Math.random()) * 720.0D);
      proj.setAngularVelocity(angVel);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
