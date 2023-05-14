package data.hullmods;

import java.util.HashSet;

import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.scripts.DMEUtils;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.awt.Color;
import java.util.Set;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_SpartacusReactor extends BaseHullMod
{
    private static final Set<String> BLOCKED_HULLMODS;
    public static final float COST_REDUCTION_LG = 8.0f;
    public static final float COST_REDUCTION_MED = 4.0f;
    public static final float COST_REDUCTION_SM = 2.0f;
    public static final int ENERGY_RANGE_BONUS = 200;
    public static final Color JITTER_COLOR;
    public static final Color JITTER_UNDER_COLOR;

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
        stats.getDynamic().getMod("large_energy_mod").modifyFlat(id, -8.0f);
        stats.getDynamic().getMod("medium_energy_mod").modifyFlat(id, -4.0f);
        stats.getDynamic().getMod("small_energy_mod").modifyFlat(id, -2.0f);
        stats.getDynamic().getStat("replacement_rate_decrease_mult").modifyMult(id, 0.0f);
        stats.getEnergyWeaponRangeBonus().modifyFlat(id, 200.0f);
        if (stats.getVariant().hasHullMod("safetyoverrides") || stats.getVariant().hasHullMod("eis_aquila")) {
            stats.getShieldDamageTakenMult().modifyMult(id, 1.2f);
            stats.getOverloadTimeMod().modifyMult(id, 1.5f);
            stats.getShieldMalfunctionChance().modifyFlat(id, 0.01f);
            stats.getShieldMalfunctionFluxLevel().modifyFlat(id, 0.95f);
            stats.getDynamic().getStat("explosion_damage_mult").modifyMult(id, 3.0f);
            stats.getDynamic().getStat("explosion_radius_mult").modifyMult(id, 1.5f);
        }
    }

    public void advanceInCombat(final ShipAPI ship, final float amount) {
        final MutableShipStatsAPI stats = ship.getMutableStats();
        final String id = "magellan_SpartacusReactor";
        final float fluxlevel = ship.getFluxLevel();
        final float hardfluxlevel = ship.getHardFluxLevel();
        stats.getEmpDamageTakenMult().modifyMult(id, 0.25f + 1.0f * fluxlevel);
        stats.getDynamic().getStat("explosion_radius_mult").modifyMult(id, 0.75f + 0.5f * hardfluxlevel);
        float jitterLevel = 0.0f;
        float jitterLevel2 = 0.0f;
        float jitterRangeBonus = 0.0f;
        final float maxRangeBonus = 10.0f;
        if (fluxlevel < 0.7f) {
            jitterLevel = 0.0f;
        }
        else if (fluxlevel >= 0.7f) {
            jitterLevel = DMEUtils.lerp(0.0f, fluxlevel, -3.0f + 4.0f * hardfluxlevel);
            if (jitterLevel > 1.0f) {
                jitterLevel = 1.0f;
            }
            jitterRangeBonus = jitterLevel * maxRangeBonus;
        }
        jitterLevel2 = (float)Math.sqrt(jitterLevel);
        ship.setJitter((Object)this, dcp_magellan_SpartacusReactor.JITTER_COLOR, jitterLevel2, 3, 0.0f, 0.0f + jitterRangeBonus);
        ship.setJitterUnder((Object)this, dcp_magellan_SpartacusReactor.JITTER_UNDER_COLOR, jitterLevel2, 25, 0.0f, 7.0f + jitterRangeBonus);
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color bad = Misc.getNegativeHighlightColor();
        final Color badbg = dcp_magellan_hullmodUtils.getNegativeBGColor();
        final Color emp_color = dcp_magellan_hullmodUtils.getEMPHLColor();
        final Color lev = dcp_magellan_hullmodUtils.getLevellerHLColor();
        final Color levbg = dcp_magellan_hullmodUtils.getLevellerBGColor();
        tooltip.addSectionHeading(this.getString("SpartacusReactorTitle"), lev, levbg, Alignment.MID, pad);
        tooltip.addPara("- " + this.getString("SpartacusReactorDesc1"), pad, h, new String[] { "2", "4", "8 OP" });
        tooltip.addPara("- " + this.getString("ClassicDesc2"), padS, h, new String[] { this.getString("Classic2HL") });
        tooltip.addPara("- " + this.getString("LevellerRefitDesc2"), padS, h, new String[] { "200su" });
        final LabelAPI intlabel = tooltip.addPara("- " + this.getString("SpartacusReactorDesc3"), padS, h, new String[] { "25%", "125%" });
        intlabel.setHighlight(new String[] { this.getString("SpartacusReactor3HL"), "25%", "125%" });
        intlabel.setHighlightColors(new Color[] { emp_color, h, h });
        tooltip.addPara("- " + this.getString("SpartacusReactorDesc4"), padS, h, new String[] { this.getString("SpartacusReactor4HL"), "25%" });
        tooltip.addSectionHeading(this.getString("MagellanIncompTitle"), bad, badbg, Alignment.MID, pad);
        final TooltipMakerAPI text = tooltip.beginImageWithText("graphics/Magellan/icons/tooltip/hullmod_incompatible.png", 40.0f);
        text.addPara(this.getString("MagellanAllIncomp"), padS);
        text.addPara("- Expanded Deck Crew", bad, padS);
        text.addPara("- Resistant Flux Conduits", bad, padS);
        tooltip.addImageWithText(pad);
    }

    public void applyEffectsAfterShipCreation(final ShipAPI ship, final String id) {
        for (final String tmp : dcp_magellan_SpartacusReactor.BLOCKED_HULLMODS) {
            if (ship.getVariant().getHullMods().contains(tmp)) {
                ship.getVariant().removeMod(tmp);
                DCPBlockedHullmodDisplayScript.showBlocked(ship);
            }
        }
    }

    public boolean isApplicableToShip(final ShipAPI ship) {
        return !this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_core_hullmod") && ship != null && !ship.isFrigate() && super.isApplicableToShip(ship);
    }

    public String getUnapplicableReason(final ShipAPI ship) {
        if (ship != null && ship.isFrigate()) {
            return this.getString("MagSpecialCompatFrigate");
        }
        if (this.shipHasOtherModInCategory(ship, this.spec.getId(), "magellan_core_hullmod")) {
            return this.getString("MagSpecialCompat3");
        }
        return super.getUnapplicableReason(ship);
    }

    public boolean affectsOPCosts() {
        return true;
    }

    static {
        BLOCKED_HULLMODS = new HashSet<String>(1);
        JITTER_COLOR = new Color(50, 60, 255, 100);
        JITTER_UNDER_COLOR = new Color(50, 60, 255, 155);
        dcp_magellan_SpartacusReactor.BLOCKED_HULLMODS.add("expanded_deck_crew");
        dcp_magellan_SpartacusReactor.BLOCKED_HULLMODS.add("fluxbreakers");
    }
}