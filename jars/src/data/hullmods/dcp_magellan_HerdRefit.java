package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class dcp_magellan_HerdRefit extends BaseHullMod {
   private final IntervalUtil interval = new IntervalUtil(0.12F, 0.2F);
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(3);
   private static Map speed = new HashMap();
   private static Map accmult = new HashMap();
   public static final float HEALTH_BONUS = 100.0F;
   public static final float TURN_PENALTY = 20.0F;
   public static float DMOD_AVOID_CHANCE;
   public static final float AUTOFIRE_MALUS = -15.0F;
   public static final float FLAMEOUT_CHANCE_SO = 0.01F;
   private Color color = new Color(175, 225, 175, 200);

   public int getDisplaySortOrder() {
      return 0;
   }

   public int getDisplayCategoryIndex() {
      return 0;
   }

   private String getString(String key) {
      return Global.getSettings().getString("Hullmod", "dcp_magellan_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getWeaponHealthBonus().modifyPercent(id, 100.0F);
      stats.getWeaponTurnRateBonus().modifyMult(id, 0.8F);
      stats.getDynamic().getMod("dmod_acquire_prob_mod").modifyMult(id, 1.0F - DMOD_AVOID_CHANCE * 0.01F);
      stats.getMaxSpeed().modifyFlat(id, (Float)speed.get(hullSize));
      stats.getAcceleration().modifyMult(id, 1.0F + (Float)accmult.get(hullSize) / 2.0F);
      stats.getDeceleration().modifyMult(id, (Float)accmult.get(hullSize));
      if (stats.getVariant().hasHullMod("safetyoverrides") || stats.getVariant().hasHullMod("eis_aquila")) {
         stats.getEngineMalfunctionChance().modifyFlat(id, 0.01F);
      }

      stats.getAutofireAimAccuracy().modifyFlat(id, -0.14999999F);
   }

   public void advanceInCombat(ShipAPI ship, float amount) {
      this.interval.advance(amount);
      if (this.interval.intervalElapsed()) {
         float enginejitter = 0.1F + 0.1F * (float)Math.random();
         ship.getEngineController().fadeToOtherColor(this, this.color, (Color)null, 1.0F, enginejitter);
         ship.getEngineController().extendFlame(this, 0.0F, enginejitter, enginejitter);
      }

   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float pad2S = 4.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color bad = Misc.getNegativeHighlightColor();
      Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
      Color herd = dcp_magellan_hullmodUtils.getHerdHLColor();
      Color herdbg = dcp_magellan_hullmodUtils.getHerdBGColor();
      tooltip.addSectionHeading(this.getString("MagellanEngTitle"), herd, herdbg, Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("MagellanEngDesc1"), pad, h, new String[]{"100%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc2"), padS, h, new String[]{"10%"});
      tooltip.addPara("- " + this.getString("MagellanEngDesc4"), padS, h, new String[]{"40%"});
      LabelAPI label = tooltip.addPara("——— " + this.getString("HerdRefitTitle") + " ———", herd, pad2S);
      label.setAlignment(Alignment.MID);
      tooltip.addPara("- " + this.getString("HerdRefitDesc3"), pad2S, h, new String[]{"45", "30", "15", "5"});
      tooltip.addPara("- " + this.getString("HerdRefitDesc4"), padS, h, new String[]{"15%"});
      tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("MagellanAllIncomp"), padS);
      text.addPara("- Hardened Shields", bad, padS);
      text.addPara("- Armored Weapon Mounts", bad, 0.0F);
      text.addPara("- Integrated Targeting Unit", bad, 0.0F);
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
      speed.put(HullSize.FRIGATE, 45.0F);
      speed.put(HullSize.DESTROYER, 30.0F);
      speed.put(HullSize.CRUISER, 15.0F);
      speed.put(HullSize.CAPITAL_SHIP, 5.0F);
      accmult.put(HullSize.FRIGATE, 0.6F);
      accmult.put(HullSize.DESTROYER, 0.5F);
      accmult.put(HullSize.CRUISER, 0.4F);
      accmult.put(HullSize.CAPITAL_SHIP, 0.3F);
      DMOD_AVOID_CHANCE = 40.0F;
      BLOCKED_HULLMODS.add("hardenedshieldemitter");
      BLOCKED_HULLMODS.add("armoredweapons");
      BLOCKED_HULLMODS.add("targetingunit");
   }
}
