package data.scripts.weapons.MagicGuidance;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.lazywizard.lazylib.CollisionUtils;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_BusterGuidanceProjScript extends BaseEveryFrameCombatPlugin {
   private static final String GUIDANCE_MODE_PRIMARY = "DUMBCHASER";
   private static final String GUIDANCE_MODE_SECONDARY = "NONE";
   private static final List<String> VALID_TARGET_TYPES = new ArrayList();
   private static final float TARGET_REACQUIRE_RANGE = 1000.0F;
   private static final float TARGET_REACQUIRE_ANGLE = 120.0F;
   private static final float TURN_RATE = 480.0F;
   private static final float SWAY_AMOUNT_PRIMARY = 0.0F;
   private static final float SWAY_AMOUNT_SECONDARY = 0.0F;
   private static final float SWAY_PERIOD_PRIMARY = 0.0F;
   private static final float SWAY_PERIOD_SECONDARY = 0.0F;
   private static final float SWAY_FALLOFF_FACTOR = 0.0F;
   private static final float ONE_TURN_DUMB_INACCURACY = 0.0F;
   private static final float ONE_TURN_TARGET_INACCURACY = 0.0F;
   private static final int INTERCEPT_ITERATIONS = 4;
   private static final float INTERCEPT_ACCURACY_FACTOR = 1.0F;
   private static final float GUIDANCE_DELAY_MAX = 0.0F;
   private static final float GUIDANCE_DELAY_MIN = 0.0F;
   private static final boolean BROKEN_BY_PHASE = false;
   private static final boolean RETARGET_ON_SIDE_SWITCH = false;
   private DamagingProjectileAPI proj;
   private CombatEntityAPI target;
   private Vector2f targetPoint;
   private float targetAngle;
   private float swayCounter1;
   private float swayCounter2;
   private float lifeCounter;
   private float estimateMaxLife;
   private float delayCounter;
   private Vector2f offsetVelocity;
   private Vector2f lastTargetPos;
   private float actualGuidanceDelay;

   public dcp_DME_BusterGuidanceProjScript(@NotNull DamagingProjectileAPI proj, CombatEntityAPI target) {
      this.proj = proj;
      this.target = target;
      this.lastTargetPos = target != null ? target.getLocation() : new Vector2f(proj.getLocation());
      this.swayCounter1 = MathUtils.getRandomNumberInRange(0.0F, 1.0F);
      this.swayCounter2 = MathUtils.getRandomNumberInRange(0.0F, 1.0F);
      this.lifeCounter = 0.0F;
      this.estimateMaxLife = proj.getWeapon().getRange() / (new Vector2f(proj.getVelocity().x - proj.getSource().getVelocity().x, proj.getVelocity().y - proj.getSource().getVelocity().y)).length();
      this.delayCounter = 0.0F;
      this.actualGuidanceDelay = MathUtils.getRandomNumberInRange(0.0F, 0.0F);
      if ("DUMBCHASER".equals("ONE_TURN_DUMB")) {
         this.targetAngle = proj.getWeapon().getCurrAngle() + MathUtils.getRandomNumberInRange(-0.0F, 0.0F);
         this.offsetVelocity = proj.getSource().getVelocity();
      } else if ("DUMBCHASER".equals("ONE_TURN_TARGET")) {
         this.targetPoint = MathUtils.getRandomPointInCircle(this.getApproximateInterception(25), 0.0F);
      } else if ("DUMBCHASER".contains("SWARM") && target != null) {
         this.applySwarmOffset();
      } else {
         this.targetPoint = new Vector2f(Misc.ZERO);
      }

   }

   public void advance(float amount, List<InputEventAPI> events) {
      if (Global.getCombatEngine() != null) {
         if (Global.getCombatEngine().isPaused()) {
            amount = 0.0F;
         }

         if (this.proj != null && !this.proj.didDamage() && !this.proj.isFading() && Global.getCombatEngine().isEntityInPlay(this.proj)) {
            this.lifeCounter += amount;
            if (this.lifeCounter > this.estimateMaxLife) {
               this.lifeCounter = this.estimateMaxLife;
            }

            if (this.delayCounter < this.actualGuidanceDelay) {
               this.delayCounter += amount;
            } else {
               this.swayCounter1 += amount * 0.0F;
               this.swayCounter2 += amount * 0.0F;
               float swayThisFrame = (float)Math.pow((double)(1.0F - this.lifeCounter / this.estimateMaxLife), 0.0D) * ((float)(FastTrig.sin(6.283185307179586D * (double)this.swayCounter1) * 0.0D) + (float)(FastTrig.sin(6.283185307179586D * (double)this.swayCounter2) * 0.0D));
               if (!"DUMBCHASER".contains("ONE_TURN")) {
                  if (this.target != null) {
                     if (!Global.getCombatEngine().isEntityInPlay(this.target)) {
                        this.target = null;
                     }

                     if (this.target instanceof ShipAPI) {
                        if (!((ShipAPI)this.target).isHulk()) {
                           if (((ShipAPI)this.target).isPhased()) {
                           }

                           if (this.target.getOwner() == this.proj.getOwner()) {
                           }
                        } else {
                           this.target = null;
                        }
                     }
                  }

                  if (this.target == null) {
                     if ("NONE".equals("NONE")) {
                        Global.getCombatEngine().removePlugin(this);
                        return;
                     }

                     if ("NONE".equals("DISAPPEAR")) {
                        Global.getCombatEngine().removeEntity(this.proj);
                        Global.getCombatEngine().removePlugin(this);
                        return;
                     }

                     this.reacquireTarget();
                  } else {
                     this.lastTargetPos = new Vector2f(this.target.getLocation());
                  }
               }

               if ("DUMBCHASER".contains("ONE_TURN") || this.target != null) {
                  float facingSwayless;
                  float facingSwayless1;
                  Vector2f targetPointRotated;
                  if ("DUMBCHASER".equals("ONE_TURN_DUMB")) {
                     facingSwayless = this.proj.getFacing() - swayThisFrame;

                     for(facingSwayless = Math.abs(this.targetAngle - facingSwayless); facingSwayless > 180.0F; facingSwayless = Math.abs(facingSwayless - 360.0F)) {
                     }

                     facingSwayless += Misc.getClosestTurnDirection(facingSwayless, this.targetAngle) * Math.min(facingSwayless, 480.0F * amount);
                     targetPointRotated = new Vector2f(this.proj.getVelocity());
                     targetPointRotated.x -= this.offsetVelocity.x;
                     targetPointRotated.y -= this.offsetVelocity.y;
                     this.proj.setFacing(facingSwayless + swayThisFrame);
                     this.proj.getVelocity().x = MathUtils.getPoint(new Vector2f(Misc.ZERO), targetPointRotated.length(), facingSwayless + swayThisFrame).x + this.offsetVelocity.x;
                     this.proj.getVelocity().y = MathUtils.getPoint(new Vector2f(Misc.ZERO), targetPointRotated.length(), facingSwayless + swayThisFrame).y + this.offsetVelocity.y;
                  } else {
                     float angleToHit;
                     if ("DUMBCHASER".equals("ONE_TURN_TARGET")) {
                        facingSwayless = this.proj.getFacing() - swayThisFrame;
                        facingSwayless = VectorUtils.getAngle(this.proj.getLocation(), this.targetPoint);

                        for(angleToHit = Math.abs(facingSwayless - facingSwayless); angleToHit > 180.0F; angleToHit = Math.abs(angleToHit - 360.0F)) {
                        }

                        facingSwayless += Misc.getClosestTurnDirection(facingSwayless, facingSwayless) * Math.min(angleToHit, 480.0F * amount);
                        this.proj.setFacing(facingSwayless + swayThisFrame);
                        this.proj.getVelocity().x = MathUtils.getPoint(new Vector2f(Misc.ZERO), this.proj.getVelocity().length(), facingSwayless + swayThisFrame).x;
                        this.proj.getVelocity().y = MathUtils.getPoint(new Vector2f(Misc.ZERO), this.proj.getVelocity().length(), facingSwayless + swayThisFrame).y;
                     } else {
                        float angleToHit1;
                        if ("DUMBCHASER".contains("DUMBCHASER")) {
                           facingSwayless = this.proj.getFacing() - swayThisFrame;
                           Vector2f targetPointRotated1 = VectorUtils.rotate(new Vector2f(this.targetPoint), this.target.getFacing());
                           angleToHit = VectorUtils.getAngle(this.proj.getLocation(), Vector2f.add(this.target.getLocation(), targetPointRotated1, new Vector2f(Misc.ZERO)));

                           for(angleToHit = Math.abs(angleToHit - facingSwayless); angleToHit > 180.0F; angleToHit = Math.abs(angleToHit - 360.0F)) {
                           }

                           facingSwayless += Misc.getClosestTurnDirection(facingSwayless, angleToHit) * Math.min(angleToHit, 480.0F * amount);
                           this.proj.setFacing(facingSwayless + swayThisFrame);
                           this.proj.getVelocity().x = MathUtils.getPoint(new Vector2f(Misc.ZERO), this.proj.getVelocity().length(), facingSwayless + swayThisFrame).x;
                           this.proj.getVelocity().y = MathUtils.getPoint(new Vector2f(Misc.ZERO), this.proj.getVelocity().length(), facingSwayless + swayThisFrame).y;
                        } else if ("DUMBCHASER".contains("INTERCEPT")) {
                           int iterations = 4;
                           facingSwayless = this.proj.getFacing() - swayThisFrame;
                           targetPointRotated = VectorUtils.rotate(new Vector2f(this.targetPoint), this.target.getFacing());
                           angleToHit = VectorUtils.getAngle(this.proj.getLocation(), Vector2f.add(this.getApproximateInterception(iterations), targetPointRotated, new Vector2f(Misc.ZERO)));

                           float angleDiffAbsolute;
                           for(angleDiffAbsolute = Math.abs(angleToHit - facingSwayless); angleDiffAbsolute > 180.0F; angleDiffAbsolute = Math.abs(angleDiffAbsolute - 360.0F)) {
                           }

                           facingSwayless += Misc.getClosestTurnDirection(facingSwayless, angleToHit) * Math.min(angleDiffAbsolute, 480.0F * amount);
                           this.proj.setFacing(facingSwayless + swayThisFrame);
                           this.proj.getVelocity().x = MathUtils.getPoint(new Vector2f(Misc.ZERO), this.proj.getVelocity().length(), facingSwayless + swayThisFrame).x;
                           this.proj.getVelocity().y = MathUtils.getPoint(new Vector2f(Misc.ZERO), this.proj.getVelocity().length(), facingSwayless + swayThisFrame).y;
                        }
                     }
                  }

               }
            }
         } else {
            Global.getCombatEngine().removePlugin(this);
         }
      }
   }

   private void reacquireTarget() {
      CombatEntityAPI newTarget = null;
      Vector2f centerOfDetection = this.lastTargetPos;
      if ("NONE".contains("_PROJ")) {
         centerOfDetection = this.proj.getLocation();
      }

      List<CombatEntityAPI> potentialTargets = new ArrayList();
      Iterator var4;
      CombatEntityAPI potTarget;
      if (VALID_TARGET_TYPES.contains("ASTEROID")) {
         var4 = CombatUtils.getAsteroidsWithinRange(centerOfDetection, 1000.0F).iterator();

         while(var4.hasNext()) {
            potTarget = (CombatEntityAPI)var4.next();
            if (potTarget.getOwner() != this.proj.getOwner() && Math.abs(VectorUtils.getAngle(this.proj.getLocation(), potTarget.getLocation()) - this.proj.getFacing()) < 120.0F) {
               potentialTargets.add(potTarget);
            }
         }
      }

      if (VALID_TARGET_TYPES.contains("MISSILE")) {
         var4 = CombatUtils.getMissilesWithinRange(centerOfDetection, 1000.0F).iterator();

         while(var4.hasNext()) {
            potTarget = (CombatEntityAPI)var4.next();
            if (potTarget.getOwner() != this.proj.getOwner() && Math.abs(VectorUtils.getAngle(this.proj.getLocation(), potTarget.getLocation()) - this.proj.getFacing()) < 120.0F) {
               potentialTargets.add(potTarget);
            }
         }
      }

      var4 = CombatUtils.getShipsWithinRange(centerOfDetection, 1000.0F).iterator();

      while(var4.hasNext()) {
         ShipAPI potTarget1 = (ShipAPI)var4.next();
         if (potTarget1.getOwner() != this.proj.getOwner() && !(Math.abs(VectorUtils.getAngle(this.proj.getLocation(), potTarget1.getLocation()) - this.proj.getFacing()) > 120.0F) && !potTarget1.isHulk()) {
            if (potTarget1.isPhased()) {
            }

            if (potTarget1.getHullSize().equals(HullSize.FIGHTER) && VALID_TARGET_TYPES.contains("FIGHTER")) {
               potentialTargets.add(potTarget1);
            }

            if (potTarget1.getHullSize().equals(HullSize.FRIGATE) && VALID_TARGET_TYPES.contains("FRIGATE")) {
               potentialTargets.add(potTarget1);
            }

            if (potTarget1.getHullSize().equals(HullSize.DESTROYER) && VALID_TARGET_TYPES.contains("DESTROYER")) {
               potentialTargets.add(potTarget1);
            }

            if (potTarget1.getHullSize().equals(HullSize.CRUISER) && VALID_TARGET_TYPES.contains("CRUISER")) {
               potentialTargets.add(potTarget1);
            }

            if (potTarget1.getHullSize().equals(HullSize.CAPITAL_SHIP) && VALID_TARGET_TYPES.contains("CAPITAL")) {
               potentialTargets.add(potTarget1);
            }
         }
      }

      if (!potentialTargets.isEmpty()) {
         if ("NONE".contains("REACQUIRE_NEAREST")) {
            var4 = potentialTargets.iterator();

            while(var4.hasNext()) {
               potTarget = (CombatEntityAPI)var4.next();
               if (newTarget == null) {
                  newTarget = potTarget;
               } else if (MathUtils.getDistance(newTarget, centerOfDetection) > MathUtils.getDistance(potTarget, centerOfDetection)) {
                  newTarget = potTarget;
               }
            }
         } else if ("NONE".contains("REACQUIRE_RANDOM")) {
            newTarget = (CombatEntityAPI)potentialTargets.get(MathUtils.getRandomNumberInRange(0, potentialTargets.size() - 1));
         }

         this.target = newTarget;
         if ("DUMBCHASER".contains("SWARM")) {
            this.applySwarmOffset();
         }
      }

   }

   private Vector2f getApproximateInterception(int calculationSteps) {
      Vector2f returnPoint = new Vector2f(this.target.getLocation());

      for(int i = 0; i < calculationSteps; ++i) {
         float arrivalTime = MathUtils.getDistance(this.proj.getLocation(), returnPoint) / this.proj.getVelocity().length();
         returnPoint.x = this.target.getLocation().x + this.target.getVelocity().x * arrivalTime * 1.0F;
         returnPoint.y = this.target.getLocation().y + this.target.getVelocity().y * arrivalTime * 1.0F;
      }

      return returnPoint;
   }

   private void applySwarmOffset() {
      int i = 40;
      boolean success = false;

      while(i > 0 && this.target != null) {
         --i;
         Vector2f potPoint = MathUtils.getRandomPointInCircle(this.target.getLocation(), this.target.getCollisionRadius());
         if (CollisionUtils.isPointWithinBounds(potPoint, this.target)) {
            potPoint.x -= this.target.getLocation().x;
            potPoint.y -= this.target.getLocation().y;
            potPoint = VectorUtils.rotate(potPoint, -this.target.getFacing());
            this.targetPoint = new Vector2f(potPoint);
            success = true;
            break;
         }
      }

      if (!success) {
         this.targetPoint = new Vector2f(Misc.ZERO);
      }

   }

   static {
      VALID_TARGET_TYPES.add("DESTROYER");
      VALID_TARGET_TYPES.add("CRUISER");
      VALID_TARGET_TYPES.add("CAPITAL");
   }
}
