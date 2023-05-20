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

public class dcp_magellan_LevellerRefit extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(3);
   public static final float HEALTH_BONUS = 100.0F;
   public static final float TURN_PENALTY = 10.0F;
   private static Map mag = new HashMap();
   public static final int ENERGY_RANGE_BONUS = 200;
   public static final float MANEUVER_BONUS = 25.0F;

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
      stats.getWeaponHealthBonus().modifyPercent(id, 100.0F);
      stats.getWeaponTurnRateBonus().modifyMult(id, 0.9F);
      stats.getEnergyWeaponRangeBonus().modifyFlat(id, 200.0F);
      stats.getFluxDissipation().modifyFlat(id, (Float)mag.get(hullSize));
      stats.getAcceleration().modifyPercent(id, 50.0F);
      stats.getDeceleration().modifyPercent(id, 25.0F);
      stats.getTurnAcceleration().modifyPercent(id, 50.0F);
      stats.getMaxTurnRate().modifyPercent(id, 25.0F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float pad2S = 4.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color bad = Misc.getNegativeHighlightColor();
      Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
      Color lev = dcp_magellan_hullmodUtils.getLevellerHLColor();
      Color levbg = dcp_magellan_hullmodUtils.getLevellerBGColor();
      tooltip.addSectionHeading(this.getString("MagellanEngTitle"), lev, levbg, Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("MagellanEngDesc1"), pad, h, new String[]{"100%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc2"), padS, h, new String[]{"10%"});
      LabelAPI label = tooltip.addPara("——— " + this.getString("LevellerRefitTitle") + " ———", lev, pad2S);
      label.setAlignment(Alignment.MID);
      tooltip.addPara("- " + this.getString("LevellerRefitDesc2"), pad2S, h, new String[]{"200su"});
      tooltip.addPara("- " + this.getString("LevellerRefitDesc3"), padS, h, new String[]{"30", "60", "90", "150"});
      tooltip.addPara("- " + this.getString("LevellerRefitDesc4"), padS, h, new String[]{"25%"});
      tooltip.addSectionHeading(this.getString("magellanIncompTitle"), bad, badbg, Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("magellanAllIncomp"), padS);
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
      mag.put(HullSize.FRIGATE, 30.0F);
      mag.put(HullSize.DESTROYER, 60.0F);
      mag.put(HullSize.CRUISER, 90.0F);
      mag.put(HullSize.CAPITAL_SHIP, 150.0F);
      BLOCKED_HULLMODS.add("armoredweapons");
      BLOCKED_HULLMODS.add("converted_hangar");
      BLOCKED_HULLMODS.add("roider_fighterClamps");
   }
}
