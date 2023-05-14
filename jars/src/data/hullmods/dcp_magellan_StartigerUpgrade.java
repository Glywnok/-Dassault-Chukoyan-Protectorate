package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class dcp_magellan_StartigerUpgrade extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(4);
   private static final Map BUILT_IN_WING = new HashMap();
   public static final float HEALTH_BONUS = 100.0F;
   public static final float TURN_PENALTY = 10.0F;
   public static float DMOD_AVOID_CHANCE;
   public static final float DAMAGE_REDUCTION = 0.75F;
   public static final float EMP_EXTRA = 25.0F;

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
      stats.getWeaponHealthBonus().modifyPercent(id, 100.0F);
      stats.getEngineHealthBonus().modifyPercent(id, 50.0F);
      stats.getDynamic().getMod("dmod_acquire_prob_mod").modifyMult(id, 1.0F - DMOD_AVOID_CHANCE * 0.01F);
      stats.getHighExplosiveDamageTakenMult().modifyMult(id, 0.75F);
      stats.getEmpDamageTakenMult().modifyMult(id, 1.25F);
      if (stats.getVariant().hasHullMod("converted_hangar") || stats.getVariant().hasHullMod("roider_fighterClamps")) {
         ShipVariantAPI variant = stats.getVariant();
         variant.setWingId(0, (String)BUILT_IN_WING.get(hullSize));
      }

   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float pad2S = 4.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
      Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
      Color emp_color = dcp_magellan_hullmodUtils.getEMPHLColor();
      Color bad = Misc.getNegativeHighlightColor();
      Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
      tooltip.addSectionHeading(this.getString("MagellanEngTitle"), mag, magbg, Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("MagellanEngDesc1"), pad, h, new String[]{"100%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc3"), padS, h, new String[]{"50%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc4"), padS, h, new String[]{"10%"});
      LabelAPI label = tooltip.addPara("——— " + this.getString("StartigerModTitle") + " ———", mag, pad2S);
      label.setAlignment(Alignment.MID);
      tooltip.addPara("- " + this.getString("StartigerModDesc5"), pad2S, h, new String[]{"25%"});
      LabelAPI intlabel = tooltip.addPara("- " + this.getString("StartigerModDesc6"), padS, h, new String[]{"25%"});
      intlabel.setHighlight(new String[]{this.getString("StartigerMod6HL"), "25%"});
      intlabel.setHighlightColors(new Color[]{emp_color, h});
      tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("MagellanAllIncomp"), padS);
      text.addPara("- Hardened Shields", bad, padS);
      text.addPara("- Armored Weapon Mounts", bad, 0.0F);
      text.addPara("- Converted Hangar", bad, 0.0F);
      if (Global.getSettings().getModManager().isModEnabled("roider")) {
         text.addPara("- Fighter Clamps", bad, 0.0F);
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
      BUILT_IN_WING.put(HullSize.FRIGATE, "magellan_interceptor_startiger_lt_wing");
      BUILT_IN_WING.put(HullSize.DESTROYER, "magellan_interceptor_startiger_wing");
      BUILT_IN_WING.put(HullSize.CRUISER, "magellan_interceptor_startiger_wing");
      BUILT_IN_WING.put(HullSize.CAPITAL_SHIP, "magellan_corvette_startiger_wing");
      DMOD_AVOID_CHANCE = 10.0F;
      BLOCKED_HULLMODS.add("hardenedshieldemitter");
      BLOCKED_HULLMODS.add("armoredweapons");
      BLOCKED_HULLMODS.add("converted_hangar");
      BLOCKED_HULLMODS.add("roider_fighterClamps");
   }
}
