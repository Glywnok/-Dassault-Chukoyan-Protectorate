package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_ShellbackOnHit implements OnHitEffectPlugin {
   private static final float FLUXRAISE_MULT = 1.0F;
   private static final Color EXPLOSION_COLOR = new Color(75, 90, 255, 200);
   private static final Color PARTICLE_COLOR = new Color(100, 110, 255, 255);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 1.2F;
   private static final int PARTICLE_COUNT = 2;
   private static final float CONE_ANGLE = 75.0F;
   private static final float VEL_MIN = 0.12F;
   private static final float VEL_MAX = 0.2F;
   private static final float A_2 = 37.5F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit && target instanceof ShipAPI) {
         ShipAPI targetship = (ShipAPI)target;
         engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, 15.0F, 0.3F);
         float fluxmult = projectile.getDamageAmount() * 1.0F;
         float maxflux = targetship.getMaxFlux();
         if (maxflux > fluxmult * 1.5F) {
            targetship.getFluxTracker().increaseFlux(fluxmult, false);
         }

         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         for(int i = 0; i <= 2; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 37.5F, facing + 37.5F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.12F, speed * -0.2F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, 5.0F, 255.0F, 1.2F, PARTICLE_COLOR);
         }
      }

   }
}
