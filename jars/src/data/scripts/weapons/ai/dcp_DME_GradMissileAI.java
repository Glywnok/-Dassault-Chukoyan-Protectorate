package data.scripts.weapons.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import java.awt.Color;
import java.util.List;
import org.lazywizard.lazylib.CollisionUtils;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_GradMissileAI extends dcp_DME_BaseMissile {
   private static final float ENGINE_DEAD_TIME_MAX = 1.0F;
   private static final float ENGINE_DEAD_TIME_MIN = 0.5F;
   private static final float FIRE_INACCURACY = 2.0F;
   private static final float AIM_THRESHOLD = 0.25F;
   private static final float MIRV_DISTANCE = 600.0F;
   private static final float TIME_BEFORE_CAN_MIRV = 1.0F;
   private static final float FLARE_OFFSET = -9.0F;
   private static final Color FLARE_COLOR = new Color(200, 165, 55, 255);
   private static final Color SMOKE_COLOR = new Color(155, 145, 135, 150);
   private static final boolean STAGE_ONE_EXPLODE = true;
   private static final boolean STAGE_ONE_FLARE = false;
   private static final boolean STAGE_ONE_TRANSFER_MOMENTUM = true;
   private static final float SUBMUNITION_VELOCITY_MOD_MAX = 250.0F;
   private static final float SUBMUNITION_VELOCITY_MOD_MIN = 50.0F;
   private static final int NUMBER_SUBMUNITIONS = 4;
   private static final float SUBMUNITION_RELATIVE_OFFSET = 6.0F;
   private static final float SUBMUNITION_INACCURACY = 1.0F;
   private static final String STAGE_TWO_WEAPON_ID = "dcp_DME_TBM_subForAI";
   private static final String STAGE_TWO_SOUND_ID = "devastator_explosion";
   private static final float VELOCITY_DAMPING_FACTOR = 0.15F;
   private static final float WEAVE_FALLOFF_DISTANCE = 1500.0F;
   private static final float WEAVE_SINE_A_AMPLITUDE = 16.0F;
   private static final float WEAVE_SINE_A_PERIOD = 8.0F;
   private static final float WEAVE_SINE_B_AMPLITUDE = 32.0F;
   private static final float WEAVE_SINE_B_PERIOD = 16.0F;
   private static final Vector2f ZERO = new Vector2f();
   private float engineDeadTimer = MathUtils.getRandomNumberInRange(0.5F, 1.0F);
   private float timeAccum = 0.0F;
   private final float weaveSineAPhase = (float)(Math.random() * 3.141592653589793D * 2.0D);
   private final float weaveSineBPhase = (float)(Math.random() * 3.141592653589793D * 2.0D);
   private final float inaccuracy = MathUtils.getRandomNumberInRange(-2.0F, 2.0F);
   private boolean readyToFly = false;
   protected final float eccmMult = 0.5F;

   public dcp_DME_GradMissileAI(MissileAPI missile, ShipAPI launchingShip) {
      super(missile, launchingShip);
   }

   public float getInaccuracyAfterECCM() {
      float eccmEffectMult = 1.0F;
      if (this.launchingShip != null) {
         eccmEffectMult = 1.0F - this.eccmMult * this.launchingShip.getMutableStats().getMissileGuidance().getModifiedValue();
      }

      if (eccmEffectMult < 0.0F) {
         eccmEffectMult = 0.0F;
      }

      return this.inaccuracy * eccmEffectMult;
   }

   public boolean isWithinMIRVAngle(Vector2f missilePos, Vector2f targetPos, float distance, float heading, float radius) {
      Vector2f endpoint = MathUtils.getPointOnCircumference(missilePos, distance, heading);
      radius *= 0.25F;
      return CollisionUtils.getCollides(missilePos, endpoint, targetPos, radius);
   }

   public void advance(float amount) {
      if (!Global.getCombatEngine().isPaused()) {
         if (!this.missile.isFading() && !this.missile.isFizzling()) {
            boolean mirvNow = false;
            if (!this.readyToFly && this.engineDeadTimer > 0.0F) {
               this.engineDeadTimer -= amount;
               if (this.engineDeadTimer <= 0.0F) {
                  this.readyToFly = true;
               }
            }

            this.timeAccum += amount;
            Vector2f submunition;
            float angle;
            if (this.acquireTarget(amount)) {
               float distance = MathUtils.getDistance(this.target.getLocation(), this.missile.getLocation());
               Vector2f guidedTarget = intercept(this.missile.getLocation(), this.missile.getMaxSpeed(), this.target.getLocation(), this.target.getVelocity());
               float weaveSineB;
               if (guidedTarget == null) {
                  submunition = new Vector2f(this.target.getVelocity());
                  weaveSineB = distance / (this.missile.getVelocity().length() + 1.0F);
                  submunition.scale(weaveSineB);
                  guidedTarget = Vector2f.add(this.target.getLocation(), submunition, (Vector2f)null);
               }

               float weaveSineA = 16.0F * (float)FastTrig.sin(6.283185307179586D * (double)this.timeAccum / 8.0D + (double)this.weaveSineAPhase);
               weaveSineB = 32.0F * (float)FastTrig.sin(6.283185307179586D * (double)this.timeAccum / 16.0D + (double)this.weaveSineBPhase);
               angle = (weaveSineA + weaveSineB) * Math.min(1.0F, distance / 1500.0F);
               float angularDistance = MathUtils.getShortestRotation(this.missile.getFacing(), MathUtils.clampAngle(VectorUtils.getAngle(this.missile.getLocation(), guidedTarget) + this.getInaccuracyAfterECCM() + angle));
               float absDAng = Math.abs(angularDistance);
               if (this.readyToFly) {
                  this.missile.giveCommand(ShipCommand.ACCELERATE);
               }

               this.missile.giveCommand(angularDistance < 0.0F ? ShipCommand.TURN_RIGHT : ShipCommand.TURN_LEFT);
               if (absDAng < Math.abs(this.missile.getAngularVelocity()) * 0.15F) {
                  this.missile.setAngularVelocity(angularDistance / 0.15F);
               }

               float neededDist = 600.0F + this.target.getCollisionRadius() + this.missile.getCollisionRadius();
               if (this.timeAccum >= 1.0F && this.target.getCollisionClass() != CollisionClass.NONE && distance <= neededDist && this.isWithinMIRVAngle(this.missile.getLocation(), guidedTarget, distance, this.missile.getFacing(), this.target.getCollisionRadius())) {
                  mirvNow = true;
               }
            }

            if (mirvNow) {
               Vector2f submunitionVelocityMod = new Vector2f(0.0F, MathUtils.getRandomNumberInRange(250.0F, 50.0F));
               float initialOffset = -9.0F;
               submunition = null;

               for(int i = 0; i < 4; ++i) {
                  angle = this.missile.getFacing() + initialOffset + (float)i * 6.0F + MathUtils.getRandomNumberInRange(-1.0F, 1.0F);
                  if (angle < 0.0F) {
                     angle += 360.0F;
                  } else if (angle >= 360.0F) {
                     angle -= 360.0F;
                  }

                  Vector2f vel = this.missile.getVelocity();
                  Vector2f boost = VectorUtils.rotate(submunitionVelocityMod, this.missile.getFacing());
                  vel.translate(boost.x, boost.y);
                  DamagingProjectileAPI submunition1 = (DamagingProjectileAPI)Global.getCombatEngine().spawnProjectile(this.launchingShip, this.missile.getWeapon(), "dcp_DME_TBM_subForAI", this.missile.getLocation(), angle, vel);
                  submunition1.setFromMissile(true);
               }

               Global.getSoundPlayer().playSound("devastator_explosion", 1.0F, 1.0F, this.missile.getLocation(), this.missile.getVelocity());
               Global.getCombatEngine().addSmokeParticle(this.missile.getLocation(), this.missile.getVelocity(), 60.0F, 0.75F, 0.75F, SMOKE_COLOR);
               Global.getCombatEngine().applyDamage(this.missile, this.missile.getLocation(), this.missile.getHitpoints() * 100.0F, DamageType.FRAGMENTATION, 0.0F, false, false, this.missile);
            }

         }
      }
   }

   protected boolean acquireTarget(float amount) {
      ShipAPI newTarget;
      if (!this.isTargetValid(this.target)) {
         if (this.target instanceof ShipAPI) {
            newTarget = (ShipAPI)this.target;
            if (newTarget.isPhased() && newTarget.isAlive()) {
               return false;
            }
         }

         this.setTarget(this.findBestTarget(false));
         if (this.target == null) {
            this.setTarget(this.findBestTarget(true));
         }

         if (this.target == null) {
            return false;
         }
      } else if (this.isDroneOrFighter(this.target)) {
         if (this.target instanceof ShipAPI) {
            newTarget = (ShipAPI)this.target;
            if (newTarget.isPhased() && newTarget.isAlive()) {
               return false;
            }
         }

         newTarget = this.findBestTarget();
         if (newTarget != null) {
            this.target = newTarget;
         }
      }

      return true;
   }

   protected ShipAPI findBestTarget() {
      return this.findBestTarget(false);
   }

   protected ShipAPI findBestTarget(boolean allowDroneOrFighter) {
      ShipAPI best = null;
      float bestWeight = 0.0F;
      List<ShipAPI> ships = AIUtils.getEnemiesOnMap(this.missile);
      int size = ships.size();

      for(int i = 0; i < size; ++i) {
         ShipAPI tmp = (ShipAPI)ships.get(i);
         boolean valid = allowDroneOrFighter || !this.isDroneOrFighter(this.target);
         valid = valid && this.isTargetValid(tmp);
         if (valid) {
            float mod;
            switch(tmp.getHullSize()) {
            case FIGHTER:
            default:
               mod = 1.0F;
               break;
            case FRIGATE:
               mod = 10.0F;
               break;
            case DESTROYER:
               mod = 50.0F;
               break;
            case CRUISER:
               mod = 100.0F;
               break;
            case CAPITAL_SHIP:
               mod = 125.0F;
            }

            float weight = 4000.0F / Math.max(MathUtils.getDistance(tmp, this.missile.getLocation()), 750.0F) * mod;
            if (weight > bestWeight) {
               best = tmp;
               bestWeight = weight;
            }
         }
      }

      return best;
   }

   protected boolean isDroneOrFighter(CombatEntityAPI target) {
      if (target instanceof ShipAPI) {
         ShipAPI ship = (ShipAPI)target;
         if (ship.isFighter() || ship.isDrone()) {
            return true;
         }
      }

      return false;
   }
}
