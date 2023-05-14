package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.PhaseCloakSystemAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.awt.Color;

public class dcp_DME_DeserterPhaseCloak extends BaseShipSystemScript {
   public static final Color JITTER_COLOR = new Color(205, 255, 105, 255);
   public static final float JITTER_FADE_TIME = 0.75F;
   public static final float SHIP_ALPHA_MULT = 0.4F;
   public static final float VULNERABLE_FRACTION = 0.0F;
   public static final float INCOMING_DAMAGE_MULT = 1.5F;
   public static final float MAX_TIME_MULT = 1.5F;
   public static boolean FLUX_LEVEL_AFFECTS_SPEED = true;
   public static float MIN_SPEED_MULT = 0.15F;
   public static float BASE_FLUX_LEVEL_FOR_MIN_SPEED = 0.25F;
   protected Object STATUSKEY1 = new Object();
   protected Object STATUSKEY2 = new Object();
   protected Object STATUSKEY3 = new Object();
   protected Object STATUSKEY4 = new Object();

   public static float getMaxTimeMult(MutableShipStatsAPI stats) {
      return 1.0F + 0.5F * stats.getDynamic().getValue("phase_time_mult");
   }

   protected boolean isDisruptable(ShipSystemAPI cloak) {
      return cloak.getSpecAPI().hasTag("disruptable");
   }

   protected float getDisruptionLevel(ShipAPI ship) {
      if (FLUX_LEVEL_AFFECTS_SPEED) {
         float threshold = ship.getMutableStats().getDynamic().getMod("phase_cloak_flux_level_for_min_speed_mod").computeEffective(BASE_FLUX_LEVEL_FOR_MIN_SPEED);
         if (threshold <= 0.0F) {
            return 1.0F;
         } else {
            float level = ship.getHardFluxLevel() / threshold;
            if (level > 1.0F) {
               level = 1.0F;
            }

            return level;
         }
      } else {
         return 0.0F;
      }
   }

   protected void maintainStatus(ShipAPI playerShip, State state, float effectLevel) {
      float f = 0.0F;
      ShipSystemAPI cloak = playerShip.getPhaseCloak();
      if (cloak == null) {
         cloak = playerShip.getSystem();
      }

      if (cloak != null) {
         if (effectLevel > f) {
            Global.getCombatEngine().maintainStatusForPlayerShip(this.STATUSKEY2, cloak.getSpecAPI().getIconSpriteName(), cloak.getDisplayName(), "time flow altered", false);
         }

         if (FLUX_LEVEL_AFFECTS_SPEED && effectLevel > f) {
            if (this.getDisruptionLevel(playerShip) <= 0.0F) {
               Global.getCombatEngine().maintainStatusForPlayerShip(this.STATUSKEY3, cloak.getSpecAPI().getIconSpriteName(), "phase coils stable", "top speed at 100%", false);
            } else {
               String speedPercentStr = Math.round(this.getSpeedMult(playerShip, effectLevel) * 100.0F) + "%";
               Global.getCombatEngine().maintainStatusForPlayerShip(this.STATUSKEY3, cloak.getSpecAPI().getIconSpriteName(), "phase coil stress", "top speed at " + speedPercentStr, true);
            }
         }

      }
   }

   public float getSpeedMult(ShipAPI ship, float effectLevel) {
      return this.getDisruptionLevel(ship) <= 0.0F ? 1.0F : MIN_SPEED_MULT + (1.0F - MIN_SPEED_MULT) * (1.0F - this.getDisruptionLevel(ship) * effectLevel);
   }

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      ShipAPI ship = null;
      boolean player = false;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         player = ship == Global.getCombatEngine().getPlayerShip();
         id = id + "_" + ship.getId();
         if (player) {
            this.maintainStatus(ship, state, effectLevel);
         }

         if (!Global.getCombatEngine().isPaused()) {
            ShipSystemAPI cloak = ship.getPhaseCloak();
            if (cloak == null) {
               cloak = ship.getSystem();
            }

            if (cloak != null) {
               float speedPercentMod;
               if (FLUX_LEVEL_AFFECTS_SPEED && (state == State.ACTIVE || state == State.OUT || state == State.IN)) {
                  speedPercentMod = this.getSpeedMult(ship, effectLevel);
                  if (speedPercentMod < 1.0F) {
                     stats.getMaxSpeed().modifyMult(id + "_2", speedPercentMod);
                  } else {
                     stats.getMaxSpeed().unmodifyMult(id + "_2");
                  }

                  ((PhaseCloakSystemAPI)cloak).setMinCoilJitterLevel(this.getDisruptionLevel(ship));
               }

               if (state != State.COOLDOWN && state != State.IDLE) {
                  speedPercentMod = stats.getDynamic().getMod("phase_cloak_speed").computeEffective(0.0F);
                  float accelPercentMod = stats.getDynamic().getMod("phase_cloak_accel").computeEffective(0.0F);
                  stats.getMaxSpeed().modifyPercent(id, speedPercentMod * effectLevel);
                  stats.getAcceleration().modifyPercent(id, accelPercentMod * effectLevel);
                  stats.getDeceleration().modifyPercent(id, accelPercentMod * effectLevel);
                  float speedMultMod = stats.getDynamic().getMod("phase_cloak_speed").getMult();
                  float accelMultMod = stats.getDynamic().getMod("phase_cloak_accel").getMult();
                  stats.getMaxSpeed().modifyMult(id, speedMultMod * effectLevel);
                  stats.getAcceleration().modifyMult(id, accelMultMod * effectLevel);
                  stats.getDeceleration().modifyMult(id, accelMultMod * effectLevel);
                  float jitterLevel = 0.0F;
                  float jitterRangeBonus = 0.0F;
                  float levelForAlpha = effectLevel;
                  if (state != State.IN && state != State.ACTIVE) {
                     if (state == State.OUT) {
                        if (effectLevel > 0.5F) {
                           ship.setPhased(true);
                        } else {
                           ship.setPhased(false);
                        }

                        levelForAlpha = effectLevel;
                     }
                  } else {
                     ship.setPhased(true);
                     levelForAlpha = effectLevel;
                  }

                  ship.setExtraAlphaMult(1.0F - 0.6F * levelForAlpha);
                  ship.setApplyExtraAlphaToEngines(true);
                  float extra = 0.0F;
                  float shipTimeMult = 1.0F + (getMaxTimeMult(stats) - 1.0F) * levelForAlpha * (1.0F - extra);
                  stats.getTimeMult().modifyMult(id, shipTimeMult);
                  if (player) {
                     Global.getCombatEngine().getTimeMult().modifyMult(id, 1.0F / shipTimeMult);
                  } else {
                     Global.getCombatEngine().getTimeMult().unmodify(id);
                  }

               } else {
                  this.unapply(stats, id);
               }
            }
         }
      }
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      ShipAPI ship = null;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         Global.getCombatEngine().getTimeMult().unmodify(id);
         stats.getTimeMult().unmodify(id);
         stats.getMaxSpeed().unmodify(id);
         stats.getMaxSpeed().unmodifyMult(id + "_2");
         stats.getAcceleration().unmodify(id);
         stats.getDeceleration().unmodify(id);
         ship.setPhased(false);
         ship.setExtraAlphaMult(1.0F);
         ShipSystemAPI cloak = ship.getPhaseCloak();
         if (cloak == null) {
            cloak = ship.getSystem();
         }

         if (cloak != null) {
            ((PhaseCloakSystemAPI)cloak).setMinCoilJitterLevel(0.0F);
         }

      }
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      return null;
   }
}
