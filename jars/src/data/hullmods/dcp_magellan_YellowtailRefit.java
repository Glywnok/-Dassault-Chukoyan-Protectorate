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
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class dcp_magellan_YellowtailRefit extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(4);
   public static final float HEALTH_BONUS = 100.0F;
   public static final float TURN_PENALTY = 10.0F;
   public static float DMOD_AVOID_CHANCE = 30.0F;
   private static Map speed = new HashMap();

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
      stats.getMaxSpeed().modifyFlat(id, (Float)speed.get(hullSize));
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float pad2S = 4.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color bad = Misc.getNegativeHighlightColor();
      Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
      Color tmc = dcp_magellan_hullmodUtils.getTichelHLColor();
      Color tmcbg = dcp_magellan_hullmodUtils.getTichelBGColor();
      tooltip.addSectionHeading(this.getString("MagellanEngTitle"), tmc, tmcbg, Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("MagellanEngDesc1"), pad, h, new String[]{"100%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc3"), padS, h, new String[]{"50%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc4"), padS, h, new String[]{"30%"});
      LabelAPI label = tooltip.addPara("——— " + this.getString("YellowtailModTitle") + " ———", tmc, pad2S);
      label.setAlignment(Alignment.MID);
      tooltip.addPara("- " + this.getString("YellowtailModDesc5"), pad2S, h, new String[]{"30", "20", "12", "4"});
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
      speed.put(HullSize.FRIGATE, 30.0F);
      speed.put(HullSize.DESTROYER, 20.0F);
      speed.put(HullSize.CRUISER, 12.0F);
      speed.put(HullSize.CAPITAL_SHIP, 4.0F);
      BLOCKED_HULLMODS.add("hardenedshieldemitter");
      BLOCKED_HULLMODS.add("armoredweapons");
      BLOCKED_HULLMODS.add("converted_hangar");
      BLOCKED_HULLMODS.add("roider_fighterClamps");
   }
}
