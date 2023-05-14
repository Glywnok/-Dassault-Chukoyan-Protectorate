package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.awt.Color;

public class dcp_DME_SlammerJetsStats extends BaseShipSystemScript {
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
         stats.getMaxSpeed().modifyFlat(id, 0.0F);
         stats.getMaxSpeed().modifyPercent(id, 100.0F * effectLevel);
         stats.getMaxTurnRate().modifyPercent(id, 100.0F * effectLevel);
         stats.getAcceleration().modifyPercent(id, 150.0F * effectLevel);
         stats.getDeceleration().modifyPercent(id, 200.0F);
      } else {
         stats.getMaxSpeed().modifyFlat(id, 180.0F * effectLevel);
         stats.getMaxSpeed().modifyPercent(id, 30.0F * effectLevel);
         stats.getAcceleration().modifyPercent(id, 1000.0F * effectLevel);
         stats.getDeceleration().modifyPercent(id, 350.0F * effectLevel);
         stats.getTurnAcceleration().modifyFlat(id, 600.0F * effectLevel);
         stats.getTurnAcceleration().modifyPercent(id, 300.0F * effectLevel);
         stats.getMaxTurnRate().modifyFlat(id, 60.0F * effectLevel);
         stats.getMaxTurnRate().modifyPercent(id, 120.0F * effectLevel);
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
      if (index == 0) {
         return new StatusData("improved maneuverability", false);
      } else {
         return index == 1 ? new StatusData("increased top speed", false) : null;
      }
   }

   static {
      myParticleFX = new SkipjetParticleFX(2.0F, 0.4F, 8.0F, 0.5F, 0.3F, 45.0F, COLOR_FULL);
   }
}
