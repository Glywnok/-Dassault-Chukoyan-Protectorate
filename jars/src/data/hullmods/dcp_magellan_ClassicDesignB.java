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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class dcp_magellan_ClassicDesignB extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(3);
   public static final float HEALTH_BONUS = 100.0F;
   public static final float RANGE_BONUS = 30.0F;
   private static final float MALFUNCTION_DECREASE = 50.0F;

   public int getDisplaySortOrder() {
      return 0;
   }

   public int getDisplayCategoryIndex() {
      return 0;
   }

   private String getString(String key) {
      return Global.getSettings().getString("Hullmod", "magellan_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getBallisticWeaponRangeBonus().modifyPercent(id, 30.0F);
      stats.getEnergyWeaponRangeBonus().modifyPercent(id, 30.0F);
      stats.getEngineHealthBonus().modifyPercent(id, 100.0F);
      stats.getCriticalMalfunctionChance().modifyMult(id, 0.5F);
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
      tooltip.addPara("- " + this.getString("ClassicDesc1"), pad, h, new String[]{"30%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc3"), padS, h, new String[]{"100%"});
      tooltip.addPara("- " + this.getString("BlackcollarModDesc7"), padS, h, new String[]{"50%"});
      tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("MagellanAllIncomp"), padS);
      text.addPara("- Integrated Targeting Unit", bad, padS);
      text.addPara("- Dedicated Targeting Core", bad, 0.0F);
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

   static {
      BLOCKED_HULLMODS.add("targetingunit");
      BLOCKED_HULLMODS.add("dedicated_targeting_core");
   }
}
