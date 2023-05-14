package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class dcp_DME_BladeBreakerDefense extends BaseHullMod {
   public static final float HEALTH_BONUS = 100.0F;
   public static final float SHIELD_BONUS = 20.0F;
   public static final float PIERCE_MULT = 0.5F;
   public static final float DAMAGE_REDUCTION = 0.8F;

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "istl_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getEngineHealthBonus().modifyPercent(id, 100.0F);
      stats.getWeaponHealthBonus().modifyPercent(id, 100.0F);
      stats.getShieldDamageTakenMult().modifyMult(id, 0.8F);
      stats.getDynamic().getStat("shield_pierced_mult").modifyMult(id, 0.5F);
      stats.getHighExplosiveDamageTakenMult().modifyMult(id, 0.8F);
      stats.getEnergyDamageTakenMult().modifyMult(id, 0.8F);
   }

   public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
      MutableShipStatsAPI stats = fighter.getMutableStats();
      stats.getArmorDamageTakenMult().modifyMult(id, 0.8F);
      stats.getHullDamageTakenMult().modifyMult(id, 0.8F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      tooltip.addSectionHeading("Details", Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/ISTL/icons/tooltip/istl_hullmod_defense.png", 40.0F);
      text.addPara("- " + this.getString("BBDefenseDesc1"), pad, Misc.getHighlightColor(), new String[]{"20%"});
      text.addPara("- " + this.getString("BBDefenseDesc2"), padS, Misc.getHighlightColor(), new String[]{"20%"});
      text.addPara("- " + this.getString("BBDefenseDesc3"), padS, Misc.getHighlightColor(), new String[]{"100%"});
      tooltip.addImageWithText(pad);
      TooltipMakerAPI text2 = tooltip.beginImageWithText("graphics/ISTL/icons/tooltip/istl_hullmod_fighter.png", 40.0F);
      text2.addPara("- " + this.getString("BBDefenseDescFtr"), padS, Misc.getHighlightColor(), new String[]{"20%"});
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
}
