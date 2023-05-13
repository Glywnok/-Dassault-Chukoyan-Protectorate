package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;

public class dcp_BallisticAccelStats extends BaseShipSystemScript {

	public static final float ROF_BONUS = 0.6f;
	public static final float FLUX_REDUCTION = 30f;
        public static final float RANGE_INCREASE = 100;
	public static final float PROJ_SPEED_BONUS = 30;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		
		float mult = 1f + ROF_BONUS * effectLevel;
		stats.getBallisticRoFMult().modifyMult(id, mult);
		stats.getBallisticWeaponFluxCostMod().modifyPercent(id, -FLUX_REDUCTION);
                stats.getBallisticWeaponRangeBonus().modifyFlat(id, RANGE_INCREASE * effectLevel);
                stats.getProjectileSpeedMult().modifyPercent(id, PROJ_SPEED_BONUS * effectLevel);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getBallisticRoFMult().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
                stats.getBallisticWeaponRangeBonus().unmodify(id);
                stats.getProjectileSpeedMult().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float mult = 1f + ROF_BONUS * effectLevel;
		float bonusPercent = (int) ((mult - 1f) * 100f);
		if (index == 0) {
			return new StatusData("ballistic rate of fire +" + (int) bonusPercent + "%", false);
		}
		if (index == 1) {
			return new StatusData("ballistic flux use -" + (int) FLUX_REDUCTION + "%", false);
		}
                if (index == 2) {
			return new StatusData("ballistic range increase +" + Misc.getRoundedValueMaxOneAfterDecimal(RANGE_INCREASE * effectLevel) + "su", false);
		}            
		return null;
	}
}
