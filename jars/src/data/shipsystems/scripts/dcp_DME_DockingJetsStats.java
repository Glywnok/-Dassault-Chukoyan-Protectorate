package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class dcp_DME_DockingJetsStats extends BaseShipSystemScript {
   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      if (state == State.OUT) {
         stats.getMaxSpeed().unmodify(id);
         stats.getMaxTurnRate().unmodify(id);
      } else {
         stats.getMaxSpeed().modifyFlat(id, 50.0F);
         stats.getAcceleration().modifyPercent(id, 120.0F * effectLevel);
         stats.getDeceleration().modifyPercent(id, 120.0F * effectLevel);
         stats.getTurnAcceleration().modifyFlat(id, 30.0F * effectLevel);
         stats.getTurnAcceleration().modifyPercent(id, 120.0F * effectLevel);
         stats.getMaxTurnRate().modifyFlat(id, 20.0F);
         stats.getMaxTurnRate().modifyPercent(id, 120.0F);
      }

      if (stats.getEntity() instanceof ShipAPI) {
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
         return index == 1 ? new StatusData("+20 top speed", false) : null;
      }
   }
}
