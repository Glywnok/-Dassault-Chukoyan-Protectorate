package data.hullmods;

import java.util.HashSet;

import com.fs.starfarer.api.ui.LabelAPI;
import java.awt.Color;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.util.Set;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_DemilitarizedRefit extends BaseHullMod
{
    private static final Set<String> BLOCKED_HULLMODS;
    public static final float HEALTH_BONUS = 50.0f;
    public static float DMOD_AVOID_CHANCE;
    private static final float PROFILE_INCREASE = 50.0f;
    private static final float STRENGTH_DECREASE = 25.0f;
    public static final float MAINTENANCE_MULT = 0.8f;

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
        stats.getWeaponHealthBonus().modifyPercent(id, 50.0f);
        stats.getEngineHealthBonus().modifyPercent(id, 50.0f);
        stats.getDynamic().getMod("dmod_acquire_prob_mod").modifyMult(id, 1.0f - dcp_magellan_DemilitarizedRefit.DMOD_AVOID_CHANCE * 0.01f);
        stats.getSensorProfile().modifyPercent(id, 50.0f);
        stats.getSensorStrength().modifyMult(id, 0.75f);
        stats.getMinCrewMod().modifyMult(id, 0.8f);
        stats.getSuppliesPerMonth().modifyMult(id, 0.8f);
        stats.getFuelUseMod().modifyMult(id, 0.8f);
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float pad2S = 4.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
        final Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
        final Color bad = Misc.getNegativeHighlightColor();
        final Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
        tooltip.addSectionHeading(this.getString("MagellanEngTitle"), mag, magbg, Alignment.MID, pad);
        tooltip.addPara("- " + this.getString("MagellanEngDesc1"), pad, h, new String[] { "50%" });
        tooltip.addPara("- " + this.getString("MagellanEngDesc3"), padS, h, new String[] { "50%" });
        tooltip.addPara("- " + this.getString("MagellanEngDesc4"), padS, h, new String[] { "25%" });
        final LabelAPI label = tooltip.addPara("\u2014\u2014\u2014 " + this.getString("DemilTitle") + " \u2014\u2014\u2014", mag, pad2S);
        label.setAlignment(Alignment.MID);
        tooltip.addPara("- " + this.getString("DemilDesc5"), pad2S, h, new String[] { "50%" });
        tooltip.addPara("- " + this.getString("DemilDesc6"), padS, h, new String[] { "25%" });
        tooltip.addPara("- " + this.getString("DemilDesc7"), padS, h, new String[] { "20%" });
        tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/hullmod_incompatible.png", 40.0f);
        text.addPara(this.getString("MagellanAllIncomp"), padS);
        text.addPara("- Hardened Shields", bad, padS);
        text.addPara("- Armored Weapon Mounts", bad, 0.0f);
        tooltip.addImageWithText(pad);
    }

    public void applyEffectsAfterShipCreation(final ShipAPI ship, final String id) {
        for (final String tmp : dcp_magellan_DemilitarizedRefit.BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                ship.getVariant().removeMod(tmp);
                DCPBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }

    static {
        BLOCKED_HULLMODS = new HashSet<String>(2);
        dcp_magellan_DemilitarizedRefit.DMOD_AVOID_CHANCE = 25.0f;
        dcp_magellan_DemilitarizedRefit.BLOCKED_HULLMODS.add("hardenedshieldemitter");
        dcp_magellan_DemilitarizedRefit.BLOCKED_HULLMODS.add("armoredweapons");
    }
}