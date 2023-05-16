package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class dcp_DME_BladeBreakerAssault extends BaseHullMod {
   private static Map speed = new HashMap();
   public static final float FLUX_REDUCTION = 25.0F;
   private static final float FLUX_DISSIPATION_MULT = 2.0F;
   private static final float PEAK_MULT = 0.75F;
   private static final float RANGE_THRESHOLD = 600.0F;
   private static final float RANGE_MULT = 0.5F;
   private static final float FTR_SPEED_BOOST = 0.25F;
   private Color color = new Color(0, 255, 0, 255);

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "dcp_DME_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getMaxSpeed().modifyFlat(id, (Float)speed.get(hullSize));
      stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -25.0F);
      stats.getEnergyWeaponFluxCostMod().modifyPercent(id, -25.0F);
      stats.getFluxDissipation().modifyMult(id, 2.0F);
      stats.getPeakCRDuration().modifyMult(id, 0.75F);
      stats.getVentRateMult().modifyMult(id, 0.0F);
      stats.getWeaponRangeThreshold().modifyFlat(id, 600.0F);
      stats.getWeaponRangeMultPastThreshold().modifyMult(id, 0.5F);
   }

   public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
      MutableShipStatsAPI stats = fighter.getMutableStats();
      stats.getMaxSpeed().modifyMult(id, 1.25F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      tooltip.addSectionHeading("Details", Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/dcp_hullmod_movement.png", 40.0F);
      text.addPara("- " + this.getString("BBAssaultDesc1"), pad, Misc.getHighlightColor(), new String[]{"50", "40", "25", "15"});
      text.addPara("- " + this.getString("BBAssaultDesc2"), padS, Misc.getHighlightColor(), new String[]{"25%"});
      text.addPara("- " + this.getString("BBAssaultDesc3"), padS, Misc.getHighlightColor(), new String[]{"2"});
      text.addPara("- " + this.getString("BBAssaultDesc4"), padS, Misc.getHighlightColor(), new String[]{"600"});
      text.addPara("- " + this.getString("BBAssaultDesc5"), padS, Misc.getHighlightColor(), new String[]{"25%"});
      tooltip.addImageWithText(pad);
      TooltipMakerAPI text2 = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/dcp_hullmod_fighter.png", 40.0F);
      text2.addPara("- " + this.getString("BBAssaultDescFtr"), padS, Misc.getHighlightColor(), new String[]{"25%"});
      tooltip.addImageWithText(pad);
   }

   public void advanceInCombat(ShipAPI ship, float amount) {
      HullSize hullSize = ship.getHullSize();
      MutableShipStatsAPI stats = ship.getMutableStats();
      String id = "BladeBreakerAssault";
      FluxTrackerAPI flux = ship.getFluxTracker();
      float fluxLevel = flux.getCurrFlux() / flux.getMaxFlux();
      float speedBonus = 1.0F;
      if (flux.isOverloadedOrVenting()) {
         speedBonus = 2.0F;
      }

      stats.getMaxSpeed().modifyFlat(id, (Float)speed.get(hullSize) * speedBonus * fluxLevel);
      stats.getAcceleration().modifyPercent(id, 60.0F * (Float)speed.get(hullSize) * speedBonus * fluxLevel);
      stats.getDeceleration().modifyPercent(id, 40.0F * (Float)speed.get(hullSize) * speedBonus * fluxLevel);
      stats.getMaxTurnRate().modifyFlat(id, 5.0F * (speedBonus / 2.0F) * fluxLevel);
      stats.getMaxTurnRate().modifyPercent(id, 100.0F * (Float)speed.get(hullSize) * fluxLevel);
      stats.getTurnAcceleration().modifyFlat(id, 10.0F * (speedBonus / 1.5F) * fluxLevel);
      stats.getTurnAcceleration().modifyPercent(id, 50.0F * (speedBonus / 1.5F) * fluxLevel);
      stats.getEngineDamageTakenMult().modifyPercent(id, 100.0F * fluxLevel);
      ship.getEngineController().fadeToOtherColor(this, this.color, (Color)null, 1.0F * fluxLevel, 0.5F);
      ship.getEngineController().extendFlame(this, 0.3F * fluxLevel, 0.3F * fluxLevel, 0.6F * fluxLevel);
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_DME_breaker_package")) {
         return false;
      } else {
         return ship.getVariant().hasHullMod("dcp_DME_bbengineering") && super.isApplicableToShip(ship);
      }
   }

   public String getUnapplicableReason(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_DME_breaker_package")) {
         return "Can only install one combat focus on a Blade Breaker hull";
      } else {
         return !ship.getVariant().hasHullMod("dcp_DME_bbengineering") ? "Must be installed on a Blade Breaker ship" : super.getUnapplicableReason(ship);
      }
   }

   static {
      speed.put(HullSize.FRIGATE, 50.0F);
      speed.put(HullSize.DESTROYER, 40.0F);
      speed.put(HullSize.CRUISER, 25.0F);
      speed.put(HullSize.CAPITAL_SHIP, 15.0F);
   }
}
