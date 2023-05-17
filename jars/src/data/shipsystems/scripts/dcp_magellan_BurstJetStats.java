package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.util.Iterator;

public class dcp_magellan_BurstJetStats extends BaseShipSystemScript {
   private String getString(String key) {
      return Global.getSettings().getString("System", "dcp_magellan_" + key);
   }

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      if (state == State.OUT) {
         stats.getMaxSpeed().modifyPercent(id, 200.0F * effectLevel);
         stats.getMaxTurnRate().modifyPercent(id, 150.0F * effectLevel);
         stats.getDeceleration().modifyPercent(id, 150.0F * effectLevel);
      } else {
         stats.getMaxSpeed().modifyFlat(id, 260.0F * effectLevel);
         stats.getMaxSpeed().modifyPercent(id, 25.0F * effectLevel);
         stats.getAcceleration().modifyFlat(id, 250.0F * effectLevel);
         stats.getDeceleration().modifyFlat(id, 150.0F * effectLevel);
         stats.getTurnAcceleration().modifyFlat(id, 120.0F * effectLevel);
         stats.getTurnAcceleration().modifyPercent(id, 200.0F * effectLevel);
         stats.getMaxTurnRate().modifyFlat(id, 60.0F * effectLevel);
         stats.getMaxTurnRate().modifyPercent(id, 150.0F * effectLevel);
      }

      if (stats.getEntity() instanceof ShipAPI) {
         ShipAPI ship = (ShipAPI)stats.getEntity();
         String key = ship.getId() + "_" + id;
         Object test = Global.getCombatEngine().getCustomData().get(key);
         if (state == State.IN) {
            if (test == null && effectLevel > 0.3F) {
               Global.getCombatEngine().getCustomData().put(key, new Object());
               ship.getEngineController().getExtendLengthFraction().advance(1.0F);
               Iterator var8 = ship.getEngineController().getShipEngines().iterator();

               while(var8.hasNext()) {
                  ShipEngineAPI engine = (ShipEngineAPI)var8.next();
                  if (engine.isSystemActivated()) {
                     ship.getEngineController().setFlameLevel(engine.getEngineSlot(), 1.0F);
                  }
               }
            }
         } else {
            Global.getCombatEngine().getCustomData().remove(key);
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
      if (index == 0) {
         return new StatusData(this.getString("burstjet_str1"), false);
      } else {
         return index == 1 ? new StatusData(this.getString("burstjet_str2"), false) : null;
      }
   }
}
