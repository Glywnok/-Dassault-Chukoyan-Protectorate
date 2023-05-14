package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class dcp_DME_NodalPhaseCloak extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(2);
   public static final float DEGRADE_INCREASE_PERCENT = 50.0F;

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getSensorProfile().modifyMult(id, 0.0F);
      stats.getCRLossPerSecondPercent().modifyPercent(id, 50.0F);
   }

   public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
      Iterator var3 = BLOCKED_HULLMODS.iterator();

      while(var3.hasNext()) {
         String tmp = (String)var3.next();
         if (ship.getVariant().getHullMods().contains(tmp)) {
            ship.getVariant().removeMod(tmp);
            DCPBlockedHullmodDisplayScript.showBlocked(ship);
         }
      }

   }

   public String getDescriptionParam(int index, HullSize hullSize) {
      return index == 0 ? "50%" : null;
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      return ship != null && (ship.getHullSpec().getNoCRLossTime() < 10000.0F || ship.getHullSpec().getCRLossPerSecond() > 0.0F);
   }

   static {
      BLOCKED_HULLMODS.add("convertedhangar");
      BLOCKED_HULLMODS.add("roider_fighterClamps");
   }
}
