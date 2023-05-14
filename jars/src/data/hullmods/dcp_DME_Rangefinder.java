package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.util.HashMap;
import java.util.Map;

public class dcp_DME_Rangefinder extends BaseHullMod {
   private static Map mag = new HashMap();
   public static final float AUTOFIRE_BONUS = 60.0F;

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getAutofireAimAccuracy().modifyFlat(id, 0.59999996F);
   }

   public String getDescriptionParam(int index, HullSize hullSize) {
      if (index == 0) {
         return "60%";
      } else if (index == 1) {
         return "" + ((Float)mag.get(HullSize.FRIGATE)).intValue();
      } else if (index == 2) {
         return "" + ((Float)mag.get(HullSize.DESTROYER)).intValue();
      } else if (index == 3) {
         return "" + ((Float)mag.get(HullSize.CRUISER)).intValue();
      } else {
         return index == 4 ? "" + ((Float)mag.get(HullSize.CAPITAL_SHIP)).intValue() : null;
      }
   }

   static {
      mag.put(HullSize.FIGHTER, 0.0F);
      mag.put(HullSize.FRIGATE, 0.0F);
      mag.put(HullSize.DESTROYER, 100.0F);
      mag.put(HullSize.CRUISER, 200.0F);
      mag.put(HullSize.CAPITAL_SHIP, 200.0F);
   }
}
