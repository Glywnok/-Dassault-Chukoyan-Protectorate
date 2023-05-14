package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class dcp_magellan_TargetingDroneStats extends BaseShipSystemScript {
   public static final float SENSOR_RANGE_PERCENT = 10.0F;
   public static final float WEAPON_RANGE_FLAT = 100.0F;
   public static final float RECOIL_MULT = 0.3F;

   private String getString(String key) {
      return Global.getSettings().getString("System", "magellan_" + key);
   }

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      float sensorRangePercent = 10.0F * effectLevel;
      float weaponRangeFlat = 100.0F * effectLevel;
      float recoilMult = 0.3F * effectLevel;
      stats.getSightRadiusMod().modifyPercent(id, sensorRangePercent);
      stats.getBallisticWeaponRangeBonus().modifyFlat(id, weaponRangeFlat);
      stats.getEnergyWeaponRangeBonus().modifyFlat(id, weaponRangeFlat);
      stats.getMaxRecoilMult().modifyMult(id, recoilMult);
      stats.getRecoilPerShotMult().modifyMult(id, recoilMult);
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      stats.getSightRadiusMod().unmodify(id);
      stats.getBallisticWeaponRangeBonus().unmodify(id);
      stats.getEnergyWeaponRangeBonus().unmodify(id);
      stats.getMaxRecoilMult().unmodify(id);
      stats.getRecoilPerShotMult().unmodify(id);
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      float sensorRangePercent = 10.0F * effectLevel;
      float weaponRangeFlat = 100.0F * effectLevel;
      float recoilMult = 0.3F * effectLevel;
      if (index == 0) {
         return new StatusData(this.getString("targetdrone_str1") + " +" + (int)sensorRangePercent + "%", false);
      } else if (index == 1) {
         return new StatusData(this.getString("targetdrone_str2") + " +" + (int)weaponRangeFlat + "su", false);
      } else {
         return index == 2 ? new StatusData(this.getString("targetdrone_str3") + " -" + (int)(100.0F * recoilMult) + "%", false) : null;
      }
   }
}
