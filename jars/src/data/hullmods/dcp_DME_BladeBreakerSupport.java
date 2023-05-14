package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.HashMap;
import java.util.Map;

public class dcp_DME_BladeBreakerSupport extends BaseHullMod {
   private static final Map mag = new HashMap();
   public static final float TURRET_SPEED_BONUS = 50.0F;
   public static final float SIGHT_BONUS = 125.0F;
   public static final float FTR_RANGE_BONUS = 100.0F;

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "istl_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getBallisticWeaponRangeBonus().modifyFlat(id, (Float)mag.get(hullSize));
      stats.getEnergyWeaponRangeBonus().modifyFlat(id, (Float)mag.get(hullSize));
      stats.getWeaponTurnRateBonus().modifyPercent(id, 50.0F);
      stats.getBeamWeaponTurnRateBonus().modifyPercent(id, 50.0F);
      stats.getSightRadiusMod().modifyPercent(id, 125.0F);
   }

   public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
      MutableShipStatsAPI stats = fighter.getMutableStats();
      stats.getBallisticWeaponRangeBonus().modifyFlat(id, 100.0F);
      stats.getEnergyWeaponRangeBonus().modifyFlat(id, 100.0F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      tooltip.addSectionHeading("Details", Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/ISTL/icons/tooltip/istl_hullmod_sensor.png", 40.0F);
      text.addPara("- " + this.getString("BBSupportDesc1"), pad, Misc.getHighlightColor(), new String[]{"100", "200", "300", "400"});
      text.addPara("- " + this.getString("BBSupportDesc2"), padS, Misc.getHighlightColor(), new String[]{"50%"});
      text.addPara("- " + this.getString("BBSupportDesc3"), padS, Misc.getHighlightColor(), new String[]{"25%"});
      tooltip.addImageWithText(pad);
      TooltipMakerAPI text2 = tooltip.beginImageWithText("graphics/ISTL/icons/tooltip/istl_hullmod_fighter.png", 40.0F);
      text2.addPara("- " + this.getString("BBSupportDescFtr"), padS, Misc.getHighlightColor(), new String[]{"100"});
      tooltip.addImageWithText(pad);
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "istl_breaker_package")) {
         return false;
      } else {
         return ship.getVariant().hasHullMod("istl_bbengineering") && super.isApplicableToShip(ship);
      }
   }

   public String getUnapplicableReason(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "istl_breaker_package")) {
         return "Can only install one combat focus on a Blade Breaker hull";
      } else {
         return !ship.getVariant().hasHullMod("istl_bbengineering") ? "Must be installed on a Blade Breaker ship" : super.getUnapplicableReason(ship);
      }
   }

   static {
      mag.put(HullSize.FIGHTER, 0.0F);
      mag.put(HullSize.FRIGATE, 100.0F);
      mag.put(HullSize.DESTROYER, 200.0F);
      mag.put(HullSize.CRUISER, 300.0F);
      mag.put(HullSize.CAPITAL_SHIP, 400.0F);
   }
}
