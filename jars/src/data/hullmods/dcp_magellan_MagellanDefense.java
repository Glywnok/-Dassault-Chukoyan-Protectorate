package data.hullmods;

import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.DMEUtils;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.awt.Color;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_MagellanDefense extends BaseHullMod
{
    public static final Color ZERO_FLUX_RING;
    public static final Color ZERO_FLUX_INNER;
    public static final Color FULL_FLUX_RING;
    public static final Color FULL_FLUX_INNER;
    public static final float PROJ_DAMAGE_MULT = 0.15f;
    public static final float BEAM_DAMAGE_MULT = 0.2f;
    public static final float FRAG_DAMAGE_MULT = 0.25f;
    public static final float PROJ_DAMAGE_MULT_HS = 0.05f;
    public static final float BEAM_DAMAGE_MULT_HS = 0.5f;
    public static final float OVERLOAD_DUR_MULT = 1.5f;
    public static final float SHIELD_DIE_CHANCE = 0.03f;
    public static final float SHIELD_DIE_FLUXLEVEL = 0.8f;

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
        if (stats.getVariant().hasHullMod("hardenedshieldemitter")) {
            stats.getProjectileShieldDamageTakenMult().modifyMult(id, 0.95f);
            stats.getBeamShieldDamageTakenMult().modifyMult(id, 1.5f);
            stats.getOverloadTimeMod().modifyMult(id, 1.5f);
            stats.getShieldMalfunctionChance().modifyFlat(id, 0.03f);
            stats.getShieldMalfunctionFluxLevel().modifyFlat(id, 0.8f);
        }
        else {
            stats.getProjectileShieldDamageTakenMult().modifyMult(id, 0.85f);
            stats.getBeamShieldDamageTakenMult().modifyMult(id, 1.2f);
        }
        stats.getFragmentationDamageTakenMult().modifyMult(id, 0.75f);
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
            final Color color1 = Misc.interpolateColor(dcp_magellan_MagellanDefense.ZERO_FLUX_RING, dcp_magellan_MagellanDefense.FULL_FLUX_RING, Math.min(outputColorLerp, 1.0f));
            final Color color2 = Misc.interpolateColor(dcp_magellan_MagellanDefense.ZERO_FLUX_INNER, dcp_magellan_MagellanDefense.FULL_FLUX_INNER, Math.min(outputColorLerp, 1.0f));
            ship.getShield().setRingColor(color1);
            ship.getShield().setInnerColor(color2);
        }
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color neg = Misc.getNegativeHighlightColor();
        final Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
        final Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
        final Color quote = dcp_magellan_hullmodUtils.getQuoteColor();
        final Color attrib = Misc.getGrayColor();
        tooltip.addSectionHeading(this.getString("MagSpecialTitle"), mag, magbg, Alignment.MID, pad);
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/magellan_hullmod_defense.png", 40.0f);
        text.addPara("- " + this.getString("DefenseSPDesc1"), padS, h, new String[] { "15%" });
        text.addPara("- " + this.getString("DefenseSPDesc2"), padS, h, new String[] { "20%" });
        text.addPara("- " + this.getString("DefenseSPDesc3"), padS, h, new String[] { "25%" });
        tooltip.addImageWithText(pad);
        tooltip.addPara(this.getString("MagSpecialCompatMalfunction") + " " + this.getString("DefenseMalfunctionHL"), neg, pad);
        final LabelAPI label = tooltip.addPara(this.getString("DefenseQuote"), quote, pad);
        label.italicize(0.12f);
        tooltip.addPara("      " + this.getString("MagellanEmDash") + this.getString("DefenseAttrib"), attrib, padS);
    }

    public boolean isApplicableToShip(final ShipAPI ship) {
        return !this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_exclusive_hullmod") && (ship.getVariant().hasHullMod("magellan_engineering") || ship.getVariant().hasHullMod("magellan_engineering_civ") || ship.getVariant().hasHullMod("magellan_blackcollarmod") || ship.getVariant().hasHullMod("magellan_startigermod") || ship.getVariant().hasHullMod("magellan_levellermod") || ship.getVariant().hasHullMod("magellan_herdmod") || ship.getVariant().hasHullMod("magellan_yellowtailmod")) && super.isApplicableToShip(ship);
    }

    public String getUnapplicableReason(final ShipAPI ship) {
        if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_exclusive_hullmod")) {
            return this.getString("MagSpecialCompat1");
        }
        if (!ship.getVariant().hasHullMod("magellan_engineering") || !ship.getVariant().hasHullMod("magellan_engineering_civ") || !ship.getVariant().hasHullMod("magellan_blackcollarmod") || !ship.getVariant().hasHullMod("magellan_startigermod") || !ship.getVariant().hasHullMod("magellan_levellermod") || !ship.getVariant().hasHullMod("magellan_herdmod") || !ship.getVariant().hasHullMod("magellan_yellowtailmod")) {
            return this.getString("MagSpecialCompat2");
        }
        return super.getUnapplicableReason(ship);
    }

    static {
        ZERO_FLUX_RING = new Color(255, 255, 225, 255);
        ZERO_FLUX_INNER = new Color(125, 125, 100, 75);
        FULL_FLUX_RING = new Color(255, 240, 225, 255);
        FULL_FLUX_INNER = new Color(255, 90, 75, 75);
    }
}