package data.scripts.weapons.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.GuidedMissileAI;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.CollectionUtils.SortEntitiesByDistance;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_BaseMissile implements MissileAIPlugin, GuidedMissileAI {
   private static final float RETARGET_TIME = 0.0F;
   protected ShipAPI launchingShip;
   protected MissileAPI missile;
   protected float retargetTimer = 0.0F;
   protected CombatEntityAPI target;

   private static Vector2f quad(float a, float b, float c) {
      Vector2f solution = null;
      if (Float.compare(Math.abs(a), 0.0F) == 0) {
         if (Float.compare(Math.abs(b), 0.0F) == 0) {
            solution = Float.compare(Math.abs(c), 0.0F) == 0 ? new Vector2f(0.0F, 0.0F) : null;
         } else {
            solution = new Vector2f(-c / b, -c / b);
         }
      } else {
         float d = b * b - 4.0F * a * c;
         if (d >= 0.0F) {
            d = (float)Math.sqrt((double)d);
            float e = 2.0F * a;
            solution = new Vector2f((-b - d) / e, (-b + d) / e);
         }
      }

      return solution;
   }

   static List<ShipAPI> getSortedDirectTargets(ShipAPI launchingShip) {
      List<ShipAPI> directTargets = CombatUtils.getShipsWithinRange(launchingShip.getMouseTarget(), 300.0F);
      if (!directTargets.isEmpty()) {
         Collections.sort(directTargets, new SortEntitiesByDistance(launchingShip.getMouseTarget()));
      }

      return directTargets;
   }

   static Vector2f intercept(Vector2f point, float speed, Vector2f target, Vector2f targetVel) {
      Vector2f difference = new Vector2f(target.x - point.x, target.y - point.y);
      float a = targetVel.x * targetVel.x + targetVel.y * targetVel.y - speed * speed;
      float b = 2.0F * (targetVel.x * difference.x + targetVel.y * difference.y);
      float c = difference.x * difference.x + difference.y * difference.y;
      Vector2f solutionSet = quad(a, b, c);
      Vector2f intercept = null;
      if (solutionSet != null) {
         float bestFit = Math.min(solutionSet.x, solutionSet.y);
         if (bestFit < 0.0F) {
            bestFit = Math.max(solutionSet.x, solutionSet.y);
         }

         if (bestFit > 0.0F) {
            intercept = new Vector2f(target.x + targetVel.x * bestFit, target.y + targetVel.y * bestFit);
         }
      }

      return intercept;
   }

   static Vector2f interceptAdvanced(Vector2f point, float speed, float acceleration, float maxspeed, Vector2f target, Vector2f targetVel) {
      Vector2f difference = new Vector2f(target.x - point.x, target.y - point.y);
      float s = speed;
      float a = acceleration / 2.0F;
      float c = difference.length();
      Vector2f solutionSet = quad(a, speed, c);
      if (solutionSet != null) {
         float t = Math.min(solutionSet.x, solutionSet.y);
         if (t < 0.0F) {
            t = Math.max(solutionSet.x, solutionSet.y);
         }

         if (t > 0.0F) {
            s = acceleration * t;
            s = s / 2.0F + speed;
            s = Math.min(s, maxspeed);
         }
      }

      a = targetVel.x * targetVel.x + targetVel.y * targetVel.y - s * s;
      float b = 2.0F * (targetVel.x * difference.x + targetVel.y * difference.y);
      c = difference.x * difference.x + difference.y * difference.y;
      solutionSet = quad(a, b, c);
      Vector2f intercept = null;
      if (solutionSet != null) {
         float bestFit = Math.min(solutionSet.x, solutionSet.y);
         if (bestFit < 0.0F) {
            bestFit = Math.max(solutionSet.x, solutionSet.y);
         }

         if (bestFit > 0.0F) {
            intercept = new Vector2f(target.x + targetVel.x * bestFit, target.y + targetVel.y * bestFit);
         }
      }

      return intercept;
   }

   public dcp_DME_BaseMissile(MissileAPI missile, ShipAPI launchingShip) {
      this.missile = missile;
      this.launchingShip = launchingShip;
      this.defaultInitialTargetingBehavior(launchingShip);
   }

   public void advance(float amount) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public CombatEntityAPI getTarget() {
      return this.target;
   }

   public final void setTarget(CombatEntityAPI target) {
      this.target = target;
   }

   private void defaultInitialTargetingBehavior(ShipAPI launchingShip) {
      this.assignMissileToShipTarget(launchingShip);
      if (this.target == null) {
         this.setTarget(this.getMouseTarget(launchingShip));
      }

      if (this.target == null) {
         this.setTarget(this.findBestTarget());
      }

   }

   protected boolean acquireTarget(float amount) {
      if (!this.isTargetValid(this.target)) {
         if (this.target instanceof ShipAPI) {
            ShipAPI ship = (ShipAPI)this.target;
            if (ship.isPhased() && ship.isAlive()) {
               return false;
            }
         }

         this.setTarget(this.findBestTarget());
         if (this.target == null) {
            return false;
         }
      }

      return true;
   }

   protected void assignMissileToShipTarget(ShipAPI launchingShip) {
      if (this.isTargetValid(launchingShip.getShipTarget())) {
         this.setTarget(launchingShip.getShipTarget());
      }

   }

   protected CombatEntityAPI findBestTarget() {
      ShipAPI closest = null;
      float range = this.getRemainingRange();
      float closestDistance = Float.MAX_VALUE;
      List<ShipAPI> ships = AIUtils.getEnemiesOnMap(this.missile);
      int size = ships.size();

      for(int i = 0; i < size; ++i) {
         ShipAPI tmp = (ShipAPI)ships.get(i);
         float mod = 0.0F;
         if (tmp.isFighter() || tmp.isDrone()) {
            mod = range / 2.0F;
         }

         if (!this.isTargetValid(tmp)) {
            mod = range;
         }

         float distance = MathUtils.getDistance(tmp, this.missile.getLocation()) + mod;
         if (distance < closestDistance) {
            closest = tmp;
            closestDistance = distance;
         }
      }

      return closest;
   }

   protected CombatEntityAPI getMouseTarget(ShipAPI launchingShip) {
      ListIterator iter = getSortedDirectTargets(launchingShip).listIterator();

      ShipAPI tmp;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         tmp = (ShipAPI)iter.next();
      } while(!this.isTargetValid(tmp));

      return tmp;
   }

   protected float getRange() {
      float maxTime = this.missile.getMaxFlightTime();
      float speed = this.missile.getMaxSpeed();
      return speed * maxTime;
   }

   protected float getRemainingRange() {
      float time = this.missile.getMaxFlightTime() - this.missile.getFlightTime();
      float speed = this.missile.getMaxSpeed();
      return speed * time;
   }

   protected boolean isTargetValid(CombatEntityAPI target) {
      if (target != null && this.missile.getOwner() != target.getOwner() && Global.getCombatEngine().isEntityInPlay(target)) {
         if (target instanceof ShipAPI) {
            ShipAPI ship = (ShipAPI)target;
            if (ship.isPhased() || !ship.isAlive()) {
               return false;
            }
         } else if (target.getCollisionClass() == CollisionClass.NONE) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }
}
