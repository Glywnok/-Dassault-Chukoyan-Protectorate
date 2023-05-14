package data.hullmods;

import java.util.HashMap;
import com.fs.starfarer.api.ui.LabelAPI;
import java.awt.Color;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.Global;
import java.util.Map;
import com.fs.starfarer.api.combat.BaseHullMod;
import data.hullmods.dcp_magellan_hullmodUtils;

public class dcp_magellan_AblativeComposites extends BaseHullMod {
    private static Map damage;
    private static final float EMP_MULT = 0.75f;

    private String getString(final String key) {
        return Global.getSettings().getString("Hullmod", "magellan_" + key);
    }

    public void applyEffectsBeforeShipCreation(final ShipAPI.HullSize hullSize, final MutableShipStatsAPI stats, final String id) {
        stats.getMaxArmorDamageReduction().modifyFlat(id, -0.6f);
        stats.getHullDamageTakenMult().modifyMult(id, (float)dcp_magellan_AblativeComposites.damage.get(hullSize));
        stats.getEngineDamageTakenMult().modifyMult(id, (float)dcp_magellan_AblativeComposites.damage.get(hullSize));
        stats.getWeaponDamageTakenMult().modifyMult(id, (float)dcp_magellan_AblativeComposites.damage.get(hullSize));
        stats.getBeamDamageTakenMult().modifyMult(id, 0.75f);
        stats.getEmpDamageTakenMult().modifyMult(id, 0.75f);
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color emp_color = dcp_magellan_hullmodUtils.getEMPHLColor();
        final Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
        final Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
        tooltip.addSectionHeading(this.getString("MagellanEffects"), mag, magbg, Alignment.MID, pad);
        tooltip.addPara("- " + this.getString("MagellanArmorDesc1"), pad, h, new String[] { "60%", "25%" });
        tooltip.addPara("- " + this.getString("MagellanArmorDesc2"), padS, h, new String[] { "30%", "40%", "50%" });
        final LabelAPI intlabel = tooltip.addPara("- " + this.getString("MagellanArmorDesc3"), padS, h, new String[] { "25%" });
        intlabel.setHighlight(new String[] { this.getString("MagellanArmorDesc3HL"), "25%" });
        intlabel.setHighlightColors(new Color[] { emp_color, h });
    }

    public boolean isApplicableToShip(final ShipAPI ship) {
        return ship != null && !ship.isFrigate() && (ship.getVariant().hasHullMod("magellan_engineering") || ship.getVariant().hasHullMod("magellan_engineering_civ") || ship.getVariant().hasHullMod("magellan_blackcollarmod") || ship.getVariant().hasHullMod("magellan_startigermod") || ship.getVariant().hasHullMod("magellan_herdmod")) && super.isApplicableToShip(ship);
    }

    public String getUnapplicableReason(final ShipAPI ship) {
        if (ship != null && ship.isFrigate()) {
            return this.getString("MagSpecialCompatFrigate");
        }
        if (!ship.getVariant().hasHullMod("magellan_engineering") || !ship.getVariant().hasHullMod("magellan_engineering_civ") || !ship.getVariant().hasHullMod("magellan_blackcollarmod") || !ship.getVariant().hasHullMod("magellan_startigermod") || !ship.getVariant().hasHullMod("magellan_herdmod")) {
            return this.getString("MagSpecialCompat2");
        }
        return super.getUnapplicableReason(ship);
    }

    static {
        (dcp_magellan_AblativeComposites.damage = new HashMap()).put(ShipAPI.HullSize.FRIGATE, 0.0f);
        dcp_magellan_AblativeComposites.damage.put(ShipAPI.HullSize.DESTROYER, 0.7f);
        dcp_magellan_AblativeComposites.damage.put(ShipAPI.HullSize.CRUISER, 0.6f);
        dcp_magellan_AblativeComposites.damage.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.5f);
    }
}