package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class dcp_magellan_ImpBurnStats extends BaseShipSystemScript {
   private String getString(String key) {
      return Global.getSettings().getString("System", "dcp_magellan_" + key);
   }

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      if (state == State.OUT) {
         stats.getMaxSpeed().unmodify(id);
      } else {
         stats.getMaxSpeed().modifyFlat(id, 200.0F * effectLevel);
         stats.getAcceleration().modifyFlat(id, 200.0F * effectLevel);
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
      return index == 0 ? new StatusData(this.getString("impulseburn_str"), false) : null;
   }
}
