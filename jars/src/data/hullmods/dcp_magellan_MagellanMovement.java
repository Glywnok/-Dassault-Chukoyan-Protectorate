package data.hullmods;

import java.util.HashMap;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.awt.Color;
import java.util.Map;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_MagellanMovement extends BaseHullMod
{
    private final IntervalUtil interval;
    private static Map speed;
    public static final float ZERO_FLUX_BONUS = 10.0f;
    public static final float FUEL_USE_PERCENT = 25.0f;
    public static final float ENGINE_DAMAGE_MULT = 2.0f;
    public static final float ENGINE_DAMAGE_MULT_SO = 4.0f;
    public static final float ENGINE_REPAIR_MULT_SO = 0.3f;
    public static final float FLAMEOUT_CHANCE_SO = 0.02f;
    private Color color;

    public dcp_magellan_MagellanMovement() {
        this.interval = new IntervalUtil(0.1f, 0.15f);
        this.color = new Color(200, 200, 200, 255);
    }

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
        stats.getMaxSpeed().modifyFlat(id, (float) dcp_magellan_MagellanMovement.speed.get(hullSize));
        stats.getZeroFluxSpeedBoost().modifyFlat(id, 10.0f);
        stats.getFuelUseMod().modifyPercent(id, 25.0f);
        if (stats.getVariant().hasHullMod("safetyoverrides") || stats.getVariant().hasHullMod("eis_aquila")) {
            stats.getEngineDamageTakenMult().modifyMult(id, 4.0f);
            stats.getCombatEngineRepairTimeMult().modifyMult(id, 1.3f);
            stats.getEngineMalfunctionChance().modifyFlat(id, 0.02f);
        }
        else {
            stats.getEngineDamageTakenMult().modifyMult(id, 2.0f);
        }
    }

    public void advanceInCombat(final ShipAPI ship, final float amount) {
        this.interval.advance(amount);
        if (this.interval.intervalElapsed()) {
            float enginejitter = 0.3f + 0.1f * (float)Math.random();
            if (ship.getVariant().hasHullMod("safetyoverrides") || ship.getVariant().hasHullMod("eis_aquila")) {
                this.color = new Color(255, 135, 135, 255);
                enginejitter = -0.3f + 0.5f * (float)Math.random();
            }
            ship.getEngineController().fadeToOtherColor((Object)this, this.color, (Color)null, 1.0f, enginejitter);
            ship.getEngineController().extendFlame((Object)this, 0.1f, enginejitter, enginejitter);
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
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/magellan_hullmod_movement.png", 40.0f);
        text.addPara("- " + this.getString("MovementSPDesc1"), padS, h, new String[] { "30", "20", "10", "10su" });
        text.addPara("- " + this.getString("MovementSPDesc2"), padS, h, new String[] { "25%" });
        text.addPara("- " + this.getString("MovementSPDesc3"), padS, h, new String[] { this.getString("MovementSP3HL") });
        tooltip.addImageWithText(pad);
        tooltip.addPara(this.getString("MagSpecialCompatMalfunction") + " " + this.getString("MovementMalfunctionHL"), neg, pad);
        final LabelAPI label = tooltip.addPara(this.getString("MovementQuote"), quote, pad);
        label.italicize(0.12f);
        tooltip.addPara("      " + this.getString("MagellanEmDash") + this.getString("MovementAttrib"), attrib, padS);
    }

    public boolean isApplicableToShip(final ShipAPI ship) {
        return !this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_exclusive_hullmod") && ship != null && !ship.isCapital() && (ship.getVariant().hasHullMod("magellan_engineering") || ship.getVariant().hasHullMod("magellan_engineering_civ") || ship.getVariant().hasHullMod("magellan_blackcollarmod") || ship.getVariant().hasHullMod("magellan_startigermod") || ship.getVariant().hasHullMod("magellan_levellermod") || ship.getVariant().hasHullMod("magellan_herdmod") || ship.getVariant().hasHullMod("magellan_yellowtailmod")) && super.isApplicableToShip(ship);
    }

    public String getUnapplicableReason(final ShipAPI ship) {
        if (ship != null && ship.isCapital()) {
            return this.getString("MagSpecialCompatCapital");
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
        (dcp_magellan_MagellanMovement.speed = new HashMap()).put(ShipAPI.HullSize.FRIGATE, 30.0f);
        dcp_magellan_MagellanMovement.speed.put(ShipAPI.HullSize.DESTROYER, 20.0f);
        dcp_magellan_MagellanMovement.speed.put(ShipAPI.HullSize.CRUISER, 10.0f);
        dcp_magellan_MagellanMovement.speed.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.0f);
    }
}