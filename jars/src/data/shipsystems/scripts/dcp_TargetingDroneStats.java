/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
/**
 *
 * @author Soren
 */
public class dcp_TargetingDroneStats extends BaseShipSystemScript {

	public static final float SENSOR_RANGE_PERCENT = 10f;
	public static final float WEAPON_RANGE_FLAT = 100f;
	public static final float RECOIL_MULT = 0.3f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float sensorRangePercent = SENSOR_RANGE_PERCENT * effectLevel;
		float weaponRangeFlat = WEAPON_RANGE_FLAT * effectLevel;
		float recoilMult = RECOIL_MULT * effectLevel;
		
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
		float sensorRangePercent = SENSOR_RANGE_PERCENT * effectLevel;
		float weaponRangeFlat = WEAPON_RANGE_FLAT * effectLevel;
		float recoilMult = RECOIL_MULT * effectLevel;
		if (index == 0) {
			return new StatusData("sensor range +" + (int) sensorRangePercent + "%", false);
		} else if (index == 1) {
			return new StatusData("weapon range +" + (int) weaponRangeFlat + "su", false);
		} else if (index == 2) {
			return new StatusData("recoil reduced by +" + (int) (100 * recoilMult) + "%", false);
		}
		return null;
	}
}
