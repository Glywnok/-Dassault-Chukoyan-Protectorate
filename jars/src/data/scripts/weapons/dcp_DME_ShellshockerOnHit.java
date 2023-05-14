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

public class dcp_DME_ShellshockerOnHit implements OnHitEffectPlugin {
   private static final int MIN_ARCS = 3;
   private static final int MAX_ARCS = 5;
   private static final float ARC_DAMAGE = 0.1667F;
   private static final float ARC_EMP = 0.2F;
   private static final String SFX = "istl_kinetic_crit_med";
   private static final float FLUXRAISE_MULT = 0.6F;
   private static final Color NEBULA_COLOR = new Color(75, 90, 255, 200);
   private static final float NEBULA_SIZE = 10.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 20.0F;
   private static final float NEBULA_DUR = 0.8F;
   private static final float NEBULA_RAMPUP = 0.15F;
   private static final Color EXPLOSION_COLOR = new Color(125, 135, 255, 255);
   private static final float EXPLOSION_DUR_MULT = 0.75F;
   private static final Color PARTICLE_COLOR = new Color(120, 135, 255, 255);
   private static final float PARTICLE_SIZE = 7.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 2.0F;
   private static final int PARTICLE_COUNT = 5;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.08F;
   private static final float VEL_MAX = 0.2F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f v_target = new Vector2f(target.getVelocity());
      engine.addNebulaParticle(point, v_target, NEBULA_SIZE, 20.0F, 0.15F, 0.3F, 0.8F, NEBULA_COLOR, true);
      engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, NEBULA_SIZE * 3.0F, 0.6F);
      if (!shieldHit && target instanceof ShipAPI) {
         ShipAPI targetship = (ShipAPI)target;
         float dam = projectile.getDamageAmount() * 0.1667F;
         float emp = projectile.getEmpAmount() * 0.2F;
         int arcs = MathUtils.getRandomNumberInRange(3, 5);

         for(int i = 0; i < arcs; ++i) {
            engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 100000.0F, "tachyon_lance_emp_impact", 25.0F, new Color(50, 55, 155, 255), new Color(200, 220, 255, 255));
         }

         Global.getSoundPlayer().playSound("istl_kinetic_crit_med", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         float maxflux;
         for(int i2 = 0; i2 <= 5; ++i2) {
            maxflux = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.08F, speed * -0.2F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, maxflux);
            engine.addHitParticle(point, vector, 7.0F, 255.0F, 2.0F, PARTICLE_COLOR);
         }

         float fluxmult = projectile.getDamageAmount() * 0.6F;
         maxflux = targetship.getMaxFlux();
         if (maxflux > fluxmult * 1.5F) {
            targetship.getFluxTracker().increaseFlux(fluxmult, true);
         }
      }

   }
}
