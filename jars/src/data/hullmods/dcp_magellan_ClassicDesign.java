package data.hullmods;

import java.util.HashSet;

import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.DMEUtils;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.awt.Color;
import java.util.Set;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_ClassicDesign extends BaseHullMod
{
    private static final Set<String> BLOCKED_HULLMODS;
    public static final Color ZERO_FLUX_RING;
    public static final Color ZERO_FLUX_INNER;
    public static final Color FULL_FLUX_RING;
    public static final Color FULL_FLUX_INNER;
    public static final float HEALTH_BONUS = 100.0f;
    public static final float RANGE_BONUS = 50.0f;
    private static final float MALFUNCTION_DECREASE = 25.0f;
    private static final float EXTRA_MODS = 1.0f;

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
        stats.getBallisticWeaponRangeBonus().modifyPercent(id, 50.0f);
        stats.getEnergyWeaponRangeBonus().modifyPercent(id, 50.0f);
        stats.getDynamic().getStat("replacement_rate_decrease_mult").modifyMult(id, 0.0f);
        stats.getEngineHealthBonus().modifyPercent(id, 100.0f);
        stats.getCriticalMalfunctionChance().modifyMult(id, 0.75f);
        stats.getDynamic().getMod("max_permanent_hullmods_mod").modifyFlat(id, 1.0f);
    }

    public void advanceInCombat(final ShipAPI ship, final float amount) {
        if (ship.getShield() != null) {
            final float hardflux_track = ship.getHardFluxLevel();
            float outputColorLerp = 0.0f;
            if (hardflux_track < 0.5f) {
                outputColorLerp = 0.0f;
            }
            else if (hardflux_track >= 0.5f) {
                outputColorLerp = DMEUtils.lerp(0.0f, hardflux_track, hardflux_track);
            }
            final Color color1 = Misc.interpolateColor(dcp_magellan_ClassicDesign.ZERO_FLUX_RING, dcp_magellan_ClassicDesign.FULL_FLUX_RING, Math.min(outputColorLerp, 1.0f));
            final Color color2 = Misc.interpolateColor(dcp_magellan_ClassicDesign.ZERO_FLUX_INNER, dcp_magellan_ClassicDesign.FULL_FLUX_INNER, Math.min(outputColorLerp, 1.0f));
            ship.getShield().setRingColor(color1);
            ship.getShield().setInnerColor(color2);
        }
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
        tooltip.addPara("- " + this.getString("ClassicDesc1"), pad, h, new String[] { "50%" });
        tooltip.addPara("- " + this.getString("ClassicDesc2"), padS, h, new String[] { this.getString("Classic2HL") });
        tooltip.addPara("- " + this.getString("ClassicDesc3"), padS, h, new String[] { "1" });
        tooltip.addPara("- " + this.getString("MagellanEngDesc3"), padS, h, new String[] { "100%" });
        tooltip.addPara("- " + this.getString("BlackcollarModDesc7"), padS, h, new String[] { "25%" });
        tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/hullmod_incompatible.png", 40.0f);
        text.addPara(this.getString("MagellanAllIncomp"), padS);
        text.addPara("- Integrated Targeting Unit", bad, padS);
        text.addPara("- Dedicated Targeting Core", bad, 0.0f);
        text.addPara("- Expanded Deck Crew", bad, 0.0f);
        tooltip.addImageWithText(pad);
    }

    public void applyEffectsAfterShipCreation(final ShipAPI ship, final String id) {
        for (final String tmp : dcp_magellan_ClassicDesign.BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                ship.getVariant().removeMod(tmp);
                DCPBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }

    public boolean isApplicableToShip(final ShipAPI ship) {
        return !this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_core_hullmod") && ship != null && !ship.isCapital() && super.isApplicableToShip(ship);
    }

    public String getUnapplicableReason(final ShipAPI ship) {
        if (ship != null && ship.isCapital()) {
            return this.getString("MagSpecialCompatCapital");
        }
        if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_core_hullmod")) {
            return this.getString("MagSpecialCompat3");
        }
        return super.getUnapplicableReason(ship);
    }

    static {
        BLOCKED_HULLMODS = new HashSet<String>(3);
        ZERO_FLUX_RING = new Color(215, 215, 255, 255);
        ZERO_FLUX_INNER = new Color(0, 110, 200, 75);
        FULL_FLUX_RING = new Color(255, 240, 225, 255);
        FULL_FLUX_INNER = new Color(255, 90, 75, 75);
        dcp_magellan_ClassicDesign.BLOCKED_HULLMODS.add("targetingunit");
        dcp_magellan_ClassicDesign.BLOCKED_HULLMODS.add("dedicated_targeting_core");
        dcp_magellan_ClassicDesign.BLOCKED_HULLMODS.add("expanded_deck_crew");
    }
}