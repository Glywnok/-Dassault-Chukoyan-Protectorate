package data.scripts.weapons.MagicGuidance;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lazywizard.lazylib.combat.CombatUtils;

public class dcp_DME_MagicGuidanceWeaponScript implements EveryFrameWeaponEffectPlugin {
   private List<DamagingProjectileAPI> alreadyRegisteredProjectiles = new ArrayList();

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      ShipAPI source = weapon.getShip();
      ShipAPI target = null;
      if (source.getWeaponGroupFor(weapon) != null) {
         if (source.getWeaponGroupFor(weapon).isAutofiring() && source.getSelectedGroupAPI() != source.getWeaponGroupFor(weapon)) {
            target = source.getWeaponGroupFor(weapon).getAutofirePlugin(weapon).getTargetShip();
         } else {
            target = source.getShipTarget();
         }
      }

      Iterator var6 = CombatUtils.getProjectilesWithinRange(weapon.getLocation(), 200.0F).iterator();

      while(var6.hasNext()) {
         DamagingProjectileAPI proj = (DamagingProjectileAPI)var6.next();
         if (proj.getWeapon() == weapon && !this.alreadyRegisteredProjectiles.contains(proj) && engine.isEntityInPlay(proj) && !proj.didDamage()) {
            engine.addPlugin(new dcp_DME_MagicGuidanceProjScript(proj, target));
            this.alreadyRegisteredProjectiles.add(proj);
         }
      }

      List<DamagingProjectileAPI> cloneList = new ArrayList(this.alreadyRegisteredProjectiles);
      Iterator var10 = cloneList.iterator();

      while(true) {
         DamagingProjectileAPI proj;
         do {
            if (!var10.hasNext()) {
               return;
            }

            proj = (DamagingProjectileAPI)var10.next();
         } while(engine.isEntityInPlay(proj) && !proj.didDamage());

         this.alreadyRegisteredProjectiles.remove(proj);
      }
   }
}
