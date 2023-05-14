package data.hullmods;

import java.util.HashSet;
import java.awt.Color;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.util.Set;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_ClassicDesignB extends BaseHullMod
{
    private static final Set<String> BLOCKED_HULLMODS;
    public static final float HEALTH_BONUS = 100.0f;
    public static final float RANGE_BONUS = 30.0f;
    private static final float MALFUNCTION_DECREASE = 50.0f;

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
        stats.getBallisticWeaponRangeBonus().modifyPercent(id, 30.0f);
        stats.getEnergyWeaponRangeBonus().modifyPercent(id, 30.0f);
        stats.getEngineHealthBonus().modifyPercent(id, 100.0f);
        stats.getCriticalMalfunctionChance().modifyMult(id, 0.5f);
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color clas = dcp_magellan_hullmodUtils.getClassicHLColor();
        final Color clasbg = dcp_magellan_hullmodUtils.getClassicBGColor();
        final Color bad = Misc.getNegativeHighlightColor();
        final Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
        tooltip.addSectionHeading(this.getString("ClassicTitle"), clas, clasbg, Alignment.MID, pad);
        tooltip.addPara("- " + this.getString("ClassicDesc1"), pad, h, new String[] { "30%" });
        tooltip.addPara("- " + this.getString("MagellanEngDesc3"), padS, h, new String[] { "100%" });
        tooltip.addPara("- " + this.getString("BlackcollarModDesc7"), padS, h, new String[] { "50%" });
        tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/hullmod_incompatible.png", 40.0f);
        text.addPara(this.getString("MagellanAllIncomp"), padS);
        text.addPara("- Integrated Targeting Unit", bad, padS);
        text.addPara("- Dedicated Targeting Core", bad, 0.0f);
        tooltip.addImageWithText(pad);
    }

    public void applyEffectsAfterShipCreation(final ShipAPI ship, final String id) {
        for (final String tmp : dcp_magellan_ClassicDesignB.BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                ship.getVariant().removeMod(tmp);
                DCPBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }

    static {
        (BLOCKED_HULLMODS = new HashSet<String>(3)).add("targetingunit");
        dcp_magellan_ClassicDesignB.BLOCKED_HULLMODS.add("dedicated_targeting_core");
    }
}