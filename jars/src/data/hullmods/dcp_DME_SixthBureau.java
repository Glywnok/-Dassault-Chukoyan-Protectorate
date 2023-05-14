package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class dcp_DME_SixthBureau extends BaseHullMod {
   private static final float PROFILE_DECREASE = 25.0F;
   private static final float CAPACITY_MULT = 1.05F;
   private static final float DISSIPATION_MULT = 1.05F;
   private static final float SUPPLY_USE_MULT = 1.15F;

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getSensorProfile().modifyMult(id, 0.75F);
      stats.getFluxCapacity().modifyMult(id, 1.05F);
      stats.getFluxDissipation().modifyMult(id, 1.05F);
      stats.getSuppliesPerMonth().modifyMult(id, 1.15F);
   }

   public String getDescriptionParam(int index, HullSize hullSize) {
      if (index == 0) {
         return "25";
      } else if (index == 1) {
         return "5";
      } else {
         return index == 2 ? "15%" : null;
      }
   }
}
