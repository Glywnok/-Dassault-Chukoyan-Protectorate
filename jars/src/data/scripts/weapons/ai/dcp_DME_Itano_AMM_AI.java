package data.scripts.weapons.ai;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import java.util.List;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_Itano_AMM_AI extends istl_BaseMissile {
   private static final float ENGINE_DEAD_TIME_MAX = 0.2F;
   private static final float ENGINE_DEAD_TIME_MIN = 0.12F;
   private static final float LEAD_GUIDANCE_FACTOR = 0.6F;
   private static final float LEAD_GUIDANCE_FACTOR_FROM_ECCM = 0.3F;
   private static final float VELOCITY_DAMPING_FACTOR = 0.1F;
   private static final float WEAVE_START_DISTANCE = 1000.0F;
   private static final float WEAVE_SINE_A_AMPLITUDE = 18.0F;
   private static final float WEAVE_SINE_A_PERIOD = 8.0F;
   private static final float WEAVE_SINE_B_AMPLITUDE = 30.0F;
   private static final float WEAVE_SINE_B_PERIOD = 12.0F;
   private static final Vector2f ZERO = new Vector2f();
   private boolean aspectLocked = true;
   private float engineDeadTime = MathUtils.getRandomNumberInRange(0.12F, 0.2F);
   private float timeAccum = 0.0F;
   private final float weaveSineAPhase = (float)(Math.random() * 3.141592653589793D * 2.0D);
   private final float weaveSineBPhase = (float)(Math.random() * 3.141592653589793D * 2.0D);

   public dcp_DME_Itano_AMM_AI(MissileAPI missile, ShipAPI launchingShip) {
      super(missile, launchingShip);
   }

   public void advance(float amount) {
      if (!this.missile.isFizzling() && !this.missile.isFading()) {
         if (this.engineDeadTime > 0.0F) {
            this.engineDeadTime -= amount;
         } else {
            this.timeAccum += amount;
            if (!this.acquireTarget(amount)) {
               this.missile.giveCommand(ShipCommand.ACCELERATE);
            } else {
               float distance = MathUtils.getDistance(this.target.getLocation(), this.missile.getLocation());
               float guidance = 0.6F;
               if (this.missile.getSource() != null) {
                  guidance += Math.min(this.missile.getSource().getMutableStats().getMissileGuidance().getModifiedValue() - this.missile.getSource().getMutableStats().getMissileGuidance().getBaseValue(), 1.0F) * 0.3F;
               }

               Vector2f guidedTarget = intercept(this.missile.getLocation(), this.missile.getVelocity().length(), this.target.getLocation(), this.target.getVelocity());
               float weaveSineB;
               if (guidedTarget == null) {
                  Vector2f projection = new Vector2f(this.target.getVelocity());
                  weaveSineB = distance / (this.missile.getVelocity().length() + 1.0F);
                  projection.scale(weaveSineB);
                  guidedTarget = Vector2f.add(this.target.getLocation(), projection, (Vector2f)null);
               }

               Vector2f.sub(guidedTarget, this.target.getLocation(), guidedTarget);
               guidedTarget.scale(guidance);
               Vector2f.add(guidedTarget, this.target.getLocation(), guidedTarget);
               float weaveSineA = 18.0F * (float)FastTrig.sin(6.283185307179586D * (double)this.timeAccum / 8.0D + (double)this.weaveSineAPhase);
               weaveSineB = 30.0F * (float)FastTrig.sin(6.283185307179586D * (double)this.timeAccum / 12.0D + (double)this.weaveSineBPhase);
               float weaveOffset = (weaveSineA + weaveSineB) * (1.0F - Math.min(1.0F, distance / 1000.0F));
               float angularDistance;
               if (this.aspectLocked) {
                  angularDistance = MathUtils.getShortestRotation(this.missile.getFacing(), MathUtils.clampAngle(VectorUtils.getAngle(this.missile.getLocation(), guidedTarget) + weaveOffset));
               } else {
                  angularDistance = MathUtils.getShortestRotation(this.missile.getFacing(), MathUtils.clampAngle(VectorUtils.getAngle(this.missile.getLocation(), guidedTarget)));
               }

               float absDAng = Math.abs(angularDistance);
               this.missile.giveCommand(angularDistance < 0.0F ? ShipCommand.TURN_RIGHT : ShipCommand.TURN_LEFT);
               if (this.aspectLocked && absDAng > 60.0F) {
                  this.aspectLocked = false;
               }

               if (!this.aspectLocked && absDAng <= 45.0F) {
                  this.aspectLocked = true;
               }

               if (this.aspectLocked) {
                  this.missile.giveCommand(ShipCommand.ACCELERATE);
               }

               if (absDAng < 5.0F) {
                  float MFlightAng = VectorUtils.getAngle(ZERO, this.missile.getVelocity());
                  float MFlightCC = MathUtils.getShortestRotation(this.missile.getFacing(), MFlightAng);
                  if (Math.abs(MFlightCC) > 20.0F) {
                     this.missile.giveCommand(MFlightCC < 0.0F ? ShipCommand.STRAFE_LEFT : ShipCommand.STRAFE_RIGHT);
                  }
               }

               if (absDAng < Math.abs(this.missile.getAngularVelocity()) * 0.1F) {
                  this.missile.setAngularVelocity(angularDistance / 0.1F);
               }

            }
         }
      }
   }

   protected boolean acquireTarget(float amount) {
      ShipAPI newTarget;
      if (!this.isTargetValidAlternate(this.target)) {
         if (this.target instanceof ShipAPI) {
            newTarget = (ShipAPI)this.target;
            if (newTarget.isPhased() && newTarget.isAlive()) {
               return false;
            }
         }

         this.setTarget(this.findBestTarget());
         if (this.target == null) {
            this.setTarget(this.findBestTargetAlternate());
         }

         if (this.target == null) {
            return false;
         }
      } else if (!this.isTargetValid(this.target)) {
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
      ShipAPI best = null;
      float bestWeight = 0.0F;
      List<ShipAPI> ships = AIUtils.getEnemiesOnMap(this.missile);
      int size = ships.size();

      for(int i = 0; i < size; ++i) {
         ShipAPI tmp = (ShipAPI)ships.get(i);
         float mod;
         if (!this.isTargetValid(tmp)) {
            mod = 0.0F;
         } else {
            switch(tmp.getHullSize()) {
            case FIGHTER:
            default:
               mod = 150.0F;
               break;
            case FRIGATE:
               mod = 75.0F;
               break;
            case DESTROYER:
               mod = 35.0F;
               break;
            case CRUISER:
               mod = 7.0F;
               break;
            case CAPITAL_SHIP:
               mod = 1.0F;
            }
         }

         float weight = 1500.0F / Math.max(MathUtils.getDistance(tmp, this.missile.getLocation()), 750.0F) * mod;
         if (weight > bestWeight) {
            best = tmp;
            bestWeight = weight;
         }
      }

      return best;
   }

   protected ShipAPI findBestTargetAlternate() {
      ShipAPI best = null;
      float bestWeight = 0.0F;
      List<ShipAPI> ships = AIUtils.getEnemiesOnMap(this.missile);
      int size = ships.size();

      for(int i = 0; i < size; ++i) {
         ShipAPI tmp = (ShipAPI)ships.get(i);
         float mod;
         if (!this.isTargetValidAlternate(tmp)) {
            mod = 0.0F;
         } else {
            switch(tmp.getHullSize()) {
            case FIGHTER:
            default:
               mod = 125.0F;
               break;
            case FRIGATE:
               mod = 75.0F;
               break;
            case DESTROYER:
               mod = 50.0F;
               break;
            case CRUISER:
               mod = 10.0F;
               break;
            case CAPITAL_SHIP:
               mod = 1.0F;
            }
         }

         float weight = 1500.0F / Math.max(MathUtils.getDistance(tmp, this.missile.getLocation()), 750.0F) * mod;
         if (weight > bestWeight) {
            best = tmp;
            bestWeight = weight;
         }
      }

      return best;
   }

   protected boolean isTargetValidAlternate(CombatEntityAPI target) {
      return super.isTargetValid(target);
   }
}
