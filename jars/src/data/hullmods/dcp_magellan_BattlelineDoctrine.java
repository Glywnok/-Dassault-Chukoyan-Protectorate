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

public class dcp_magellan_BattlelineDoctrine extends BaseHullMod {
   public static float PROJ_SPEED_BONUS = 20.0F;
   public static final float PD_BONUS = 100.0F;
   public static Color BORDER = new Color(147, 102, 50, 0);
   public static Color NAME = new Color(153, 134, 117, 255);

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "dcp_magellan_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getProjectileSpeedMult().modifyPercent(id, PROJ_SPEED_BONUS);
      stats.getNonBeamPDWeaponRangeBonus().modifyFlat(id, 100.0F);
      stats.getDamageToFighters().modifyPercent(id, 10.0F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color quote = dcp_magellan_hullmodUtils.getQuoteColor();
      Color attrib = Misc.getGrayColor();
      tooltip.addSectionHeading("Technical Details", Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("ComCrewDesc1"), pad, h, new String[]{"20%"});
      tooltip.addPara("- " + this.getString("ComCrewDesc2"), padS, h, new String[]{"100su"});
      tooltip.addPara("- " + this.getString("ComCrewDesc3"), padS, h, new String[]{"10%"});
      LabelAPI label = tooltip.addPara(this.getString("ComCrewQuote"), quote, pad);
      label.italicize(0.12F);
      tooltip.addPara("      " + this.getString("MagellanEmDash") + this.getString("ComCrewAttrib"), attrib, padS);
   }

   public Color getBorderColor() {
      return BORDER;
   }

   public Color getNameColor() {
      return NAME;
   }
}
