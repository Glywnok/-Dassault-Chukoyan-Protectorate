package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.awt.Color;

public class dcp_DME_PulsedSkipjetsStats extends BaseShipSystemScript {
   private static final float PARTICLE_BASE_SIZE = 3.0F;
   private static final float PARTICLE_BASE_DURATION = 0.9F;
   private static final float PARTICLE_BASE_CHANCE = 0.6F;
   private static final float PARTICLE_BASE_BRIGHTNESS = 7.0F;
   private static final float PARTICLE_VELOCITY_MULT = 0.3F;
   private static final float CONE_ANGLE = 45.0F;
   private static final Color COLOR_FULL = new Color(145, 175, 255, 255);
   private static final dcp_DME_SkipjetParticleFX myParticleFX;

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      if (state == State.OUT) {
         stats.getMaxSpeed().unmodify(id);
      } else {
         stats.getMaxSpeed().modifyFlat(id, 600.0F * effectLevel);
         stats.getAcceleration().modifyFlat(id, 1200.0F * effectLevel);
         stats.getMaxTurnRate().modifyFlat(id, 20.0F * effectLevel);
         CombatEntityAPI ship = stats.getEntity();
         if (ship instanceof ShipAPI) {
            myParticleFX.apply((ShipAPI)ship, Global.getCombatEngine(), effectLevel);
         }
      }

   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      stats.getMaxSpeed().unmodify(id);
      stats.getAcceleration().unmodify(id);
      stats.getMaxTurnRate().unmodify(id);
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      if (index == 0) {
         return new StatusData("improved maneuverability", false);
      } else {
         return index == 1 ? new StatusData("increased top speed", false) : null;
      }
   }

   static {
      myParticleFX = new dcp_DME_SkipjetParticleFX(3.0F, 0.9F, 7.0F, 0.6F, 0.3F, 45.0F, COLOR_FULL);
   }
}
