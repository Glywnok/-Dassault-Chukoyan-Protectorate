package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.DCPUtils;
import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class dcp_magellan_ClassicDesign extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(3);
   public static final Color ZERO_FLUX_RING = new Color(215, 215, 255, 255);
   public static final Color ZERO_FLUX_INNER = new Color(0, 110, 200, 75);
   public static final Color FULL_FLUX_RING = new Color(255, 240, 225, 255);
   public static final Color FULL_FLUX_INNER = new Color(255, 90, 75, 75);
   public static final float HEALTH_BONUS = 100.0F;
   public static final float RANGE_BONUS = 50.0F;
   private static final float MALFUNCTION_DECREASE = 25.0F;
   private static final float EXTRA_MODS = 1.0F;

   public int getDisplaySortOrder() {
      return 0;
   }

   public int getDisplayCategoryIndex() {
      return 0;
   }

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "dcp_magellan_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getBallisticWeaponRangeBonus().modifyPercent(id, 50.0F);
      stats.getEnergyWeaponRangeBonus().modifyPercent(id, 50.0F);
      stats.getDynamic().getStat("replacement_rate_decrease_mult").modifyMult(id, 0.0F);
      stats.getEngineHealthBonus().modifyPercent(id, 100.0F);
      stats.getCriticalMalfunctionChance().modifyMult(id, 0.75F);
      stats.getDynamic().getMod("max_permanent_hullmods_mod").modifyFlat(id, 1.0F);
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
      Color clas = dcp_magellan_hullmodUtils.getClassicHLColor();
      Color clasbg = dcp_magellan_hullmodUtils.getClassicBGColor();
      Color bad = Misc.getNegativeHighlightColor();
      Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
      tooltip.addSectionHeading(this.getString("ClassicTitle"), clas, clasbg, Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("ClassicDesc1"), pad, h, new String[]{"50%"});
      tooltip.addPara("- " + this.getString("ClassicDesc2"), padS, h, new String[]{this.getString("Classic2HL")});
      tooltip.addPara("- " + this.getString("ClassicDesc3"), padS, h, new String[]{"1"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc3"), padS, h, new String[]{"100%"});
      tooltip.addPara("- " + this.getString("BlackcollarModDesc7"), padS, h, new String[]{"25%"});
      tooltip.addSectionHeading(this.getString("magellanIncompTitle"), bad, badbg, Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("magellanAllIncomp"), padS);
      text.addPara("- Integrated Targeting Unit", bad, padS);
      text.addPara("- Dedicated Targeting Core", bad, 0.0F);
      text.addPara("- Expanded Deck Crew", bad, 0.0F);
      tooltip.addImageWithText(pad);
   }

   public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
      Iterator var3 = BLOCKED_HULLMODS.iterator();

      while(var3.hasNext()) {
         String tmp = (String)var3.next();
         if (ship.getVariant().getHullMods().contains(tmp)) {
            ship.getVariant().removeMod(tmp);
            DCPBlockedHullmodDisplayScript.showBlocked(ship);
         }
      }

   }

   public boolean isApplicableToShip(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_magellan_core_hullmod")) {
         return false;
      } else {
         return ship != null && !ship.isCapital() && super.isApplicableToShip(ship);
      }
   }

   public String getUnapplicableReason(ShipAPI ship) {
      if (ship != null && ship.isCapital()) {
         return this.getString("MagSpecialCompatCapital");
      } else {
         return this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_magellan_core_hullmod") ? this.getString("MagSpecialCompat3") : super.getUnapplicableReason(ship);
      }
   }

   static {
      BLOCKED_HULLMODS.add("targetingunit");
      BLOCKED_HULLMODS.add("dedicated_targeting_core");
      BLOCKED_HULLMODS.add("expanded_deck_crew");
   }
}
