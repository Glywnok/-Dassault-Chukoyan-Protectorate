package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class dcp_DME_HyperfocusStats extends BaseShipSystemScript {
   public static final float DAMAGE_BONUS_PERCENT = 30.0F;
   public static final float RANGE_BONUS_PERCENT = 60.0F;
   public static final float MANEUVER_MALUS_PERCENT = 30.0F;

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      float bonusPercent = 30.0F * effectLevel;
      stats.getEnergyWeaponDamageMult().modifyPercent(id, bonusPercent);
      float rangePercent = 60.0F * effectLevel;
      stats.getBeamWeaponRangeBonus().modifyPercent(id, rangePercent);
      float thresholdPercent = 60.0F * effectLevel;
      stats.getWeaponRangeThreshold().modifyPercent(id, thresholdPercent);
      stats.getAcceleration().modifyPercent(id, 90.0F);
      stats.getDeceleration().modifyPercent(id, 30.0F);
      stats.getTurnAcceleration().modifyPercent(id, 90.0F);
      stats.getMaxTurnRate().modifyPercent(id, 30.0F);
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      stats.getEnergyWeaponDamageMult().unmodify(id);
      stats.getBeamWeaponRangeBonus().unmodify(id);
      stats.getWeaponRangeThreshold().unmodify(id);
      stats.getAcceleration().unmodify(id);
      stats.getDeceleration().unmodify(id);
      stats.getTurnAcceleration().unmodify(id);
      stats.getMaxTurnRate().unmodify(id);
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      float bonusPercent = 30.0F * effectLevel;
      float rangePercent = 60.0F * effectLevel;
      float speedPercent = 30.0F * effectLevel;
      if (index == 0) {
         return new StatusData("+" + (int)bonusPercent + "% energy weapon damage", false);
      } else if (index == 1) {
         return new StatusData("+" + (int)rangePercent + "% beam weapon range", false);
      } else if (index == 2) {
         return new StatusData("-" + (int)speedPercent + "% turning and acceleration", false);
      } else {
         return index == 3 ? null : null;
      }
   }
}
