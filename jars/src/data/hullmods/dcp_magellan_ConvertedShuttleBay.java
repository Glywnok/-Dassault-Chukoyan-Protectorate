package data.hullmods;

import java.util.HashMap;
import java.awt.Color;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import java.util.Map;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_ConvertedShuttleBay extends BaseHullMod
{
    public static final float REFIT_TIME_MULT = 1.5f;
    private static Map<ShipAPI.HullSize, Integer> numBays;

    private String getString(final String key) {
        return Global.getSettings().getString("Hullmod", "magellan_" + key);
    }

    public void applyEffectsBeforeShipCreation(final ShipAPI.HullSize hullSize, final MutableShipStatsAPI stats, final String id) {
        stats.getFighterRefitTimeMult().modifyMult(id, 1.5f);
        stats.getNumFighterBays().modifyFlat(id, (float) dcp_magellan_ConvertedShuttleBay.numBays.get(hullSize));
    }

    public void addPostDescriptionSection(final TooltipMakerAPI tooltip, final ShipAPI.HullSize hullSize, final ShipAPI ship, final float width, final boolean isForModSpec) {
        final float pad = 10.0f;
        final float padS = 2.0f;
        final Color h = Misc.getHighlightColor();
        final Color mag = dcp_magellan_hullmodUtils.getMagellanHLColor();
        final Color magbg = dcp_magellan_hullmodUtils.getMagellanBGColor();
        tooltip.addSectionHeading(this.getString("MagellanEffects"), mag, magbg, Alignment.MID, pad);
        tooltip.addPara("- " + this.getString("MagellanBaysDesc1"), pad, h, new String[] { "1", "1", "2" });
        tooltip.addPara("- " + this.getString("MagellanBaysDesc2"), padS, h, new String[] { "50%" });
    }

    public boolean isApplicableToShip(final ShipAPI ship) {
        return ship != null && !ship.isFrigate() && !ship.getVariant().hasHullMod("phasefield") && super.isApplicableToShip(ship);
    }

    public String getUnapplicableReason(final ShipAPI ship) {
        if (ship != null && ship.isFrigate()) {
            return this.getString("MagSpecialCompatFrigate");
        }
        if (ship != null && ship.getVariant().hasHullMod("phasefield")) {
            return this.getString("MagSpecialCompatPhase");
        }
        return super.getUnapplicableReason(ship);
    }

    static {
        (dcp_magellan_ConvertedShuttleBay.numBays = new HashMap<ShipAPI.HullSize, Integer>()).put(ShipAPI.HullSize.FRIGATE, 0);
        dcp_magellan_ConvertedShuttleBay.numBays.put(ShipAPI.HullSize.DESTROYER, 1);
        dcp_magellan_ConvertedShuttleBay.numBays.put(ShipAPI.HullSize.CRUISER, 1);
        dcp_magellan_ConvertedShuttleBay.numBays.put(ShipAPI.HullSize.CAPITAL_SHIP, 2);
    }
}