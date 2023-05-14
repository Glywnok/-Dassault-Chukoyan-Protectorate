package data.hullmods;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import com.fs.starfarer.api.ui.LabelAPI;
import java.awt.Color;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.util.Map;
import java.util.Set;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_LevellerRefit extends BaseHullMod
{
    private static final Set<String> BLOCKED_HULLMODS;
    public static final float HEALTH_BONUS = 100.0f;
    public static final float TURN_PENALTY = 10.0f;
    private static Map mag;
    public static final int ENERGY_RANGE_BONUS = 200;
    public static final float MANEUVER_BONUS = 25.0f;

    public int getDisplaySortOrder() {
        return 0;
    }

    public int getDisplayCategoryIndex() {
        return 0;
    }

    private String getString(final String key) {
        return Global.getSettings().getString("Hullmod", "magellan_" + key);
    }

    public void applyEffectsBeforeShipCreation(final ShipAPI.HullSize hullSize, final MutableShipStatsAPI stats, final String id) {
        stats.getWeaponHealthBonus().modifyPercent(id, 100.0f);
        stats.getWeaponTurnRateBonus().modifyMult(id, 0.9f);
        stats.getEnergyWeaponRangeBonus().modifyFlat(id, 200.0f);
        stats.getFluxDissipation().modifyFlat(id, (float)dcp_magellan_LevellerRefit.mag.get(hullSize));
        stats.getAcceleration().modifyPercent(id, 50.0f);
        stats.getDeceleration().modifyPercent(id, 25.0f);
        stats.getTurnAcceleration().modifyPercent(id, 50.0f);
        stats.getMaxTurnRate().modifyPercent(id, 25.0f);
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float pad2S = 4.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color bad = Misc.getNegativeHighlightColor();
        final Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
        final Color lev = dcp_magellan_hullmodUtils.getLevellerHLColor();
        final Color levbg = dcp_magellan_hullmodUtils.getLevellerBGColor();
        tooltip.addSectionHeading(this.getString("MagellanEngTitle"), lev, levbg, Alignment.MID, pad);
        tooltip.addPara("- " + this.getString("MagellanEngDesc1"), pad, h, new String[] { "100%" });
        tooltip.addPara("- " + this.getString("MagellanEngDesc2"), padS, h, new String[] { "10%" });
        final LabelAPI label = tooltip.addPara("\u2014\u2014\u2014 " + this.getString("LevellerRefitTitle") + " \u2014\u2014\u2014", lev, pad2S);
        label.setAlignment(Alignment.MID);
        tooltip.addPara("- " + this.getString("LevellerRefitDesc2"), pad2S, h, new String[] { "200su" });
        tooltip.addPara("- " + this.getString("LevellerRefitDesc3"), padS, h, new String[] { "30", "60", "90", "150" });
        tooltip.addPara("- " + this.getString("LevellerRefitDesc4"), padS, h, new String[] { "25%" });
        tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/hullmod_incompatible.png", 40.0f);
        text.addPara(this.getString("MagellanAllIncomp"), padS);
        text.addPara("- Armored Weapon Mounts", bad, 0.0f);
        text.addPara("- Converted Hangar", bad, 0.0f);
        if (Global.getSettings().getModManager().isModEnabled("roider")) {
            text.addPara("- Fighter Clamps", bad, 0.0f);
        }
        tooltip.addImageWithText(pad);
    }

    public void applyEffectsAfterShipCreation(final ShipAPI ship, final String id) {
        for (final String tmp : dcp_magellan_LevellerRefit.BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                ship.getVariant().removeMod(tmp);
                DCPBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }

    static {
        BLOCKED_HULLMODS = new HashSet<String>(3);
        (dcp_magellan_LevellerRefit.mag = new HashMap()).put(ShipAPI.HullSize.FRIGATE, 30.0f);
        dcp_magellan_LevellerRefit.mag.put(ShipAPI.HullSize.DESTROYER, 60.0f);
        dcp_magellan_LevellerRefit.mag.put(ShipAPI.HullSize.CRUISER, 90.0f);
        dcp_magellan_LevellerRefit.mag.put(ShipAPI.HullSize.CAPITAL_SHIP, 150.0f);
        dcp_magellan_LevellerRefit.BLOCKED_HULLMODS.add("armoredweapons");
        dcp_magellan_LevellerRefit.BLOCKED_HULLMODS.add("converted_hangar");
        dcp_magellan_LevellerRefit.BLOCKED_HULLMODS.add("roider_fighterClamps");
    }
}