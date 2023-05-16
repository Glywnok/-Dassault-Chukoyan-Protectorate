package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashMap;
import java.util.Map;

public class dcp_DME_ITU extends BaseHullMod {
   private static Map mag = new HashMap();

   public String getDescriptionParam(int index, HullSize hullSize) {
      if (index == 0) {
         return "" + ((Float)mag.get(HullSize.FRIGATE)).intValue() + "%";
      } else if (index == 1) {
         return "" + ((Float)mag.get(HullSize.DESTROYER)).intValue() + "%";
      } else if (index == 2) {
         return "" + ((Float)mag.get(HullSize.CRUISER)).intValue() + "%";
      } else {
         return index == 3 ? "" + ((Float)mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%" : null;
      }
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getBallisticWeaponRangeBonus().modifyFlat(id, (Float)mag.get(hullSize));
      stats.getEnergyWeaponRangeBonus().modifyFlat(id, (Float)mag.get(hullSize));
      stats.getWeaponRangeThreshold().modifyFlat(id, (Float)mag.get(hullSize));
   }

   public String getUnapplicableReason(ShipAPI ship) {
      return "Must be installed on a Dassault-Mikoyan ship with Monobloc Construction";
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      return ship.getHullSpec().getHullId().startsWith("dcp_DME_");
   }

   static {
      mag.put(HullSize.FIGHTER, 0.0F);
      mag.put(HullSize.FRIGATE, 100.0F);
      mag.put(HullSize.DESTROYER, 200.0F);
      mag.put(HullSize.CRUISER, 300.0F);
      mag.put(HullSize.CAPITAL_SHIP, 400.0F);
   }
}
