package data.hullmods;

import com.fs.starfarer.api.util.Misc;
import data.scripts.DMEUtils;
import com.fs.starfarer.api.combat.ShipAPI;
import java.awt.Color;
import com.fs.starfarer.api.combat.BaseHullMod;

public class dcp_magellan_ClassicShieldHullmod extends BaseHullMod
{
    public static final Color ZERO_FLUX_RING;
    public static final Color ZERO_FLUX_INNER;
    public static final Color FULL_FLUX_RING;
    public static final Color FULL_FLUX_INNER;

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
            final Color color1 = Misc.interpolateColor(dcp_magellan_ClassicShieldHullmod.ZERO_FLUX_RING, dcp_magellan_ClassicShieldHullmod.FULL_FLUX_RING, Math.min(outputColorLerp, 1.0f));
            final Color color2 = Misc.interpolateColor(dcp_magellan_ClassicShieldHullmod.ZERO_FLUX_INNER, dcp_magellan_ClassicShieldHullmod.FULL_FLUX_INNER, Math.min(outputColorLerp, 1.0f));
            ship.getShield().setRingColor(color1);
            ship.getShield().setInnerColor(color2);
        }
    }

    static {
        ZERO_FLUX_RING = new Color(215, 215, 255, 255);
        ZERO_FLUX_INNER = new Color(0, 110, 200, 75);
        FULL_FLUX_RING = new Color(255, 240, 225, 255);
        FULL_FLUX_INNER = new Color(255, 90, 75, 75);
    }
}