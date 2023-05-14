package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.awt.Color;

public class dcp_DME_WitchJetsStats extends BaseShipSystemScript {
   private static final float PARTICLE_BASE_SIZE = 2.0F;
   private static final float PARTICLE_BASE_DURATION = 0.4F;
   private static final float PARTICLE_BASE_CHANCE = 0.5F;
   private static final float PARTICLE_BASE_BRIGHTNESS = 8.0F;
   private static final float PARTICLE_VELOCITY_MULT = 0.3F;
   private static final float CONE_ANGLE = 45.0F;
   private static final Color COLOR_FULL = new Color(105, 255, 205, 255);
   private static final SkipjetParticleFX myParticleFX;

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      if (state == State.OUT) {
         stats.getMaxSpeed().unmodify(id);
      } else {
         stats.getMaxSpeed().modifyFlat(id, 500.0F * effectLevel);
         stats.getAcceleration().modifyFlat(id, 1200.0F * effectLevel);
      }

   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      stats.getMaxSpeed().unmodify(id);
      stats.getAcceleration().unmodify(id);
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      return index == 0 ? new StatusData("increased engine power", false) : null;
   }

   static {
      myParticleFX = new SkipjetParticleFX(2.0F, 0.4F, 8.0F, 0.5F, 0.3F, 45.0F, COLOR_FULL);
   }
}
