package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;

public class dcp_DME_blinkerEffect implements EveryFrameWeaponEffectPlugin {
   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      weapon.getSprite().setAdditiveBlend();
      if (weapon.getShip() != null && weapon.getShip().isAlive()) {
         weapon.getAnimation().setAlphaMult(1.0F);
      } else {
         weapon.getAnimation().setAlphaMult(0.0F);
      }

   }
}
