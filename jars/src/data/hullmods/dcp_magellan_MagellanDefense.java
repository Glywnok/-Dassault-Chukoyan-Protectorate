package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.DCPUtils;
import java.awt.Color;

public class dcp_magellan_MagellanDefense extends BaseHullMod {
   public static final Color ZERO_FLUX_RING = new Color(255, 255, 225, 255);
   public static final Color ZERO_FLUX_INNER = new Color(125, 125, 100, 75);
   public static final Color FULL_FLUX_RING = new Color(255, 240, 225, 255);
   public static final Color FULL_FLUX_INNER = new Color(255, 90, 75, 75);
   public static final float PROJ_DAMAGE_MULT = 0.15F;
   public static final float BEAM_DAMAGE_MULT = 0.2F;
   public static final float FRAG_DAMAGE_MULT = 0.25F;
   public static final float PROJ_DAMAGE_MULT_HS = 0.05F;
   public static final float BEAM_DAMAGE_MULT_HS = 0.5F;
   public static final float OVERLOAD_DUR_MULT = 1.5F;
   public static final float SHIELD_DIE_CHANCE = 0.03F;
   public static final float SHIELD_DIE_FLUXLEVEL = 0.8F;

   public int getDisplaySortOrder() {
      return 4;
   }

   public int getDisplayCategoryIndex() {
      return 3;
   }

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "dcp_magellan_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      if (stats.getVariant().hasHullMod("hardenedshieldemitter")) {
         stats.getProjectileShieldDamageTakenMult().modifyMult(id, 0.95F);
         stats.getBeamShieldDamageTakenMult().modifyMult(id, 1.5F);
         stats.getOverloadTimeMod().modifyMult(id, 1.5F);
         stats.getShieldMalfunctionChance().modifyFlat(id, 0.03F);
         stats.getShieldMalfunctionFluxLevel().modifyFlat(id, 0.8F);
      } else {
         stats.getProjectileShieldDamageTakenMult().modifyMult(id, 0.85F);
         stats.getBeamShieldDamageTakenMult().modifyMult(id, 1.2F);
      }

      stats.getFragmentationDamageTakenMult().modifyMult(id, 0.75F);
   }

   public void advanceInCombat(ShipAPI ship, float amount) {
      if (ship.getShield() != null) {
         float hardflux_track = ship.getHardFluxLevel();
         float outputColorLerp = 0.0F;
         if (hardflux_track < 0.5F) {
            outputColorLerp = 0.0F;
         } else if (hardflux_track >= 0.5F) {
            outputColorLerp = DCPUtils.lerp(0.0F, hardflux_track, hardflux_track);
         }

         Color color1 = Misc.interpolateColor(ZERO_FLUX_RING, FULL_FLUX_RING, Math.min(outputColorLerp, 1.0F));
         Color color2 = Misc.interpolateColor(ZERO_FLUX_INNER, FULL_FLUX_INNER, Math.min(outputColorLerp, 1.0F));
         ship.getShield().setRingColor(color1);
         ship.getShield().setInnerColor(color2);
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
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/magellan_hullmod_defense.png", 40.0F);
      text.addPara("- " + this.getString("DefenseSPDesc1"), padS, h, new String[]{"15%"});
      text.addPara("- " + this.getString("DefenseSPDesc2"), padS, h, new String[]{"20%"});
      text.addPara("- " + this.getString("DefenseSPDesc3"), padS, h, new String[]{"25%"});
      tooltip.addImageWithText(pad);
      tooltip.addPara(this.getString("MagSpecialCompatMalfunction") + " " + this.getString("DefenseMalfunctionHL"), neg, pad);
      LabelAPI label = tooltip.addPara(this.getString("DefenseQuote"), quote, pad);
      label.italicize(0.12F);
      tooltip.addPara("      " + this.getString("MagellanEmDash") + this.getString("DefenseAttrib"), attrib, padS);
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_magellan_exclusive_hullmod")) {
         return false;
      } else {
         return (ship.getVariant().hasHullMod("dcp_magellan_engineering") || ship.getVariant().hasHullMod("dcp_magellan_engineering_civ") || ship.getVariant().hasHullMod("dcp_magellan_blackcollarmod") || ship.getVariant().hasHullMod("dcp_magellan_startigermod") || ship.getVariant().hasHullMod("dcp_magellan_levellermod") || ship.getVariant().hasHullMod("dcp_magellan_herdmod") || ship.getVariant().hasHullMod("dcp_magellan_yellowtailmod")) && super.isApplicableToShip(ship);
      }
   }

   public String getUnapplicableReason(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_magellan_exclusive_hullmod")) {
         return this.getString("MagSpecialCompat1");
      } else {
         return ship.getVariant().hasHullMod("dcp_magellan_engineering") && ship.getVariant().hasHullMod("dcp_magellan_engineering_civ") && ship.getVariant().hasHullMod("dcp_magellan_blackcollarmod") && ship.getVariant().hasHullMod("dcp_magellan_startigermod") && ship.getVariant().hasHullMod("dcp_magellan_levellermod") && ship.getVariant().hasHullMod("dcp_magellan_herdmod") && ship.getVariant().hasHullMod("dcp_magellan_yellowtailmod") ? super.getUnapplicableReason(ship) : this.getString("MagSpecialCompat2");
      }
   }
}
