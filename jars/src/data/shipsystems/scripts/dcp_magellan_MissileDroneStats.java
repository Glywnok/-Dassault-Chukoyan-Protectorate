package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.EmpArcEntityAPI;
import com.fs.starfarer.api.combat.FighterLaunchBayAPI;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.GuidedMissileAI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.impl.combat.DroneStrikeStats;
import com.fs.starfarer.api.util.Misc;
import data.scripts.DMEUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_MissileDroneStats extends DroneStrikeStats {
   private final String DRONE_WPN_ID = "magellan_missiledrone_wpn";
   private final int DRONES_TO_FIRE = 1;
   private final Color FLASH_COLOR = new Color(210, 170, 90, 255);
   protected WeaponAPI weapon;
   protected boolean fired = false;
   protected ShipAPI forceNextTarget = null;

   private String getString(String key) {
      return Global.getSettings().getString("Magellan_Strings", "magellan_" + key);
   }

   protected String getWeaponId() {
      return "magellan_missiledrone_wpn";
   }

   protected int getNumToFire() {
      return 1;
   }

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      ShipAPI ship = null;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         if (this.weapon == null) {
            this.weapon = Global.getCombatEngine().createFakeWeapon(ship, this.getWeaponId());
         }

         Iterator var6 = this.getDrones(ship).iterator();

         while(var6.hasNext()) {
            ShipAPI drone = (ShipAPI)var6.next();
            drone.setExplosionScale(0.67F);
            drone.setExplosionVelocityOverride(new Vector2f());
            drone.setExplosionFlashColorOverride(this.FLASH_COLOR);
         }

         if (effectLevel > 0.0F && !this.fired) {
            if (!this.getDrones(ship).isEmpty()) {
               ShipAPI target = this.findTarget(ship);
               this.convertDrones(ship, target);
            }
         } else if (state == State.IDLE) {
            this.fired = false;
         }

      }
   }

   public void convertDrones(ShipAPI ship, final ShipAPI target) {
      CombatEngineAPI engine = Global.getCombatEngine();
      this.fired = true;
      this.forceNextTarget = null;
      int num = 0;
      List<ShipAPI> drones = this.getDrones(ship);
      if (target != null) {
         Collections.sort(drones, new Comparator<ShipAPI>() {
            public int compare(ShipAPI o1, ShipAPI o2) {
               float d1 = Misc.getDistance(o1.getLocation(), target.getLocation());
               float d2 = Misc.getDistance(o2.getLocation(), target.getLocation());
               return (int)Math.signum(d1 - d2);
            }
         });
      } else {
         Collections.shuffle(drones);
      }

      for(Iterator var6 = drones.iterator(); var6.hasNext(); ++num) {
         ShipAPI drone = (ShipAPI)var6.next();
         if (num < this.getNumToFire()) {
            MissileAPI missile = (MissileAPI)engine.spawnProjectile(ship, this.weapon, this.getWeaponId(), new Vector2f(drone.getLocation()), drone.getFacing(), new Vector2f(drone.getVelocity()));
            if (target != null && missile.getAI() instanceof GuidedMissileAI) {
               GuidedMissileAI ai = (GuidedMissileAI)missile.getAI();
               ai.setTarget(target);
            }

            missile.setEmpResistance(10000);
            float base = missile.getMaxRange();
            float max = this.getMaxRange(ship);
            missile.setMaxRange(max);
            missile.setMaxFlightTime(missile.getMaxFlightTime() * max / base);
            drone.getWing().removeMember(drone);
            drone.setWing((FighterWingAPI)null);
            drone.setExplosionFlashColorOverride(this.FLASH_COLOR);
            engine.addLayeredRenderingPlugin(new data.shipsystems.scripts.dcp_magellan_MissileDroneStats.DroneMissileScript(drone, missile));
            float thickness = 8.0F;
            float coreWidthMult = 0.5F;
            EmpArcEntityAPI arc = engine.spawnEmpArcVisual(DMEUtils.translate_polar(ship.getLocation(), -20.0F, ship.getFacing()), ship, missile.getLocation(), missile, thickness, this.FLASH_COLOR, Color.white);
            arc.setCoreWidthOverride(thickness * coreWidthMult);
            arc.setSingleFlickerMode();
         } else if (drone.getShipAI() != null) {
            drone.getShipAI().cancelCurrentManeuver();
         }
      }

   }

   public void unapply(MutableShipStatsAPI stats, String id) {
   }

   protected ShipAPI findTarget(ShipAPI ship) {
      if (this.getDrones(ship).isEmpty()) {
         return null;
      } else if (this.forceNextTarget != null && this.forceNextTarget.isAlive()) {
         return this.forceNextTarget;
      } else {
         float range = this.getMaxRange(ship);
         boolean player = ship == Global.getCombatEngine().getPlayerShip();
         ShipAPI target = ship.getShipTarget();
         if (!player) {
            Object test = ship.getAIFlags().getCustom(AIFlags.MANEUVER_TARGET);
            if (test instanceof ShipAPI) {
               target = (ShipAPI)test;
               float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
               float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
               if (dist > range + radSum) {
                  target = null;
               }
            }

            if (target == null) {
               target = Misc.findClosestShipEnemyOf(ship, ship.getMouseTarget(), HullSize.FRIGATE, range, true);
            }

            return target;
         } else if (target != null) {
            return target;
         } else {
            target = Misc.findClosestShipEnemyOf(ship, ship.getMouseTarget(), HullSize.FIGHTER, Float.MAX_VALUE, true);
            if (target != null && target.isFighter()) {
               ShipAPI nearbyShip = Misc.findClosestShipEnemyOf(ship, target.getLocation(), HullSize.FRIGATE, 100.0F, false);
               if (nearbyShip != null) {
                  target = nearbyShip;
               }
            }

            if (target == null) {
               target = Misc.findClosestShipEnemyOf(ship, ship.getLocation(), HullSize.FIGHTER, range, true);
            }

            return target;
         }
      }
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      return null;
   }

   public List<ShipAPI> getDrones(ShipAPI ship) {
      List<ShipAPI> result = new ArrayList();
      Iterator var3 = ship.getLaunchBaysCopy().iterator();

      while(true) {
         FighterLaunchBayAPI bay;
         do {
            if (!var3.hasNext()) {
               return result;
            }

            bay = (FighterLaunchBayAPI)var3.next();
         } while(bay.getWing() == null);

         Iterator var5 = bay.getWing().getWingMembers().iterator();

         while(var5.hasNext()) {
            ShipAPI drone = (ShipAPI)var5.next();
            result.add(drone);
         }
      }
   }

   public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
      if (system.isOutOfAmmo()) {
         return null;
      } else if (system.getState() != SystemState.IDLE) {
         return null;
      } else if (this.getDrones(ship).isEmpty()) {
         return this.getString("str_system_nodrones");
      } else {
         float range = this.getMaxRange(ship);
         ShipAPI target = this.findTarget(ship);
         float dist;
         float radSum;
         if (target == null) {
            if (ship.getMouseTarget() != null) {
               dist = Misc.getDistance(ship.getLocation(), ship.getMouseTarget());
               radSum = ship.getCollisionRadius();
               if (dist + radSum > range) {
                  return this.getString("str_system_outofrange");
               }
            }

            return this.getString("str_system_notarget");
         } else {
            dist = Misc.getDistance(ship.getLocation(), target.getLocation());
            radSum = ship.getCollisionRadius() + target.getCollisionRadius();
            return dist > range + radSum ? this.getString("str_system_outofrange") : this.getString("str_system_ready");
         }
      }
   }

   public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
      if (ship != null && ship.getSystem() != null && ship.getSystem().getState() != SystemState.IDLE) {
         return true;
      } else {
         return !this.getDrones(ship).isEmpty();
      }
   }

   public float getMaxRange(ShipAPI ship) {
      if (this.weapon == null) {
         this.weapon = Global.getCombatEngine().createFakeWeapon(ship, this.getWeaponId());
      }

      return ship.getMutableStats().getSystemRangeBonus().computeEffective(this.weapon.getRange());
   }

   public boolean dronesUsefulAsPD() {
      return true;
   }

   public boolean droneStrikeUsefulVsFighters() {
      return false;
   }

   public int getMaxDrones() {
      return 2;
   }

   public float getMissileSpeed() {
      return this.weapon.getProjectileSpeed();
   }

   public void setForceNextTarget(ShipAPI forceNextTarget) {
      this.forceNextTarget = forceNextTarget;
   }

   public ShipAPI getForceNextTarget() {
      return this.forceNextTarget;
   }

   public static class DroneMissileScript extends BaseCombatLayeredRenderingPlugin {
      protected ShipAPI drone;
      protected MissileAPI missile;
      protected boolean done;

      public DroneMissileScript(ShipAPI drone, MissileAPI missile) {
         this.drone = drone;
         this.missile = missile;
         missile.setNoFlameoutOnFizzling(true);
      }

      public void advance(float amount) {
         super.advance(amount);
         if (!this.done) {
            CombatEngineAPI engine = Global.getCombatEngine();
            this.missile.setEccmChanceOverride(1.0F);
            this.missile.setOwner(this.drone.getOriginalOwner());
            this.drone.getLocation().set(this.missile.getLocation());
            this.drone.getVelocity().set(this.missile.getVelocity());
            this.drone.setCollisionClass(CollisionClass.FIGHTER);
            this.drone.setFacing(this.missile.getFacing());
            this.drone.getEngineController().fadeToOtherColor(this, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0), 1.0F, 1.0F);
            float dist = Misc.getDistance(this.missile.getLocation(), this.missile.getStart());
            float jitterFraction = dist / this.missile.getMaxRange();
            jitterFraction = Math.max(jitterFraction, this.missile.getFlightTime() / this.missile.getMaxFlightTime());
            this.missile.setSpriteAlphaOverride(0.0F);
            float jitterMax = 1.0F + 10.0F * jitterFraction;
            this.drone.setJitter(this, new Color(210, 170, 90, (int)(25.0F + 50.0F * jitterFraction)), 1.0F, 10, 1.0F, jitterMax);
            boolean droneDestroyed = this.drone.isHulk() || this.drone.getHitpoints() <= 0.0F;
            Vector2f damageFrom;
            if (!this.missile.isFizzling() && (!(this.missile.getHitpoints() <= 0.0F) || this.missile.didDamage()) && !droneDestroyed) {
               if (this.missile.didDamage()) {
                  this.drone.getVelocity().set(0.0F, 0.0F);
                  this.missile.getVelocity().set(0.0F, 0.0F);
                  damageFrom = new Vector2f(this.drone.getLocation());
                  damageFrom = Misc.getPointWithinRadius(damageFrom, 20.0F);
                  engine.applyDamage(this.drone, damageFrom, 1000000.0F, DamageType.ENERGY, 0.0F, true, false, this.drone, false);
                  this.missile.interruptContrail();
                  engine.removeEntity(this.drone);
                  engine.removeEntity(this.missile);
                  this.done = true;
               }
            } else {
               this.drone.getVelocity().set(0.0F, 0.0F);
               this.missile.getVelocity().set(0.0F, 0.0F);
               if (!droneDestroyed) {
                  damageFrom = new Vector2f(this.drone.getLocation());
                  damageFrom = Misc.getPointWithinRadius(damageFrom, 20.0F);
                  engine.applyDamage(this.drone, damageFrom, 1000000.0F, DamageType.ENERGY, 0.0F, true, false, this.drone, false);
               }

               this.missile.interruptContrail();
               engine.removeEntity(this.drone);
               engine.removeEntity(this.missile);
               this.missile.explode();
               this.done = true;
            }
         }
      }

      public boolean isExpired() {
         return this.done;
      }
   }
}
