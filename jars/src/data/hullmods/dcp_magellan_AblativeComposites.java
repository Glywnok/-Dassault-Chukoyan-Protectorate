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
import java.util.Map;

public class dcp_magellan_AblativeComposites extends BaseHullMod {
   private static Map damage = new HashMap();
   private static final float EMP_MULT = 0.75F;

   private String getString(String key) {
      return Global.getSettings().getString("Hullmod", "magellan_" + key);
   }

   public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
      stats.getMaxArmorDamageReduction().modifyFlat(id, -0.6F);
      stats.getHullDamageTakenMult().modifyMult(id, (Float)damage.get(hullSize));
      stats.getEngineDamageTakenMult().modifyMult(id, (Float)damage.get(hullSize));
      stats.getWeaponDamageTakenMult().modifyMult(id, (Float)damage.get(hullSize));
      stats.getBeamDamageTakenMult().modifyMult(id, 0.75F);
      stats.getEmpDamageTakenMult().modifyMult(id, 0.75F);
   }

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
      float pad = 10.0F;
      float padS = 2.0F;
      Color h = Misc.getHighlightColor();
      Color emp_color = dcp_magellan_hullmodUtils.getEMPHLColor();
      Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
      Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
      tooltip.addSectionHeading(this.getString("MagellanEffects"), mag, magbg, Alignment.MID, pad);
      tooltip.addPara("- " + this.getString("MagellanArmorDesc1"), pad, h, new String[]{"60%", "25%"});
      tooltip.addPara("- " + this.getString("MagellanArmorDesc2"), padS, h, new String[]{"30%", "40%", "50%"});
      LabelAPI intlabel = tooltip.addPara("- " + this.getString("MagellanArmorDesc3"), padS, h, new String[]{"25%"});
      intlabel.setHighlight(new String[]{this.getString("MagellanArmorDesc3HL"), "25%"});
      intlabel.setHighlightColors(new Color[]{emp_color, h});
   }

   public boolean isApplicableToShip(ShipAPI ship) {
      return ship != null && !ship.isFrigate() && (ship.getVariant().hasHullMod("magellan_engineering") || ship.getVariant().hasHullMod("magellan_engineering_civ") || ship.getVariant().hasHullMod("magellan_blackcollarmod") || ship.getVariant().hasHullMod("magellan_startigermod") || ship.getVariant().hasHullMod("magellan_herdmod")) && super.isApplicableToShip(ship);
   }

   public String getUnapplicableReason(ShipAPI ship) {
      if (ship != null && ship.isFrigate()) {
         return this.getString("MagSpecialCompatFrigate");
      } else {
         return ship.getVariant().hasHullMod("magellan_engineering") && ship.getVariant().hasHullMod("magellan_engineering_civ") && ship.getVariant().hasHullMod("magellan_blackcollarmod") && ship.getVariant().hasHullMod("magellan_startigermod") && ship.getVariant().hasHullMod("magellan_herdmod") ? super.getUnapplicableReason(ship) : this.getString("MagSpecialCompat2");
      }
   }

   static {
      damage.put(HullSize.FRIGATE, 0.0F);
      damage.put(HullSize.DESTROYER, 0.7F);
      damage.put(HullSize.CRUISER, 0.6F);
      damage.put(HullSize.CAPITAL_SHIP, 0.5F);
   }
}
