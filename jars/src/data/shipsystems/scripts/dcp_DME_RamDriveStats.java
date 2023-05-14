package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.awt.Color;

public class dcp_DME_RamDriveStats extends BaseShipSystemScript {
   private static final float PARTICLE_BASE_SIZE = 3.0F;
   private static final float PARTICLE_BASE_DURATION = 0.9F;
   private static final float PARTICLE_BASE_CHANCE = 1.0F;
   private static final float PARTICLE_BASE_BRIGHTNESS = 9.0F;
   private static final float PARTICLE_VELOCITY_MULT = 0.4F;
   private static final float CONE_ANGLE = 50.0F;
   private static final Color COLOR_FULL = new Color(255, 90, 75, 255);
   private static final SkipjetParticleFX myParticleFX;

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      if (state == State.OUT) {
         stats.getMaxSpeed().unmodify(id);
      } else {
         stats.getMaxSpeed().modifyFlat(id, 180.0F * effectLevel);
         stats.getAcceleration().modifyFlat(id, 120.0F * effectLevel);
         CombatEntityAPI ship = stats.getEntity();
         if (ship instanceof ShipAPI) {
            myParticleFX.apply((ShipAPI)ship, Global.getCombatEngine(), effectLevel);
         }
      }

   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      stats.getMaxSpeed().unmodify(id);
      stats.getMaxTurnRate().unmodify(id);
      stats.getTurnAcceleration().unmodify(id);
      stats.getAcceleration().unmodify(id);
      stats.getDeceleration().unmodify(id);
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      return index == 0 ? new StatusData("increased engine power", false) : null;
   }

   static {
      myParticleFX = new SkipjetParticleFX(3.0F, 0.9F, 9.0F, 1.0F, 0.4F, 50.0F, COLOR_FULL);
   }
}
