/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author HarmfulMechanic
 */
public class dcp_magellan_levellerRefit extends BaseHullMod {
        private String getString(String key) {
        return Global.getSettings().getString("HullMod", "magellan_" + key);}

	private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(2);
	
        public static final float HEALTH_BONUS = 100f;
	public static final float TURN_PENALTY = 10f;
        private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 30f);
		mag.put(HullSize.DESTROYER, 60f);
		mag.put(HullSize.CRUISER, 90f);
		mag.put(HullSize.CAPITAL_SHIP, 150f);
	}
        public static final float MANEUVER_BONUS = 25f;
        
        static
        {
            // These hullmods will automatically be removed
            // Not as elegant as blocking them in the first place, but
            // this method doesn't require editing every hullmod's script
            BLOCKED_HULLMODS.add("fluxdistributor"); // No more flux upgrades, sorry.
            BLOCKED_HULLMODS.add("armoredweapons"); // No more uparmoring those mounts.
        }
        
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS);
		stats.getWeaponTurnRateBonus().modifyMult(id, 1f - TURN_PENALTY * 0.01f);
		stats.getFluxDissipation().modifyFlat(id, (Float) mag.get(hullSize));
                stats.getAcceleration().modifyPercent(id, MANEUVER_BONUS * 2f);
		stats.getDeceleration().modifyPercent(id, MANEUVER_BONUS);
		stats.getTurnAcceleration().modifyPercent(id, MANEUVER_BONUS * 2f);
		stats.getMaxTurnRate().modifyPercent(id, MANEUVER_BONUS);
	}
        
        @Override
        public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
            float pad = 10f;
            float padS = 2f;
            tooltip.addSectionHeading("Incompatibilities", Alignment.MID, pad);
            tooltip.addPara("%s " + getString("MagellanAllIncomp"), pad, Misc.getNegativeHighlightColor(), "-", "Flux Distributor, Armored Weapon Mounts");
            tooltip.addSectionHeading("Details", Alignment.MID, pad);
            tooltip.addPara("%s " + getString("MagellanEngDesc1"), pad, Misc.getHighlightColor(), "-", "100%");
            tooltip.addPara("%s " + getString("MagellanEngDesc2"), padS, Misc.getHighlightColor(), "-", "10%");
            tooltip.addSectionHeading("Refit Effects", Alignment.MID, pad);
            tooltip.addPara("%s " + getString("LevellerRefitDesc3"), pad, Misc.getHighlightColor(), "-", "30", "60", "90", "150");
            tooltip.addPara("%s " + getString("LevellerRefitDesc4"), padS, Misc.getHighlightColor(), "-", "25%");
        }
        
            @Override
            public void applyEffectsAfterShipCreation(ShipAPI ship, String id)
        {
            for (String tmp : BLOCKED_HULLMODS)
            {
                if (ship.getVariant().getHullMods().contains(tmp))
                {
                    ship.getVariant().removeMod(tmp);
                    DCPBlockedHullmodDisplayScript.showBlocked(ship);
                }
            }
        }
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HEALTH_BONUS + "%";
		if (index == 1) return "" + (int) TURN_PENALTY + "%";
		if (index == 2) return "" + (int) (HEALTH_BONUS/2) + "%";
		return null;
	}


}
