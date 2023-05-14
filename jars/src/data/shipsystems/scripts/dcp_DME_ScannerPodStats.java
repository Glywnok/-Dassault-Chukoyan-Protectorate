package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class dcp_DME_ScannerPodStats extends BaseShipSystemScript {
   public static final float SENSOR_RANGE_PERCENT = 50.0F;
   public static final float WEAPON_RANGE_PERCENT = 20.0F;

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      float sensorRangePercent = 50.0F * effectLevel;
      float weaponRangePercent = 20.0F * effectLevel;
      stats.getSightRadiusMod().modifyPercent(id, sensorRangePercent);
      stats.getBallisticWeaponRangeBonus().modifyPercent(id, weaponRangePercent);
      stats.getEnergyWeaponRangeBonus().modifyPercent(id, weaponRangePercent);
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      stats.getSightRadiusMod().unmodify(id);
      stats.getBallisticWeaponRangeBonus().unmodify(id);
      stats.getEnergyWeaponRangeBonus().unmodify(id);
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      float sensorRangePercent = 50.0F * effectLevel;
      float weaponRangePercent = 20.0F * effectLevel;
      if (index == 0) {
         return new StatusData("sensor range +" + (int)sensorRangePercent + "%", false);
      } else if (index == 1) {
         return null;
      } else {
         return index == 2 ? new StatusData("weapon range +" + (int)weaponRangePercent + "%", false) : null;
      }
   }
}
