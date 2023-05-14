package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class dcp_DME_MirageRecallStats extends BaseShipSystemScript {
   public static final Object KEY_JITTER = new Object();
   public static final Color JITTER_COLOR = new Color(85, 115, 215, 255);

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      ShipAPI ship = null;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         if (effectLevel > 0.0F) {
            float jitterLevel = effectLevel;
            boolean firstTime = false;
            String fightersKey = ship.getId() + "_recall_device_target";
            List<ShipAPI> fighters = null;
            if (!Global.getCombatEngine().getCustomData().containsKey(fightersKey)) {
               fighters = getFighters(ship);
               Global.getCombatEngine().getCustomData().put(fightersKey, fighters);
               firstTime = true;
            } else {
               fighters = (List)Global.getCombatEngine().getCustomData().get(fightersKey);
            }

            if (fighters == null) {
               fighters = new ArrayList();
            }

            Iterator var10 = ((List)fighters).iterator();

            while(true) {
               while(true) {
                  ShipAPI fighter;
                  do {
                     do {
                        if (!var10.hasNext()) {
                           return;
                        }

                        fighter = (ShipAPI)var10.next();
                     } while(fighter.isHulk());

                     float maxRangeBonus = fighter.getCollisionRadius() * 1.0F;
                     float jitterRangeBonus = 5.0F + jitterLevel * maxRangeBonus;
                     if (firstTime) {
                        Global.getSoundPlayer().playSound("system_phase_skimmer", 1.0F, 0.5F, fighter.getLocation(), fighter.getVelocity());
                     }

                     fighter.setJitter(KEY_JITTER, JITTER_COLOR, jitterLevel, 10, 0.0F, jitterRangeBonus);
                     fighter.setPhased(true);
                     if (state == State.IN) {
                        float alpha = 1.0F - effectLevel * 0.5F;
                        fighter.setExtraAlphaMult(alpha);
                     }
                  } while(effectLevel != 1.0F);

                  if (fighter.getWing() != null && fighter.getWing().getSource() != null) {
                     fighter.getWing().getSource().makeCurrentIntervalFast();
                     fighter.getWing().getSource().land(fighter);
                  } else {
                     fighter.setExtraAlphaMult(1.0F);
                  }
               }
            }
         }
      }
   }

   public static List<ShipAPI> getFighters(ShipAPI carrier) {
      List<ShipAPI> result = new ArrayList();
      Iterator var2 = Global.getCombatEngine().getShips().iterator();

      while(var2.hasNext()) {
         ShipAPI ship = (ShipAPI)var2.next();
         if (ship.isFighter() && ship.getWing() != null && ship.getWing().getSourceShip() == carrier) {
            result.add(ship);
         }
      }

      return result;
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      ShipAPI ship = null;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         String fightersKey = ship.getId() + "_recall_device_target";
         Global.getCombatEngine().getCustomData().remove(fightersKey);
      }
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      return null;
   }
}
