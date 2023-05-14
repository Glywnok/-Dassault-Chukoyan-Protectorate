package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class dcp_magellan_MagellanMovement extends BaseHullMod {
   private final IntervalUtil interval = new IntervalUtil(0.1F, 0.15F);
   private static Map speed = new HashMap();
   public static final float ZERO_FLUX_BONUS = 10.0F;
   public static final float FUEL_USE_PERCENT = 25.0F;
   public static final float ENGINE_DAMAGE_MULT = 2.0F;
   public static final float ENGINE_DAMAGE_MULT_SO = 4.0F;
   public static final float ENGINE_REPAIR_MULT_SO = 0.3F;
   public static final float FLAMEOUT_CHANCE_SO = 0.02F;
   private Color color = new Color(200, 200, 200, 255);

   public int getDisplaySortOrder() {
      return 4;
   }

   public int getDisplayCategoryIndex() {
      return 3;
   }

   private String getString(String key) {
      return Global.getSettings().getString("Hullmod", "magellan_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getMaxSpeed().modifyFlat(id, (Float)speed.get(hullSize));
      stats.getZeroFluxSpeedBoost().modifyFlat(id, 10.0F);
      stats.getFuelUseMod().modifyPercent(id, 25.0F);
      if (!stats.getVariant().hasHullMod("safetyoverrides") && !stats.getVariant().hasHullMod("eis_aquila")) {
         stats.getEngineDamageTakenMult().modifyMult(id, 2.0F);
      } else {
         stats.getEngineDamageTakenMult().modifyMult(id, 4.0F);
         stats.getCombatEngineRepairTimeMult().modifyMult(id, 1.3F);
         stats.getEngineMalfunctionChance().modifyFlat(id, 0.02F);
      }

   }

   public void advanceInCombat(ShipAPI ship, float amount) {
      this.interval.advance(amount);
      if (this.interval.intervalElapsed()) {
         float enginejitter = 0.3F + 0.1F * (float)Math.random();
         if (ship.getVariant().hasHullMod("safetyoverrides") || ship.getVariant().hasHullMod("eis_aquila")) {
            this.color = new Color(255, 135, 135, 255);
            enginejitter = -0.3F + 0.5F * (float)Math.random();
         }

         ship.getEngineController().fadeToOtherColor(this, this.color, (Color)null, 1.0F, enginejitter);
         ship.getEngineController().extendFlame(this, 0.1F, enginejitter, enginejitter);
      }

   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color neg = Misc.getNegativeHighlightColor();
      Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
      Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
      Color quote = dcp_magellan_hullmodUtils.getQuoteColor();
      Color attrib = Misc.getGrayColor();
      tooltip.addSectionHeading(this.getString("MagSpecialTitle"), mag, magbg, Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/magellan_hullmod_movement.png", 40.0F);
      text.addPara("- " + this.getString("MovementSPDesc1"), padS, h, new String[]{"30", "20", "10", "10su"});
      text.addPara("- " + this.getString("MovementSPDesc2"), padS, h, new String[]{"25%"});
      text.addPara("- " + this.getString("MovementSPDesc3"), padS, h, new String[]{this.getString("MovementSP3HL")});
      tooltip.addImageWithText(pad);
      tooltip.addPara(this.getString("MagSpecialCompatMalfunction") + " " + this.getString("MovementMalfunctionHL"), neg, pad);
      LabelAPI label = tooltip.addPara(this.getString("MovementQuote"), quote, pad);
      label.italicize(0.12F);
      tooltip.addPara("      " + this.getString("MagellanEmDash") + this.getString("MovementAttrib"), attrib, padS);
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_exclusive_hullmod")) {
         return false;
      } else {
         return ship != null && !ship.isCapital() && (ship.getVariant().hasHullMod("magellan_engineering") || ship.getVariant().hasHullMod("magellan_engineering_civ") || ship.getVariant().hasHullMod("magellan_blackcollarmod") || ship.getVariant().hasHullMod("magellan_startigermod") || ship.getVariant().hasHullMod("magellan_levellermod") || ship.getVariant().hasHullMod("magellan_herdmod") || ship.getVariant().hasHullMod("magellan_yellowtailmod")) && super.isApplicableToShip(ship);
      }
   }

   public String getUnapplicableReason(ShipAPI ship) {
      if (ship != null && ship.isCapital()) {
         return this.getString("MagSpecialCompatCapital");
      } else if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_exclusive_hullmod")) {
         return this.getString("MagSpecialCompat1");
      } else {
         return ship.getVariant().hasHullMod("magellan_engineering") && ship.getVariant().hasHullMod("magellan_engineering_civ") && ship.getVariant().hasHullMod("magellan_blackcollarmod") && ship.getVariant().hasHullMod("magellan_startigermod") && ship.getVariant().hasHullMod("magellan_levellermod") && ship.getVariant().hasHullMod("magellan_herdmod") && ship.getVariant().hasHullMod("magellan_yellowtailmod") ? super.getUnapplicableReason(ship) : this.getString("MagSpecialCompat2");
      }
   }

   static {
      speed.put(HullSize.FRIGATE, 30.0F);
      speed.put(HullSize.DESTROYER, 20.0F);
      speed.put(HullSize.CRUISER, 10.0F);
      speed.put(HullSize.CAPITAL_SHIP, 0.0F);
   }
}
