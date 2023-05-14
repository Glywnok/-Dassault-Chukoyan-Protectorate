package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.List;

public class dcp_DME_SigmaDisruptor extends BaseShipSystemScript {
   public static final float ENERGY_DAM_PENALTY_MULT = 0.5F;
   public static final float DISRUPTION_DUR = 3.0F;
   public static final float MIN_DISRUPTION_RANGE = 700.0F;
   public static final Color OVERLOAD_COLOR = new Color(105, 255, 205, 255);
   public static final Color JITTER_COLOR = new Color(105, 255, 205, 155);
   public static final Color JITTER_UNDER_COLOR = new Color(105, 255, 205, 255);

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      ShipAPI ship = null;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         stats.getEnergyWeaponDamageMult().modifyMult(id, 0.5F);
         float jitterLevel = effectLevel;
         if (state == State.OUT) {
            jitterLevel = effectLevel * effectLevel;
         }

         float maxRangeBonus = 50.0F;
         float jitterRangeBonus = jitterLevel * maxRangeBonus;
         if (state == State.OUT) {
         }

         ship.setJitterUnder(this, JITTER_UNDER_COLOR, jitterLevel, 21, 0.0F, 3.0F + jitterRangeBonus);
         ship.setJitter(this, JITTER_COLOR, jitterLevel, 4, 0.0F, 0.0F + jitterRangeBonus);
         String targetKey = ship.getId() + "_acausal_target";
         Object foundTarget = Global.getCombatEngine().getCustomData().get(targetKey);
         ShipAPI target;
         if (state == State.IN) {
            if (foundTarget == null) {
               target = this.findTarget(ship);
               if (target != null) {
                  Global.getCombatEngine().getCustomData().put(targetKey, target);
               }
            }
         } else if (effectLevel >= 1.0F) {
            if (foundTarget instanceof ShipAPI) {
               target = (ShipAPI)foundTarget;
               if (target.getFluxTracker().isOverloadedOrVenting()) {
                  target = ship;
               }

               this.applyEffectToTarget(ship, target);
            }
         } else if (state == State.OUT && foundTarget != null) {
            Global.getCombatEngine().getCustomData().remove(targetKey);
         }

      }
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      stats.getEnergyWeaponDamageMult().unmodify(id);
   }

   protected ShipAPI findTarget(ShipAPI ship) {
      float range = this.getMaxRange(ship);
      boolean player = ship == Global.getCombatEngine().getPlayerShip();
      ShipAPI target = ship.getShipTarget();
      float dist;
      if (target != null) {
         float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
         dist = ship.getCollisionRadius() + target.getCollisionRadius();
         if (dist > range + dist) {
            target = null;
         }
      } else {
         if (target == null || target.getOwner() == ship.getOwner()) {
            if (player) {
               target = Misc.findClosestShipEnemyOf(ship, ship.getMouseTarget(), HullSize.FIGHTER, range, true);
            } else {
               Object test = ship.getAIFlags().getCustom(AIFlags.MANEUVER_TARGET);
               if (test instanceof ShipAPI) {
                  target = (ShipAPI)test;
                  dist = Misc.getDistance(ship.getLocation(), target.getLocation());
                  float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
                  if (dist > range + radSum) {
                     target = null;
                  }
               }
            }
         }

         if (target == null) {
            target = Misc.findClosestShipEnemyOf(ship, ship.getLocation(), HullSize.FIGHTER, range, true);
         }
      }

      if (target == null || target.getFluxTracker().isOverloadedOrVenting()) {
         target = ship;
      }

      return target;
   }

   protected float getMaxRange(ShipAPI ship) {
      return 700.0F;
   }

   protected void applyEffectToTarget(ShipAPI ship, final ShipAPI target) {
      if (!target.getFluxTracker().isOverloadedOrVenting()) {
         if (target != ship) {
            target.setOverloadColor(OVERLOAD_COLOR);
            target.getFluxTracker().beginOverloadWithTotalBaseDuration(3.0F);
            if (target.getFluxTracker().showFloaty() || ship == Global.getCombatEngine().getPlayerShip() || target == Global.getCombatEngine().getPlayerShip()) {
               target.getFluxTracker().playOverloadSound();
               target.getFluxTracker().showOverloadFloatyIfNeeded("Induced Overload!", OVERLOAD_COLOR, 4.0F, true);
            }

            Global.getCombatEngine().addPlugin(new BaseEveryFrameCombatPlugin() {
               public void advance(float amount, List<InputEventAPI> events) {
                  if (!target.getFluxTracker().isOverloadedOrVenting()) {
                     target.resetOverloadColor();
                     Global.getCombatEngine().removePlugin(this);
                  }

               }
            });
         }
      }
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      float percent = 50.0F;
      return index == 0 ? new StatusData((int)percent + "% less energy damage", false) : null;
   }

   public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
      if (system.isOutOfAmmo()) {
         return null;
      } else if (system.getState() != SystemState.IDLE) {
         return null;
      } else {
         ShipAPI target = this.findTarget(ship);
         if (target != null && target != ship) {
            return "READY";
         } else {
            return (target == null || target == ship) && ship.getShipTarget() != null ? "OUT OF RANGE" : "NO TARGET";
         }
      }
   }

   public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
      ShipAPI target = this.findTarget(ship);
      return target != null && target != ship;
   }
}
