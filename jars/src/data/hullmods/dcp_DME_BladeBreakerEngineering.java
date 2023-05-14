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

public class dcp_DME_BladeBreakerEngineering extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(6);
   public static final float COST_REDUCTION_LG = 8.0F;
   public static final float COST_REDUCTION_MED = 4.0F;
   public static final float COST_REDUCTION_SM = 2.0F;
   private static final float SUPPLY_USE_MULT = 1.5F;
   private static final float OVERLOAD_DUR_MULT = 1.25F;
   public static final float CORONA_EFFECT_REDUCTION = 0.05F;
   public static final float DAMAGE_MULT = 1.5F;
   public static final float RADIUS_MULT = 0.75F;

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "istl_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getDynamic().getMod("large_ballistic_mod").modifyFlat(id, -8.0F);
      stats.getDynamic().getMod("large_energy_mod").modifyFlat(id, -8.0F);
      stats.getDynamic().getMod("medium_ballistic_mod").modifyFlat(id, -4.0F);
      stats.getDynamic().getMod("medium_energy_mod").modifyFlat(id, -4.0F);
      stats.getDynamic().getMod("small_ballistic_mod").modifyFlat(id, -2.0F);
      stats.getDynamic().getMod("small_energy_mod").modifyFlat(id, -2.0F);
      stats.getSuppliesPerMonth().modifyMult(id, 1.5F);
      stats.getOverloadTimeMod().modifyMult(id, 1.25F);
      stats.getDynamic().getStat("corona_resistance").modifyMult(id, 0.05F);
      stats.getDynamic().getStat("explosion_damage_mult").modifyMult(id, 1.5F);
      stats.getDynamic().getStat("explosion_radius_mult").modifyMult(id, 0.75F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      tooltip.addSectionHeading("Details", Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("BBEngDesc1"), pad, Misc.getHighlightColor(), new String[]{"2", "4", "8 OP"});
      tooltip.addPara("- " + this.getString("BBEngDesc2"), padS, Misc.getHighlightColor(), new String[]{"50%"});
      tooltip.addPara("- " + this.getString("BBEngDesc3"), padS, Misc.getHighlightColor(), new String[]{"25%"});
      tooltip.addPara("- " + this.getString("BBEngDesc4"), padS, Misc.getHighlightColor(), new String[]{"95%"});
      tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/ISTL/icons/tooltip/hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("DMEAllIncomp"), padS);
      text.addPara("- Safety Overrides", Misc.getNegativeHighlightColor(), padS);
      if (Global.getSettings().getModManager().isModEnabled("timid_xiv")) {
         text.addPara("- Aquilla Reactor Protocol", Misc.getNegativeHighlightColor(), 0.0F);
      }

      text.addPara("- Converted Hangar", Misc.getNegativeHighlightColor(), 0.0F);
      if (Global.getSettings().getModManager().isModEnabled("roider")) {
         text.addPara("- Fighter Clamps", Misc.getNegativeHighlightColor(), 0.0F);
      }

      text.addPara("- Integrated Targeting Unit", Misc.getNegativeHighlightColor(), 0.0F);
      text.addPara("- Dedicated Targeting Core", Misc.getNegativeHighlightColor(), 0.0F);
      tooltip.addImageWithText(pad);
   }

   public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
      Iterator var3 = BLOCKED_HULLMODS.iterator();

      while(var3.hasNext()) {
         String tmp = (String)var3.next();
         if (ship.getVariant().getHullMods().contains(tmp)) {
            ship.getVariant().removeMod(tmp);
            DMEBlockedHullmodDisplayScript.showBlocked(ship);
         }
      }

   }

   public boolean affectsOPCosts() {
      return true;
   }

   static {
      BLOCKED_HULLMODS.add("safetyoverrides");
      BLOCKED_HULLMODS.add("eis_aquila");
      BLOCKED_HULLMODS.add("converted_hangar");
      BLOCKED_HULLMODS.add("roider_fighterClamps");
      BLOCKED_HULLMODS.add("targetingunit");
      BLOCKED_HULLMODS.add("dedicated_targeting_core");
   }
}
