package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;

public class dcp_magellan_BallisticAccelStats extends BaseShipSystemScript {
   public static final float ROF_BONUS = 0.6F;
   public static final float FLUX_REDUCTION = 30.0F;
   public static final float RANGE_INCREASE = 100.0F;
   public static final float PROJ_SPEED_BONUS = 30.0F;

   private String getString(String key) {
      return Global.getSettings().getString("System", "magellan_" + key);
   }

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      float mult = 1.0F + 0.6F * effectLevel;
      stats.getBallisticRoFMult().modifyMult(id, mult);
      stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -30.0F);
      stats.getBallisticWeaponRangeBonus().modifyFlat(id, 100.0F * effectLevel);
      stats.getBallisticProjectileSpeedMult().modifyPercent(id, 30.0F * effectLevel);
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      stats.getBallisticRoFMult().unmodify(id);
      stats.getBallisticWeaponFluxCostMod().unmodify(id);
      stats.getBallisticWeaponRangeBonus().unmodify(id);
      stats.getBallisticProjectileSpeedMult().unmodify(id);
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      float mult = 1.0F + 0.6F * effectLevel;
      float bonusPercent = (float)((int)((mult - 1.0F) * 100.0F));
      if (index == 0) {
         return new StatusData(this.getString("ballisticaccel_str1") + " +" + (int)bonusPercent + "%", false);
      } else if (index == 1) {
         return new StatusData(this.getString("ballisticaccel_str2") + " -" + 30 + "%", false);
      } else {
         return index == 2 ? new StatusData(this.getString("ballisticaccel_str3") + " +" + Misc.getRoundedValueMaxOneAfterDecimal(100.0F * effectLevel) + "su", false) : null;
      }
   }
}
