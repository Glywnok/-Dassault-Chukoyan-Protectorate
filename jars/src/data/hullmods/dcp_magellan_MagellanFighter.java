package data.hullmods;

import com.fs.starfarer.api.ui.LabelAPI;
import java.awt.Color;
import org.lwjgl.input.Keyboard;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.combat.FighterLaunchBayAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_MagellanFighter extends BaseHullMod
{
    public static String RD_NO_EXTRA_CRAFT;
    public static final float REFIT_MALUS = 1.5f;
    public static final float REFIT_MALUS_EDC = 2.0f;
    public static final float SPEED_MALUS = 10.0f;
    public static final float ACCEL_MALUS = 0.2f;

    public int getDisplaySortOrder() {
        return 4;
    }

    public int getDisplayCategoryIndex() {
        return 3;
    }

    private String getString(final String key) {
        return Global.getSettings().getString("Hullmod", "magellan_" + key);
    }

    public void applyEffectsBeforeShipCreation(final ShipAPI.HullSize hullSize, final MutableShipStatsAPI stats, final String id) {
        if (stats.getVariant().hasHullMod("expanded_deck_crew")) {
            stats.getFighterRefitTimeMult().modifyMult(id, 2.0f);
        }
        else {
            stats.getFighterRefitTimeMult().modifyMult(id, 1.5f);
        }
        stats.getMaxSpeed().modifyFlat(id, -10.0f);
        stats.getAcceleration().modifyMult(id, 0.8f);
    }

    public void advanceInCombat(final ShipAPI ship, final float amount) {
        if (ship == null || !ship.isAlive()) {
            return;
        }
        for (final FighterLaunchBayAPI bay : ship.getLaunchBaysCopy()) {
            if (bay.getWing() != null) {
                final FighterWingSpecAPI spec = bay.getWing().getSpec();
                final int addForWing = getAdditionalFor(spec);
                final int maxTotal = spec.getNumFighters() + addForWing;
                final int actualAdd = maxTotal - bay.getWing().getWingMembers().size();
                if (actualAdd <= 0) {
                    continue;
                }
                bay.setExtraDeployments(actualAdd);
                bay.setExtraDeploymentLimit(maxTotal);
                bay.setExtraDuration(1000000.0f);
            }
        }
    }

    public static int getAdditionalFor(final FighterWingSpecAPI spec) {
        if (spec.hasTag(dcp_magellan_MagellanFighter.RD_NO_EXTRA_CRAFT)) {
            return 0;
        }
        final int size = spec.getNumFighters();
        if (size <= 3) {
            return 1;
        }
        if (size == 4) {
            return 2;
        }
        if (size == 5) {
            return 3;
        }
        return 2;
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color pos = Misc.getPositiveHighlightColor();
        final Color neg = Misc.getNegativeHighlightColor();
        final Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
        final Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
        final Color quote = dcp_magellan_hullmodUtils.getQuoteColor();
        final Color attrib = Misc.getGrayColor();
        tooltip.addSectionHeading(this.getString("MagSpecialTitle"), mag, magbg, Alignment.MID, pad);
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/magellan_hullmod_fighter.png", 40.0f);
        text.addPara("- " + this.getString("FighterSPDesc1"), padS, h, new String[] { "1", "2", "3", "8" });
        text.addPara("- " + this.getString("FighterSPDesc2"), padS, h, new String[] { "50%" });
        text.addPara("- " + this.getString("FighterSPDesc3"), padS, h, new String[] { "10su" });
        text.addPara("- " + this.getString("FighterSPDesc4"), padS, h, new String[] { "20%" });
        tooltip.addImageWithText(pad);
        tooltip.addPara(this.getString("MagSpecialCompatMalfunction") + " " + this.getString("FighterMalfunctionHL"), neg, pad);
        if (Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
            tooltip.addSectionHeading(this.getString("FighterSPExTitle"), mag, magbg, Alignment.MID, pad);
            final LabelAPI label = tooltip.addPara(this.getString("FighterSPExDesc1") + " " + this.getString("FighterSPExDesc2") + " " + this.getString("FighterSPExDesc3"), pad);
            label.setHighlight(new String[] { "3", "+1", "4", "+2", "5", "+3", "6", "+2", "8" });
            label.setHighlightColors(new Color[] { h, pos, h, pos, h, pos, h, pos, h });
            return;
        }
        if (!Keyboard.isKeyDown(Keyboard.getKeyIndex("F1"))) {
            tooltip.addPara(this.getString("FighterSPExExpand"), attrib, pad);
        }
        final LabelAPI label = tooltip.addPara(this.getString("FighterQuote"), quote, pad);
        label.italicize(0.12f);
        tooltip.addPara("      " + this.getString("MagellanEmDash") + this.getString("FighterAttrib"), attrib, padS);
    }

    public boolean isApplicableToShip(final ShipAPI ship) {
        return !this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_exclusive_hullmod") && ship != null && !ship.isFrigate() && ship.getHullSpec().getFighterBays() > 0 && !ship.getVariant().hasHullMod("phasefield") && (ship.getVariant().hasHullMod("magellan_engineering") || ship.getVariant().hasHullMod("magellan_engineering_civ") || ship.getVariant().hasHullMod("magellan_blackcollarmod") || ship.getVariant().hasHullMod("magellan_startigermod") || ship.getVariant().hasHullMod("magellan_levellermod") || ship.getVariant().hasHullMod("magellan_herdmod") || ship.getVariant().hasHullMod("magellan_yellowtailmod")) && super.isApplicableToShip(ship);
    }

    public String getUnapplicableReason(final ShipAPI ship) {
        if (ship != null && ship.isFrigate()) {
            return this.getString("MagSpecialCompatFrigate");
        }
        if (ship != null && ship.getHullSpec().getFighterBays() == 0) {
            return this.getString("MagSpecialCompatNoBays");
        }
        if (ship != null && ship.getVariant().hasHullMod("phasefield")) {
            return this.getString("MagSpecialCompatPhase");
        }
        if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_exclusive_hullmod")) {
            return this.getString("MagSpecialCompat1");
        }
        if (!ship.getVariant().hasHullMod("magellan_engineering") || !ship.getVariant().hasHullMod("magellan_engineering_civ") || !ship.getVariant().hasHullMod("magellan_blackcollarmod") || !ship.getVariant().hasHullMod("magellan_startigermod") || !ship.getVariant().hasHullMod("magellan_levellermod") || !ship.getVariant().hasHullMod("magellan_herdmod") || !ship.getVariant().hasHullMod("magellan_yellowtailmod")) {
            return this.getString("MagSpecialCompat2");
        }
        return super.getUnapplicableReason(ship);
    }

    static {
        dcp_magellan_MagellanFighter.RD_NO_EXTRA_CRAFT = "rd_no_extra_craft";
    }
}