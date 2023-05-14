package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class dcp_DME_TargetPainterStats extends BaseShipSystemScript {
   public static final Object KEY_SHIP = new Object();
   public static final Object KEY_TARGET = new Object();
   public static final Object KEY_JITTER = new Object();
   public static final float DAM_MULT = 1.33F;
   public static final float RANGE = 2500.0F;
   public static final float AUTOFIRE_MULT = 1.6F;
   public static final Color TEXT_COLOR = new Color(255, 200, 125, 225);
   public static final Color JITTER_COLOR = new Color(75, 115, 255, 90);
   public static final Color JITTER_UNDER_COLOR = new Color(75, 115, 255, 175);

   public void apply(MutableShipStatsAPI stats, final String id, State state, float effectLevel) {
      ShipAPI ship = null;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         if (effectLevel > 0.0F) {
            float jitterLevel = effectLevel;
            float maxRangeBonus = 5.0F;
            float jitterRangeBonus = effectLevel * maxRangeBonus;
            Iterator var9 = this.getFighters(ship).iterator();

            while(var9.hasNext()) {
               ShipAPI fighter = (ShipAPI)var9.next();
               if (!fighter.isHulk()) {
                  MutableShipStatsAPI fStats = fighter.getMutableStats();
                  fStats.getAutofireAimAccuracy().modifyFlat(id, 0.6F);
                  fStats.getMaxRecoilMult().modifyMult(id, 1.0F + 0.6F * effectLevel);
                  fStats.getRecoilPerShotMult().modifyMult(id, 1.0F + 0.6F * effectLevel);
                  if (jitterLevel > 0.0F) {
                     fighter.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 3, 0.0F, jitterRangeBonus);
                     fighter.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 1, 0.0F, 0.0F + jitterRangeBonus * 1.0F);
                     Global.getSoundPlayer().playLoop("system_targeting_feed_loop", ship, 1.0F, 1.0F, fighter.getLocation(), fighter.getVelocity());
                  }
               }
            }
         }

         String targetDataKey = ship.getId() + "_entropy_target_data";
         Object targetDataObj = Global.getCombatEngine().getCustomData().get(targetDataKey);
         if (state == State.IN && targetDataObj == null) {
            ShipAPI target = this.findTarget(ship);
            Global.getCombatEngine().getCustomData().put(targetDataKey, new data.shipsystems.scripts.dcp_DME_TargetPainterStats.TargetData(ship, target));
            if (target != null && (target.getFluxTracker().showFloaty() || ship == Global.getCombatEngine().getPlayerShip() || target == Global.getCombatEngine().getPlayerShip())) {
               target.getFluxTracker().showOverloadFloatyIfNeeded("-Target Lock Acquired-", TEXT_COLOR, 4.0F, true);
            }
         } else if (state == State.IDLE && targetDataObj != null) {
            Global.getCombatEngine().getCustomData().remove(targetDataKey);
            ((data.shipsystems.scripts.dcp_DME_TargetPainterStats.TargetData)targetDataObj).currDamMult = 1.0F;
            targetDataObj = null;
         }

         if (targetDataObj != null && ((data.shipsystems.scripts.dcp_DME_TargetPainterStats.TargetData)targetDataObj).target != null) {
            final data.shipsystems.scripts.dcp_DME_TargetPainterStats.TargetData targetData = (data.shipsystems.scripts.dcp_DME_TargetPainterStats.TargetData)targetDataObj;
            targetData.currDamMult = 1.0F + 0.33000004F * effectLevel;
            if (targetData.targetEffectPlugin == null) {
               targetData.targetEffectPlugin = new BaseEveryFrameCombatPlugin() {
                  public void advance(float amount, List<InputEventAPI> events) {
                     if (!Global.getCombatEngine().isPaused()) {
                        if (targetData.target == Global.getCombatEngine().getPlayerShip()) {
                           Global.getCombatEngine().maintainStatusForPlayerShip(data.shipsystems.scripts.dcp_DME_TargetPainterStats.KEY_TARGET, targetData.ship.getSystem().getSpecAPI().getIconSpriteName(), targetData.ship.getSystem().getDisplayName(), "" + (int)((targetData.currDamMult - 1.0F) * 100.0F) + "% more damage taken", true);
                        }

                        if (!(targetData.currDamMult <= 1.0F) && targetData.ship.isAlive()) {
                           targetData.target.getMutableStats().getHullDamageTakenMult().modifyMult(id, targetData.currDamMult);
                           targetData.target.getMutableStats().getArmorDamageTakenMult().modifyMult(id, targetData.currDamMult);
                           targetData.target.getMutableStats().getShieldDamageTakenMult().modifyMult(id, targetData.currDamMult);
                           targetData.target.getMutableStats().getEmpDamageTakenMult().modifyMult(id, targetData.currDamMult);
                        } else {
                           targetData.target.getMutableStats().getHullDamageTakenMult().unmodify(id);
                           targetData.target.getMutableStats().getArmorDamageTakenMult().unmodify(id);
                           targetData.target.getMutableStats().getShieldDamageTakenMult().unmodify(id);
                           targetData.target.getMutableStats().getEmpDamageTakenMult().unmodify(id);
                           Global.getCombatEngine().removePlugin(targetData.targetEffectPlugin);
                        }

                     }
                  }
               };
               Global.getCombatEngine().addPlugin(targetData.targetEffectPlugin);
            }

            if (effectLevel > 0.0F) {
               if (state != State.IN) {
                  targetData.elaspedAfterInState += Global.getCombatEngine().getElapsedInLastFrame();
               }

               float shipJitterLevel = 0.0F;
               if (state == State.IN) {
                  shipJitterLevel = effectLevel;
               } else {
                  float durOut = 0.5F;
                  shipJitterLevel = Math.max(0.0F, durOut - targetData.elaspedAfterInState) / durOut;
               }

               float maxRangeBonus = 50.0F;
               float jitterRangeBonus = shipJitterLevel * maxRangeBonus;
               Color color = JITTER_COLOR;
               if (shipJitterLevel > 0.0F) {
                  ship.setJitter(KEY_SHIP, color, shipJitterLevel, 4, 0.0F, 0.0F + jitterRangeBonus * 1.0F);
               }

               if (effectLevel > 0.0F) {
                  targetData.target.setJitter(KEY_TARGET, color, effectLevel, 3, 0.0F, 5.0F);
               }
            }

         }
      }
   }

   private List<ShipAPI> getFighters(ShipAPI carrier) {
      List<ShipAPI> result = new ArrayList();
      Iterator var3 = Global.getCombatEngine().getShips().iterator();

      while(var3.hasNext()) {
         ShipAPI ship = (ShipAPI)var3.next();
         if (ship.isFighter() && ship.getWing() != null && ship.getWing().getSourceShip() == carrier) {
            result.add(ship);
         }
      }

      return result;
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
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

      return target;
   }

   protected float getMaxRange(ShipAPI ship) {
      return 2500.0F;
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      if (effectLevel > 0.0F) {
         float accMult;
         if (index == 0) {
            accMult = 1.0F + 0.33000004F * effectLevel;
            return new StatusData("" + (int)((accMult - 1.0F) * 100.0F) + "% more damage to target", false);
         }

         if (index == 1) {
            accMult = 1.0F + 0.6F * effectLevel;
            return new StatusData("" + (int)((accMult - 1.0F) * 100.0F) + "% added fighter accuracy", false);
         }
      }

      return null;
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
            return target == null && ship.getShipTarget() != null ? "OUT OF RANGE" : "NO TARGET";
         }
      }
   }

   public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
      ShipAPI target = this.findTarget(ship);
      return target != null && target != ship;
   }

   public static class TargetData {
      public ShipAPI ship;
      public ShipAPI target;
      public EveryFrameCombatPlugin targetEffectPlugin;
      public float currDamMult;
      public float elaspedAfterInState;

      public TargetData(ShipAPI ship, ShipAPI target) {
         this.ship = ship;
         this.target = target;
      }
   }
}
