package data.shipsystems.scripts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;

public class dcp_FighterComStats extends BaseShipSystemScript {
	public static final Object KEY_JITTER = new Object();
	
	public static final float MAX_DAMAGE_REDUCTION_BONUS = 0.05f;
	public static final float ARMOR_DAMAGE_REDUCTION = 20f;
	public static final float DAMAGE_INCREASE_PERCENT = 30f;
        
	public static final float MANEUVER_INCREASE_PERCENT = 10f;
        public static final float AUTOFIRE_BONUS = 20f;
	
	public static final Color JITTER_COLOR = new Color(175,155,95,255);
	public static final Color JITTER_UNDER_COLOR = new Color(175,155,95,155);

	
        @Override
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}
		
		
		if (effectLevel > 0) {
			float jitterLevel = effectLevel;
			float maxRangeBonus = 5f;
			float jitterRangeBonus = jitterLevel * maxRangeBonus;
			for (ShipAPI fighter : getFighters(ship)) {
				if (fighter.isHulk()) continue;
				MutableShipStatsAPI fStats = fighter.getMutableStats();
	
                                fStats.getMaxArmorDamageReduction().modifyPercent(id, MAX_DAMAGE_REDUCTION_BONUS * effectLevel);
                                fStats.getArmorDamageTakenMult().modifyMult(id, (1f - ARMOR_DAMAGE_REDUCTION / 100f) * effectLevel);
                                				
                                fStats.getBallisticWeaponDamageMult().modifyMult(id, 1f + 0.01f * DAMAGE_INCREASE_PERCENT * effectLevel);
				fStats.getEnergyWeaponDamageMult().modifyMult(id, 1f + 0.01f * DAMAGE_INCREASE_PERCENT * effectLevel);
				fStats.getMissileWeaponDamageMult().modifyMult(id, 1f + 0.01f * DAMAGE_INCREASE_PERCENT * effectLevel);
                                
                                fStats.getAutofireAimAccuracy().modifyFlat(id, AUTOFIRE_BONUS * 0.01f);
                                fStats.getMaxRecoilMult().modifyMult(id, 1f - AUTOFIRE_BONUS * 0.01f);
                                fStats.getRecoilPerShotMult().modifyMult(id, 1f - AUTOFIRE_BONUS * 0.01f);
                                
                                fStats.getDeceleration().modifyPercent(id, MANEUVER_INCREASE_PERCENT * effectLevel);
                                fStats.getMaxTurnRate().modifyPercent(id, MANEUVER_INCREASE_PERCENT * effectLevel);
                                fStats.getTurnAcceleration().modifyPercent(id, MANEUVER_INCREASE_PERCENT * effectLevel);
                                

                                
				if (jitterLevel > 0) {
					fighter.setWeaponGlow(effectLevel, Misc.setAlpha(JITTER_UNDER_COLOR, 105), EnumSet.allOf(WeaponType.class));

					fighter.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 3, 0f, jitterRangeBonus);
					fighter.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 1, 0f, 0 + jitterRangeBonus * 1f);
					Global.getSoundPlayer().playLoop("system_targeting_feed_loop", ship, 1f, 1f, fighter.getLocation(), fighter.getVelocity());
				}
			}
		}
	}
	
	private List<ShipAPI> getFighters(ShipAPI carrier) {
		List<ShipAPI> result = new ArrayList<ShipAPI>();
		
//		this didn't catch fighters returning for refit		
//		for (FighterLaunchBayAPI bay : carrier.getLaunchBaysCopy()) {
//			if (bay.getWing() == null) continue;
//			result.addAll(bay.getWing().getWingMembers());
//		}
		
		for (ShipAPI ship : Global.getCombatEngine().getShips()) {
			if (!ship.isFighter()) continue;
			if (ship.getWing() == null) continue;
			if (ship.getWing().getSourceShip() == carrier) {
				result.add(ship);
			}
		}
		
		return result;
	}
	
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
		} else {
			return;
		}
		for (ShipAPI fighter : getFighters(ship)) {
			if (fighter.isHulk()) continue;
			MutableShipStatsAPI fStats = fighter.getMutableStats();
			fStats.getBallisticWeaponDamageMult().unmodify(id);
			fStats.getEnergyWeaponDamageMult().unmodify(id);
			fStats.getMissileWeaponDamageMult().unmodify(id);
		}
	}
	
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float percent = DAMAGE_INCREASE_PERCENT * effectLevel;
		if (index == 0) {
			//return new StatusData("+" + (int)percent + "% fighter damage", false);
			return new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(1f + DAMAGE_INCREASE_PERCENT * effectLevel * 0.01f) + "x fighter damage", false);
		}
                if (index == 1) {
			//return new StatusData("+" + (int)percent + "% fighter damage", false);
			return new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(ARMOR_DAMAGE_REDUCTION * effectLevel) + "% reduction to fighter armor damage", false);
		}
                if (index == 2) {
			//return new StatusData("+" + (int)percent + "% fighter damage", false);
			return new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(MANEUVER_INCREASE_PERCENT * effectLevel) + "% more fighter agility", false);
		}
                if (index == 3) {
			//return new StatusData("+" + (int)percent + "% fighter damage", false);
			return new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(AUTOFIRE_BONUS * effectLevel) + "% more fighter accuracy", false);
		}
		return null;
	}

	
}








