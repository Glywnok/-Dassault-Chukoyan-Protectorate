package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class dcp_DME_DecoRotation implements EveryFrameWeaponEffectPlugin {
   private static final float SPIN = 120.0F;

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused() && weapon.getShip() != null && weapon.getShip().isAlive()) {
         float curr = weapon.getCurrAngle();
         curr += amount * 120.0F;
         weapon.setCurrAngle(curr);
      }
   }
}
