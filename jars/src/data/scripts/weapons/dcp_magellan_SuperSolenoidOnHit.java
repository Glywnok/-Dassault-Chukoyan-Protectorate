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
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_SuperSolenoidOnHit implements OnHitEffectPlugin {
   private static final int MIN_ARCS = 1;
   private static final float ARC_DAMAGE = 0.1F;
   private static final float ARC_EMP = 0.5F;
   private static final float FLUXRAISE_MULT = 1.0F;
   private static final Color NEBULA_COLOR = new Color(40, 140, 160, 200);
   private static final float NEBULA_SIZE_MULT = 15.0F;
   private static final Color PARTICLE_COLOR = new Color(140, 190, 210, 255);
   private static final Color GLOW_COLOR = new Color(5, 90, 110, 75);
   private static final float PARTICLE_SIZE = 4.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float CONE_ANGLE = 150.0F;
   private static final float A_2 = 75.0F;
   private static final float A_3 = 50.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f loc_target = new Vector2f(target.getLocation());
      Vector2f v_target = new Vector2f(target.getVelocity());
      String projid = projectile.getProjectileSpecId();
      byte var21 = -1;
      switch(projid.hashCode()) {
      case -124460084:
         if (projid.equals("magellan_supersolenoid_sm_shot")) {
            var21 = 0;
         }
         break;
      case 372238349:
         if (projid.equals("magellan_supersolenoid_shot")) {
            var21 = 1;
         }
      }

      byte maxarcs;
      float nebula_size;
      float nebula_dur;
      float nebula_rampup;
      float particle_dur;
      float velmin_mult;
      float velmax_mult;
      float pushmult;
      byte particle_count;
      String hit_sfx;
      switch(var21) {
      case 0:
         maxarcs = 2;
         nebula_size = 8.0F * (0.75F + (float)Math.random() * 0.5F);
         nebula_dur = 1.2F;
         nebula_rampup = 0.15F;
         particle_count = 3;
         particle_dur = 1.5F;
         velmin_mult = 0.04F;
         velmax_mult = 0.2F;
         pushmult = 0.05F;
         hit_sfx = "magellan_electron_crit_sm";
         break;
      case 1:
         maxarcs = 3;
         nebula_size = 10.0F * (0.75F + (float)Math.random() * 0.5F);
         nebula_dur = 1.5F;
         nebula_rampup = 0.25F;
         particle_count = 5;
         particle_dur = 2.0F;
         velmin_mult = 0.03F;
         velmax_mult = 0.15F;
         pushmult = 0.1F;
         hit_sfx = "magellan_electron_crit";
         break;
      default:
         return;
      }

      int i;
      float angle;
      float vel;
      float speed;
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         float dam = projectile.getDamageAmount() * 0.1F;
         speed = projectile.getEmpAmount() * 0.5F;
         int arcs = MathUtils.getRandomNumberInRange(1, maxarcs);

         for(i = 0; i < arcs; ++i) {
            engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, speed, 100000.0F, "tachyon_lance_emp_impact", 25.0F, GLOW_COLOR, PARTICLE_COLOR);
         }

         Global.getSoundPlayer().playSound(hit_sfx, 1.0F, 1.0F, loc_target, v_target);
         ShipAPI targetship = (ShipAPI)target;
         angle = projectile.getDamageAmount() * 1.0F;
         vel = targetship.getMaxFlux();
         if (vel > angle * 1.5F) {
            targetship.getFluxTracker().increaseFlux(angle, true);
         }
      }

      engine.addSwirlyNebulaParticle(point, v_target, nebula_size, 15.0F, nebula_rampup, 0.3F, nebula_dur, NEBULA_COLOR, true);
      engine.spawnExplosion(point, v_target, PARTICLE_COLOR, nebula_size * 4.0F, nebula_dur / 2.0F);
      Vector2f projectile_vel = projectile.getVelocity();
      speed = projectile_vel.length();
      float facing = projectile.getFacing();

      Vector2f vector;
      for(i = 0; i <= particle_count; ++i) {
         angle = MathUtils.getRandomNumberInRange(facing - 50.0F, facing + 50.0F);
         vel = MathUtils.getRandomNumberInRange(speed * -velmin_mult, speed * -velmax_mult);
         vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
         engine.addHitParticle(point, vector, 4.0F, 255.0F, particle_dur, PARTICLE_COLOR);
         engine.addHitParticle(point, vector, 16.0F, 255.0F, particle_dur * 0.75F, GLOW_COLOR);
      }

      for(i = 0; i <= particle_count * 2; ++i) {
         angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
         vel = MathUtils.getRandomNumberInRange(speed * -velmin_mult, speed * -velmax_mult);
         vector = MathUtils.getPointOnCircumference((Vector2f)null, vel * 1.5F, angle);
         engine.addHitParticle(point, vector, 8.0F, 255.0F, particle_dur * 0.75F, GLOW_COLOR);
      }

      CombatUtils.applyForce(target, projectile_vel, speed * pushmult);
   }
}
