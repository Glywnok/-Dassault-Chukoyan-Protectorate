package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.DCPUtils;
import java.awt.Color;

public class dcp_magellan_ClassicShieldHullmod extends BaseHullMod {
   public static final Color ZERO_FLUX_RING = new Color(215, 215, 255, 255);
   public static final Color ZERO_FLUX_INNER = new Color(0, 110, 200, 75);
   public static final Color FULL_FLUX_RING = new Color(255, 240, 225, 255);
   public static final Color FULL_FLUX_INNER = new Color(255, 90, 75, 75);

   public void advanceInCombat(ShipAPI ship, float amount) {
      if (ship.getShield() != null) {
         float hardflux_track = ship.getHardFluxLevel();
         float outputColorLerp = 0.0F;
         if (hardflux_track < 0.5F) {
            outputColorLerp = 0.0F;
         } else if (hardflux_track >= 0.5F) {
            outputColorLerp = DCPUtils.lerp(0.0F, hardflux_track, hardflux_track);
         }

         Color color1 = Misc.interpolateColor(ZERO_FLUX_RING, FULL_FLUX_RING, Math.min(outputColorLerp, 1.0F));
         Color color2 = Misc.interpolateColor(ZERO_FLUX_INNER, FULL_FLUX_INNER, Math.min(outputColorLerp, 1.0F));
         ship.getShield().setRingColor(color1);
         ship.getShield().setInnerColor(color2);
      }

   }
}
