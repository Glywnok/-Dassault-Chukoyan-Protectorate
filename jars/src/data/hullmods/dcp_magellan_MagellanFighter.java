package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.FighterLaunchBayAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;

public class dcp_magellan_MagellanFighter extends BaseHullMod {
   public static String RD_NO_EXTRA_CRAFT = "rd_no_extra_craft";
   public static final float REFIT_MALUS = 1.5F;
   public static final float REFIT_MALUS_EDC = 2.0F;
   public static final float SPEED_MALUS = 10.0F;
   public static final float ACCEL_MALUS = 0.2F;

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
      if (stats.getVariant().hasHullMod("expanded_deck_crew")) {
         stats.getFighterRefitTimeMult().modifyMult(id, 2.0F);
      } else {
         stats.getFighterRefitTimeMult().modifyMult(id, 1.5F);
      }

      stats.getMaxSpeed().modifyFlat(id, -10.0F);
      stats.getAcceleration().modifyMult(id, 0.8F);
   }

   public void advanceInCombat(ShipAPI ship, float amount) {
      if (ship != null && ship.isAlive()) {
         Iterator var3 = ship.getLaunchBaysCopy().iterator();

         while(var3.hasNext()) {
            FighterLaunchBayAPI bay = (FighterLaunchBayAPI)var3.next();
            if (bay.getWing() != null) {
               FighterWingSpecAPI spec = bay.getWing().getSpec();
               int addForWing = getAdditionalFor(spec);
               int maxTotal = spec.getNumFighters() + addForWing;
               int actualAdd = maxTotal - bay.getWing().getWingMembers().size();
               if (actualAdd > 0) {
                  bay.setExtraDeployments(actualAdd);
                  bay.setExtraDeploymentLimit(maxTotal);
                  bay.setExtraDuration(1000000.0F);
               }
            }
         }

      }
   }

   public static int getAdditionalFor(FighterWingSpecAPI spec) {
      if (spec.hasTag(RD_NO_EXTRA_CRAFT)) {
         return 0;
      } else {
         int size = spec.getNumFighters();
         if (size <= 3) {
            return 1;
         } else if (size == 4) {
            return 2;
         } else {
            return size == 5 ? 3 : 2;
         }
      }
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color pos = Misc.getPositiveHighlightColor();
      Color neg = Misc.getNegativeHighlightColor();
      Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
      Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
      Color quote = dcp_magellan_hullmodUtils.getQuoteColor();
      Color attrib = Misc.getGrayColor();
      tooltip.addSectionHeading(this.getString("MagSpecialTitle"), mag, magbg, Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/magellan_hullmod_fighter.png", 40.0F);
      text.addPara("- " + this.getString("FighterSPDesc1"), padS, h, new String[]{"1", "2", "3", "8"});
      text.addPara("- " + this.getString("FighterSPDesc2"), padS, h, new String[]{"50%"});
      text.addPara("- " + this.getString("FighterSPDesc3"), padS, h, new String[]{"10su"});
      text.addPara("- " + this.getString("FighterSPDesc4"), padS, h, new String[]{"20%"});
      tooltip.addImageWithText(pad);
      tooltip.addPara(this.getString("MagSpecialCompatMalfunction") + " " + this.getString("FighterMalfunctionHL"), neg, pad);
      LabelAPI label;
      if (Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
         tooltip.addSectionHeading(this.getString("FighterSPExTitle"), mag, magbg, Alignment.MID, pad);
         label = tooltip.addPara(this.getString("FighterSPExDesc1") + " " + this.getString("FighterSPExDesc2") + " " + this.getString("FighterSPExDesc3"), pad);
         label.setHighlight(new String[]{"3", "+1", "4", "+2", "5", "+3", "6", "+2", "8"});
         label.setHighlightColors(new Color[]{h, pos, h, pos, h, pos, h, pos, h});
      } else {
         if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
            tooltip.addPara(this.getString("FighterSPExExpand"), attrib, pad);
         }

         label = tooltip.addPara(this.getString("FighterQuote"), quote, pad);
         label.italicize(0.12F);
         tooltip.addPara("      " + this.getString("MagellanEmDash") + this.getString("FighterAttrib"), attrib, padS);
      }
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_magellan_exclusive_hullmod")) {
         return false;
      } else {
         return ship != null && !ship.isFrigate() && ship.getHullSpec().getFighterBays() > 0 && !ship.getVariant().hasHullMod("phasefield") && (ship.getVariant().hasHullMod("dcp_magellan_engineering") || ship.getVariant().hasHullMod("dcp_magellan_engineering_civ") || ship.getVariant().hasHullMod("dcp_magellan_blackcollarmod") || ship.getVariant().hasHullMod("dcp_magellan_startigermod") || ship.getVariant().hasHullMod("dcp_magellan_levellermod") || ship.getVariant().hasHullMod("dcp_magellan_herdmod") || ship.getVariant().hasHullMod("dcp_magellan_yellowtailmod")) && super.isApplicableToShip(ship);
      }
   }

   public String getUnapplicableReason(ShipAPI ship) {
      if (ship != null && ship.isFrigate()) {
         return this.getString("MagSpecialCompatFrigate");
      } else if (ship != null && ship.getHullSpec().getFighterBays() == 0) {
         return this.getString("MagSpecialCompatNoBays");
      } else if (ship != null && ship.getVariant().hasHullMod("phasefield")) {
         return this.getString("MagSpecialCompatPhase");
      } else if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_magellan_exclusive_hullmod")) {
         return this.getString("MagSpecialCompat1");
      } else {
         return ship.getVariant().hasHullMod("dcp_magellan_engineering") && ship.getVariant().hasHullMod("dcp_magellan_engineering_civ") && ship.getVariant().hasHullMod("dcp_magellan_blackcollarmod") && ship.getVariant().hasHullMod("dcp_magellan_startigermod") && ship.getVariant().hasHullMod("dcp_magellan_levellermod") && ship.getVariant().hasHullMod("dcp_magellan_herdmod") && ship.getVariant().hasHullMod("dcp_magellan_yellowtailmod") ? super.getUnapplicableReason(ship) : this.getString("MagSpecialCompat2");
      }
   }
}
