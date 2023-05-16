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
import java.util.Iterator;
import java.util.Set;

public class dcp_DME_MonoblocConstruction extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(8);
   public static final float FLUX_RESISTANCE = 25.0F;
   private static final float OVERLOAD_DUR_MULT = 0.5F;
   public static final float ZERO_FLUX_BONUS = 25.0F;

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "dcp_DME_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getEmpDamageTakenMult().modifyMult(id, 0.75F);
      stats.getOverloadTimeMod().modifyMult(id, 0.5F);
      stats.getZeroFluxSpeedBoost().modifyFlat(id, 25.0F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      tooltip.addSectionHeading("Details", Alignment.MID, pad);
      tooltip.addPara("%s " + this.getString("MonoblocDesc1"), pad, Misc.getHighlightColor(), new String[]{"-", "25%"});
      tooltip.addPara("%s " + this.getString("MonoblocDesc2"), padS, Misc.getHighlightColor(), new String[]{"-", "50%"});
      tooltip.addPara("%s " + this.getString("MonoblocDesc3"), padS, Misc.getHighlightColor(), new String[]{"-", "25su"});
      tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("DMEAllIncomp"), padS);
      text.addPara("- Heavy Armor", Misc.getNegativeHighlightColor(), padS);
      if (Global.getSettings().getModManager().isModEnabled("apex_design")) {
         text.addPara("- Nanolaminate Plating", Misc.getNegativeHighlightColor(), 0.0F);
         text.addPara("- Cryocooled Armor Lattice", Misc.getNegativeHighlightColor(), 0.0F);
      }

      text.addPara("- Converted Hangar", Misc.getNegativeHighlightColor(), 0.0F);
      if (Global.getSettings().getModManager().isModEnabled("roider")) {
         text.addPara("- Fighter Clamps", Misc.getNegativeHighlightColor(), 0.0F);
      }

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
      BLOCKED_HULLMODS.add("heavyarmor");
      BLOCKED_HULLMODS.add("apex_armor");
      BLOCKED_HULLMODS.add("apex_cryo_armor");
      BLOCKED_HULLMODS.add("converted_hangar");
      BLOCKED_HULLMODS.add("roider_fighterClamps");
      BLOCKED_HULLMODS.add("dcp_DME_bbassault");
      BLOCKED_HULLMODS.add("dcp_DME_bbdefense");
      BLOCKED_HULLMODS.add("dcp_DME_bbsupport");
   }
}
