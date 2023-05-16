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

public class dcp_DME_ExperiencedDamageControl extends BaseHullMod {
   public static final float RATE_DECREASE_MODIFIER = 15.0F;
   public static final float CREW_LOSS_MULT = 0.15F;
   public static final float REPAIR_BONUS = 15.0F;

   private String getString(String key) {
      return Global.getSettings().getString("HullMod", "dcp_DME_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getDynamic().getStat("replacement_rate_decrease_mult").modifyMult(id, 0.85F);
      stats.getCrewLossMult().modifyMult(id, 0.85F);
      stats.getDynamic().getStat("fighter_crew_loss_mult").modifyMult(id, 0.15F);
      stats.getCombatEngineRepairTimeMult().modifyMult(id, 0.85F);
      stats.getCombatWeaponRepairTimeMult().modifyMult(id, 0.85F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      tooltip.addSectionHeading("Details", Alignment.MID, pad);
      tooltip.addPara(this.getString("ComCrewDesc1"), pad, Misc.getHighlightColor(), new String[]{"15%", "15%"});
      tooltip.addPara(this.getString("ComCrewDesc2"), padS, Misc.getHighlightColor(), new String[]{"15%"});
   }

   public Color getBorderColor() {
      return new Color(147, 102, 50, 0);
   }

   public Color getNameColor() {
      return new Color(76, 113, 175, 255);
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      int bays = (int)ship.getMutableStats().getNumFighterBays().getBaseValue();
      return ship != null && bays > 0;
   }

   public String getUnapplicableReason(ShipAPI ship) {
      return "Ship does not have standard fighter bays";
   }
}
