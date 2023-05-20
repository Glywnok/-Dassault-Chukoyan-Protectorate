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
import data.scripts.DCPUtils;
import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class dcp_magellan_SpartacusReactor extends BaseHullMod {
   private static final Set<String> BLOCKED_HULLMODS = new HashSet(1);
   public static final float COST_REDUCTION_LG = 8.0F;
   public static final float COST_REDUCTION_MED = 4.0F;
   public static final float COST_REDUCTION_SM = 2.0F;
   public static final int ENERGY_RANGE_BONUS = 200;
   public static final Color JITTER_COLOR = new Color(50, 60, 255, 100);
   public static final Color JITTER_UNDER_COLOR = new Color(50, 60, 255, 155);

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
      stats.getDynamic().getMod("large_energy_mod").modifyFlat(id, -8.0F);
      stats.getDynamic().getMod("medium_energy_mod").modifyFlat(id, -4.0F);
      stats.getDynamic().getMod("small_energy_mod").modifyFlat(id, -2.0F);
      stats.getDynamic().getStat("replacement_rate_decrease_mult").modifyMult(id, 0.0F);
      stats.getEnergyWeaponRangeBonus().modifyFlat(id, 200.0F);
      if (stats.getVariant().hasHullMod("safetyoverrides") || stats.getVariant().hasHullMod("eis_aquila")) {
         stats.getShieldDamageTakenMult().modifyMult(id, 1.2F);
         stats.getOverloadTimeMod().modifyMult(id, 1.5F);
         stats.getShieldMalfunctionChance().modifyFlat(id, 0.01F);
         stats.getShieldMalfunctionFluxLevel().modifyFlat(id, 0.95F);
         stats.getDynamic().getStat("explosion_damage_mult").modifyMult(id, 3.0F);
         stats.getDynamic().getStat("explosion_radius_mult").modifyMult(id, 1.5F);
      }

   }

   public void advanceInCombat(ShipAPI ship, float amount) {
      MutableShipStatsAPI stats = ship.getMutableStats();
      String id = "dcp_magellan_SpartacusReactor";
      float fluxlevel = ship.getFluxLevel();
      float hardfluxlevel = ship.getHardFluxLevel();
      stats.getEmpDamageTakenMult().modifyMult(id, 0.25F + 1.0F * fluxlevel);
      stats.getDynamic().getStat("explosion_radius_mult").modifyMult(id, 0.75F + 0.5F * hardfluxlevel);
      float jitterLevel = 0.0F;
      float jitterLevel2 = 0.0F;
      float jitterRangeBonus = 0.0F;
      float maxRangeBonus = 10.0F;
      if (fluxlevel < 0.7F) {
         jitterLevel = 0.0F;
      } else if (fluxlevel >= 0.7F) {
         jitterLevel = DCPUtils.lerp(0.0F, fluxlevel, -3.0F + 4.0F * hardfluxlevel);
         if (jitterLevel > 1.0F) {
            jitterLevel = 1.0F;
         }

         jitterRangeBonus = jitterLevel * maxRangeBonus;
      }

      jitterLevel2 = (float)Math.sqrt((double)jitterLevel);
      ship.setJitter(this, JITTER_COLOR, jitterLevel2, 3, 0.0F, 0.0F + jitterRangeBonus);
      ship.setJitterUnder(this, JITTER_UNDER_COLOR, jitterLevel2, 25, 0.0F, 7.0F + jitterRangeBonus);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color bad = Misc.getNegativeHighlightColor();
      Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
      Color emp_color = dcp_magellan_hullmodUtils.getEMPHLColor();
      Color lev = dcp_magellan_hullmodUtils.getLevellerHLColor();
      Color levbg = dcp_magellan_hullmodUtils.getLevellerBGColor();
      tooltip.addSectionHeading(this.getString("SpartacusReactorTitle"), lev, levbg, Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("SpartacusReactorDesc1"), pad, h, new String[]{"2", "4", "8 OP"});
      tooltip.addPara("- " + this.getString("ClassicDesc2"), padS, h, new String[]{this.getString("Classic2HL")});
      tooltip.addPara("- " + this.getString("LevellerRefitDesc2"), padS, h, new String[]{"200su"});
      LabelAPI intlabel = tooltip.addPara("- " + this.getString("SpartacusReactorDesc3"), padS, h, new String[]{"25%", "125%"});
      intlabel.setHighlight(new String[]{this.getString("SpartacusReactor3HL"), "25%", "125%"});
      intlabel.setHighlightColors(new Color[]{emp_color, h, h});
      tooltip.addPara("- " + this.getString("SpartacusReactorDesc4"), padS, h, new String[]{this.getString("SpartacusReactor4HL"), "25%"});
      tooltip.addSectionHeading(this.getString("magellanIncompTitle"), bad, badbg, Alignment.MID, pad);
      TooltipMakerAPI text = tooltip.beginImageWithText("graphics/DCP/icons/tooltip/hullmod_incompatible.png", 40.0F);
      text.addPara(this.getString("magellanAllIncomp"), padS);
      text.addPara("- Expanded Deck Crew", bad, padS);
      text.addPara("- Resistant Flux Conduits", bad, padS);
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

   public boolean isApplicableToShip(ShipAPI ship) {
      if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_magellan_core_hullmod")) {
         return false;
      } else {
         return ship != null && !ship.isFrigate() && super.isApplicableToShip(ship);
      }
   }

   public String getUnapplicableReason(ShipAPI ship) {
      if (ship != null && ship.isFrigate()) {
         return this.getString("MagSpecialCompatFrigate");
      } else {
         return this.shipHasOtherModInCategory(ship, this.spec.getId(), "dcp_magellan_core_hullmod") ? this.getString("MagSpecialCompat3") : super.getUnapplicableReason(ship);
      }
   }

   public boolean affectsOPCosts() {
      return true;
   }

   static {
      BLOCKED_HULLMODS.add("expanded_deck_crew");
      BLOCKED_HULLMODS.add("fluxbreakers");
   }
}
