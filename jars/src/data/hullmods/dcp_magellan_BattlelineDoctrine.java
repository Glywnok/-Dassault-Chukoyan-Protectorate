package data.hullmods;

import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.awt.Color;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_BattlelineDoctrine extends BaseHullMod
{
    public static float PROJ_SPEED_BONUS;
    public static final float PD_BONUS = 100.0f;
    public static Color BORDER;
    public static Color NAME;

    private String getString(final String key) {
        return Global.getSettings().getString("Hullmod", "magellan_" + key);
    }

    public void applyEffectsBeforeShipCreation(final ShipAPI.HullSize hullSize, final MutableShipStatsAPI stats, final String id) {
        stats.getProjectileSpeedMult().modifyPercent(id, dcp_magellan_BattlelineDoctrine.PROJ_SPEED_BONUS);
        stats.getNonBeamPDWeaponRangeBonus().modifyFlat(id, 100.0f);
        stats.getDamageToFighters().modifyPercent(id, 10.0f);
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color quote = dcp_magellan_hullmodUtils.getQuoteColor();
        final Color attrib = Misc.getGrayColor();
        tooltip.addSectionHeading("Technical Details", Alignment.MID, pad);
        tooltip.addPara("- " + this.getString("ComCrewDesc1"), pad, h, new String[] { "20%" });
        tooltip.addPara("- " + this.getString("ComCrewDesc2"), padS, h, new String[] { "100su" });
        tooltip.addPara("- " + this.getString("ComCrewDesc3"), padS, h, new String[] { "10%" });
        final LabelAPI label = tooltip.addPara(this.getString("ComCrewQuote"), quote, pad);
        label.italicize(0.12f);
        tooltip.addPara("      " + this.getString("MagellanEmDash") + this.getString("ComCrewAttrib"), attrib, padS);
    }

    public Color getBorderColor() {
        return dcp_magellan_BattlelineDoctrine.BORDER;
    }

    public Color getNameColor() {
        return dcp_magellan_BattlelineDoctrine.NAME;
    }

    static {
        dcp_magellan_BattlelineDoctrine.PROJ_SPEED_BONUS = 20.0f;
        dcp_magellan_BattlelineDoctrine.BORDER = new Color(147, 102, 50, 0);
        dcp_magellan_BattlelineDoctrine.NAME = new Color(153, 134, 117, 255);
    }
}