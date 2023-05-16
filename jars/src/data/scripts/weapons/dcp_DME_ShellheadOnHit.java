package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_ShellheadOnHit implements OnHitEffectPlugin {
   private static final float FLUXRAISE_MIN_MULT = 0.75F;
   private static final float FLUXRAISE_MAX_MULT = 1.0F;
   private static final float CRIT_DAMAGE_MULT = 0.5F;
   private static final float CRIT_CHANCE = 0.2F;
   private static final Color EXPLOSION_COLOR = new Color(75, 90, 255, 200);
   private static final Color NEBULA_COLOR = new Color(125, 135, 255, 255);
   private static final float NEBULA_SIZE = 10.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 16.0F;
   private static final float NEBULA_DUR = 0.6F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final float EXPLOSION_DUR_MULT = 0.8F;
   private static final String SFX = "dcp_DME_kinetic_crit_med";
   private static final Color PARTICLE_COLOR = new Color(100, 110, 255, 255);
   private static final float PARTICLE_SIZE = 7.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 2.0F;
   private static final int PARTICLE_COUNT = 5;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.1F;
   private static final float VEL_MAX = 0.2F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      float fluxminmult;
      float fluxmaxmult;
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.20000000298023224D) {
         float critmult = projectile.getDamageAmount() * 0.5F;
         engine.applyDamage(target, point, critmult, DamageType.ENERGY, 200.0F, false, false, projectile.getSource());
         engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 16.0F, 0.1F, 0.3F, 0.6F, NEBULA_COLOR, true);
         Global.getSoundPlayer().playSound("dcp_DME_kinetic_crit_med", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
         fluxminmult = projectile.getVelocity().length();
         fluxmaxmult = projectile.getFacing();

         for(int i = 0; i <= 5; ++i) {
            float angle = MathUtils.getRandomNumberInRange(fluxmaxmult - 75.0F, fluxmaxmult + 75.0F);
            float vel = MathUtils.getRandomNumberInRange(fluxminmult * -0.1F, fluxminmult * -0.2F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, 7.0F, 255.0F, 2.0F, PARTICLE_COLOR);
         }
      }

      if (!shieldHit && target instanceof ShipAPI) {
         ShipAPI targetship = (ShipAPI)target;
         engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, NEBULA_SIZE * 3.0F, 0.48000002F);
         fluxminmult = projectile.getDamageAmount() * 0.75F;
         fluxmaxmult = projectile.getDamageAmount() * 1.0F;
         float maxflux = targetship.getMaxFlux();
         if (maxflux > fluxmaxmult * 1.5F) {
            targetship.getFluxTracker().increaseFlux(MathUtils.getRandomNumberInRange(fluxminmult, fluxmaxmult), false);
         }
      }

   }
}
