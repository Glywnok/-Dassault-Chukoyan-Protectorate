package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class dcp_magellan_ConvertedShuttleBay extends BaseHullMod {
   public static final float REFIT_TIME_MULT = 1.5F;
   private static Map<HullSize, Integer> numBays = new HashMap();

   private String getString(String key) {
      return Global.getSettings().getString("Hullmod", "dcp_magellan_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getFighterRefitTimeMult().modifyMult(id, 1.5F);
      stats.getNumFighterBays().modifyFlat(id, (float)(Integer)numBays.get(hullSize));
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
      Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
      tooltip.addSectionHeading(this.getString("MagellanEffects"), mag, magbg, Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("MagellanBaysDesc1"), pad, h, new String[]{"1", "1", "2"});
      tooltip.addPara("- " + this.getString("MagellanBaysDesc2"), padS, h, new String[]{"50%"});
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      return ship != null && !ship.isFrigate() && !ship.getVariant().hasHullMod("phasefield") && super.isApplicableToShip(ship);
   }

   public String getUnapplicableReason(ShipAPI ship) {
      if (ship != null && ship.isFrigate()) {
         return this.getString("MagSpecialCompatFrigate");
      } else {
         return ship != null && ship.getVariant().hasHullMod("phasefield") ? this.getString("MagSpecialCompatPhase") : super.getUnapplicableReason(ship);
      }
   }

   static {
      numBays.put(HullSize.FRIGATE, 0);
      numBays.put(HullSize.DESTROYER, 1);
      numBays.put(HullSize.CRUISER, 1);
      numBays.put(HullSize.CAPITAL_SHIP, 2);
   }
}
