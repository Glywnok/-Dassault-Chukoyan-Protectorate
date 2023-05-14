package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class dcp_DME_AdvNavRelay extends BaseHullMod {
   private static Map mag = new HashMap();
   private static final Set<String> BLOCKED_HULLMODS;

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getDynamic().getMod("coord_maneuvers_flat").modifyFlat(id, (Float)mag.get(hullSize));
   }

   public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
      Iterator var3 = BLOCKED_HULLMODS.iterator();

      while(var3.hasNext()) {
         String tmp = (String)var3.next();
         if (ship.getVariant().getHullMods().contains(tmp)) {
            ship.getVariant().removeMod(tmp);
            DMEBlockedHullmodDisplayScript.showBlocked(ship);
         }
      }

   }

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

   static {
      mag.put(HullSize.FRIGATE, 5.0F);
      mag.put(HullSize.DESTROYER, 10.0F);
      mag.put(HullSize.CRUISER, 10.0F);
      mag.put(HullSize.CAPITAL_SHIP, 15.0F);
      BLOCKED_HULLMODS = new HashSet(1);
      BLOCKED_HULLMODS.add("navrelay");
   }
}
