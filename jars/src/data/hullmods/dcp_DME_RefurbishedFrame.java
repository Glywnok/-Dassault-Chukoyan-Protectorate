package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.HashSet;
import java.util.Set;

public class dcp_DME_RefurbishedFrame extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(6);
   public static final float REPAIR_RATE_BONUS = 25.0F;
   public static final float CR_RECOVERY_BONUS = 25.0F;
   public static final float REPAIR_BONUS = 25.0F;

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "istl_" + key);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      tooltip.addSectionHeading("Details", Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("RefurbDesc1"), pad, Misc.getHighlightColor(), new String[]{"25%"});
      tooltip.addPara("- " + this.getString("RefurbDesc2"), padS, Misc.getHighlightColor(), new String[]{"25%"});
      tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/ISTL/icons/tooltip/hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("DMEAllIncomp"), padS);
      text.addPara("- Heavy Armor", Misc.getNegativeHighlightColor(), padS);
      if (Global.getSettings().getModManager().isModEnabled("apex_design")) {
         text.addPara("- Nanolaminate Plating", Misc.getNegativeHighlightColor(), 0.0F);
         text.addPara("- Cryocooled Armor Lattice", Misc.getNegativeHighlightColor(), 0.0F);
      }

      tooltip.addImageWithText(pad);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getCombatEngineRepairTimeMult().modifyMult(id, 0.75F);
      stats.getCombatWeaponRepairTimeMult().modifyMult(id, 0.75F);
      stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, 25.0F);
      stats.getRepairRatePercentPerDay().modifyPercent(id, 25.0F);
   }

   public String getDescriptionParam(int index, HullSize hullSize) {
      if (index == 0) {
         return "25";
      } else {
         return index == 1 ? "25" : null;
      }
   }

   static {
      BLOCKED_HULLMODS.add("heavyarmor");
      BLOCKED_HULLMODS.add("apex_armor");
      BLOCKED_HULLMODS.add("apex_cryo_armor");
      BLOCKED_HULLMODS.add("istl_bbassault");
      BLOCKED_HULLMODS.add("istl_bbdefense");
      BLOCKED_HULLMODS.add("istl_bbsupport");
   }
}
