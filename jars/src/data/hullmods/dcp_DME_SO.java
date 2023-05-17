package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class dcp_DME_SO extends BaseHullMod {
   private static Map speed = new HashMap();
   private static final float PEAK_MULT = 0.33F;
   private static final float FLUX_DISSIPATION_MULT = 2.0F;
   private static final float RANGE_THRESHOLD = -150.0F;
   private Color color = new Color(255, 100, 255, 255);

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getMaxSpeed().modifyFlat(id, (Float)speed.get(hullSize));
      stats.getAcceleration().modifyFlat(id, (Float)speed.get(hullSize) * 2.0F);
      stats.getDeceleration().modifyFlat(id, (Float)speed.get(hullSize) * 2.0F);
      stats.getZeroFluxMinimumFluxLevel().modifyFlat(id, 5.0F);
      stats.getFluxDissipation().modifyMult(id, 2.0F);
      stats.getPeakCRDuration().modifyMult(id, 0.33F);
      stats.getVentRateMult().modifyMult(id, 0.0F);
      stats.getWeaponRangeThreshold().modifyFlat(id, -150.0F);
   }

   public String getDescriptionParam(int index, HullSize hullSize) {
      if (index == 0) {
         return "" + ((Float)speed.get(HullSize.FRIGATE)).intValue();
      } else if (index == 1) {
         return "" + ((Float)speed.get(HullSize.DESTROYER)).intValue();
      } else if (index == 2) {
         return "" + ((Float)speed.get(HullSize.CRUISER)).intValue();
      } else if (index == 3) {
         return Misc.getRoundedValue(2.0F);
      } else if (index == 4) {
         return Misc.getRoundedValue(0.33F);
      } else {
         return index == 5 ? Misc.getRoundedValue(-150.0F) : null;
      }
   }

   public String getUnapplicableReason(ShipAPI ship) {
      return "Must be installed on a Dassault-Mikoyan ship";
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      return ship.getHullSpec().getHullId().startsWith("dcp_DME_");
   }

   public void advanceInCombat(ShipAPI ship, float amount) {
      ship.getEngineController().fadeToOtherColor(this, this.color, (Color)null, 1.0F, 0.4F);
      ship.getEngineController().extendFlame(this, 0.25F, 0.25F, 0.25F);
   }

   static {
      speed.put(HullSize.FRIGATE, 50.0F);
      speed.put(HullSize.DESTROYER, 35.0F);
      speed.put(HullSize.CRUISER, 20.0F);
      speed.put(HullSize.CAPITAL_SHIP, 10.0F);
   }
}
