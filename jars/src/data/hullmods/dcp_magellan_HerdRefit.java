package data.hullmods;

import java.util.HashMap;
import java.util.HashSet;

import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.awt.Color;
import java.util.Map;
import java.util.Set;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_HerdRefit extends BaseHullMod
{
    private final IntervalUtil interval;
    private static final Set<String> BLOCKED_HULLMODS;
    private static Map speed;
    private static Map accmult;
    public static final float HEALTH_BONUS = 100.0f;
    public static final float TURN_PENALTY = 20.0f;
    public static float DMOD_AVOID_CHANCE;
    public static final float AUTOFIRE_MALUS = -15.0f;
    public static final float FLAMEOUT_CHANCE_SO = 0.01f;
    private Color color;

    public dcp_magellan_HerdRefit() {
        this.interval = new IntervalUtil(0.12f, 0.2f);
        this.color = new Color(175, 225, 175, 200);
    }

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
        stats.getWeaponTurnRateBonus().modifyMult(id, 0.8f);
        stats.getDynamic().getMod("dmod_acquire_prob_mod").modifyMult(id, 1.0f - dcp_magellan_HerdRefit.DMOD_AVOID_CHANCE * 0.01f);
        stats.getMaxSpeed().modifyFlat(id, (float) dcp_magellan_HerdRefit.speed.get(hullSize));
        stats.getAcceleration().modifyMult(id, 1.0f + (float) dcp_magellan_HerdRefit.accmult.get(hullSize) / 2.0f);
        stats.getDeceleration().modifyMult(id, (float) dcp_magellan_HerdRefit.accmult.get(hullSize));
        if (stats.getVariant().hasHullMod("safetyoverrides") || stats.getVariant().hasHullMod("eis_aquila")) {
            stats.getEngineMalfunctionChance().modifyFlat(id, 0.01f);
        }
        stats.getAutofireAimAccuracy().modifyFlat(id, -0.14999999f);
    }

    public void advanceInCombat(final ShipAPI ship, final float amount) {
        this.interval.advance(amount);
        if (this.interval.intervalElapsed()) {
            final float enginejitter = 0.1f + 0.1f * (float)Math.random();
            ship.getEngineController().fadeToOtherColor((Object)this, this.color, (Color)null, 1.0f, enginejitter);
            ship.getEngineController().extendFlame((Object)this, 0.0f, enginejitter, enginejitter);
        }
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float pad2S = 4.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color bad = Misc.getNegativeHighlightColor();
        final Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
        final Color herd = dcp_magellan_hullmodUtils.getHerdHLColor();
        final Color herdbg = dcp_magellan_hullmodUtils.getHerdBGColor();
        tooltip.addSectionHeading(this.getString("MagellanEngTitle"), herd, herdbg, Alignment.MID, pad);
        tooltip.addPara("- " + this.getString("MagellanEngDesc1"), pad, h, new String[] { "100%" });
        tooltip.addPara("- " + this.getString("MagellanEngDesc2"), padS, h, new String[] { "10%" });
        tooltip.addPara("- " + this.getString("MagellanEngDesc4"), padS, h, new String[] { "40%" });
        final LabelAPI label = tooltip.addPara("\u2014\u2014\u2014 " + this.getString("HerdRefitTitle") + " \u2014\u2014\u2014", herd, pad2S);
        label.setAlignment(Alignment.MID);
        tooltip.addPara("- " + this.getString("HerdRefitDesc3"), pad2S, h, new String[] { "45", "30", "15", "5" });
        tooltip.addPara("- " + this.getString("HerdRefitDesc4"), padS, h, new String[] { "15%" });
        tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/hullmod_incompatible.png", 40.0f);
        text.addPara(this.getString("MagellanAllIncomp"), padS);
        text.addPara("- Hardened Shields", bad, padS);
        text.addPara("- Armored Weapon Mounts", bad, 0.0f);
        text.addPara("- Integrated Targeting Unit", bad, 0.0f);
        tooltip.addImageWithText(pad);
    }

    public void applyEffectsAfterShipCreation(final ShipAPI ship, final String id) {
        for (final String tmp : dcp_magellan_HerdRefit.BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                ship.getVariant().removeMod(tmp);
                DCPBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }

    static {
        BLOCKED_HULLMODS = new HashSet<String>(3);
        dcp_magellan_HerdRefit.speed = new HashMap();
        dcp_magellan_HerdRefit.accmult = new HashMap();
        dcp_magellan_HerdRefit.speed.put(ShipAPI.HullSize.FRIGATE, 45.0f);
        dcp_magellan_HerdRefit.speed.put(ShipAPI.HullSize.DESTROYER, 30.0f);
        dcp_magellan_HerdRefit.speed.put(ShipAPI.HullSize.CRUISER, 15.0f);
        dcp_magellan_HerdRefit.speed.put(ShipAPI.HullSize.CAPITAL_SHIP, 5.0f);
        dcp_magellan_HerdRefit.accmult.put(ShipAPI.HullSize.FRIGATE, 0.6f);
        dcp_magellan_HerdRefit.accmult.put(ShipAPI.HullSize.DESTROYER, 0.5f);
        dcp_magellan_HerdRefit.accmult.put(ShipAPI.HullSize.CRUISER, 0.4f);
        dcp_magellan_HerdRefit.accmult.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.3f);
        dcp_magellan_HerdRefit.DMOD_AVOID_CHANCE = 40.0f;
        dcp_magellan_HerdRefit.BLOCKED_HULLMODS.add("hardenedshieldemitter");
        dcp_magellan_HerdRefit.BLOCKED_HULLMODS.add("armoredweapons");
        dcp_magellan_HerdRefit.BLOCKED_HULLMODS.add("targetingunit");
    }
}