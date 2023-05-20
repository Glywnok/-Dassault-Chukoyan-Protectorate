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

public class dcp_magellan_KineticSpallOnHit implements OnHitEffectPlugin {
   private static final float CRIT_CHANCE = 0.25F;
   private static final Color EXPLOSION_COLOR = new Color(235, 235, 255, 255);
   private static final Color PARTICLE_COLOR = new Color(235, 235, 255, 225);
   private static final Color GLOW_COLOR = new Color(85, 85, 100, 25);
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float CONE_ANGLE = 120.0F;
   private static final float VEL_MIN = 0.06F;
   private static final float VEL_MAX = 0.12F;
   private static final float A_2 = 60.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f loc_target = new Vector2f(target.getLocation());
      Vector2f v_target = new Vector2f(target.getVelocity());
      Vector2f v_proj = new Vector2f(projectile.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
      String projid = projectile.getProjectileSpecId();
      byte var22 = -1;
      switch(projid.hashCode()) {
      case -1319076437:
         if (projid.equals("dcp_magellan_quenchgun_shot")) {
            var22 = 2;
         }
         break;
      case -236207448:
         if (projid.equals("dcp_magellan_bigsolenoid_shot")) {
            var22 = 1;
         }
         break;
      case 720592249:
         if (projid.equals("dcp_magellan_lilsolenoid_shot")) {
            var22 = 0;
         }
         break;
      case 1852173482:
         if (projid.equals("dcp_magellan_quenchcannon_shot")) {
            var22 = 3;
         }
      }

      float explosion_size;
      float explosion_dur;
      float particle_size;
      float particle_dur;
      float damagemin_mult;
      float damagemax_mult;
      float pushmult;
      byte particle_count;
      String spall_sfx;
      switch(var22) {
      case 0:
         explosion_size = 30.0F;
         explosion_dur = 0.3F;
         particle_count = 4;
         particle_dur = 1.2F;
         particle_size = 6.0F;
         damagemin_mult = 0.25F;
         damagemax_mult = 0.5F;
         pushmult = 0.0F;
         spall_sfx = "dcp_magellan_kineticspall_sm_crit";
         break;
      case 1:
         explosion_size = 50.0F;
         explosion_dur = 0.5F;
         particle_count = 6;
         particle_size = 6.0F;
         particle_dur = 1.5F;
         damagemin_mult = 0.5F;
         damagemax_mult = 0.667F;
         pushmult = 0.0F;
         spall_sfx = "dcp_magellan_kineticspall_crit";
         break;
      case 2:
         explosion_size = 50.0F;
         explosion_dur = 0.5F;
         particle_count = 6;
         particle_size = 6.0F;
         particle_dur = 1.5F;
         damagemin_mult = 0.5F;
         damagemax_mult = 0.667F;
         pushmult = 0.05F;
         spall_sfx = "dcp_magellan_kineticspall_crit";
         break;
      case 3:
         explosion_size = 75.0F;
         explosion_dur = 0.6F;
         particle_count = 10;
         particle_size = 7.0F;
         particle_dur = 2.5F;
         damagemin_mult = 0.333F;
         damagemax_mult = 0.5F;
         pushmult = 0.1F;
         spall_sfx = "dcp_magellan_kineticspall_crit";
         break;
      default:
         return;
      }

      float speed;
      float angle;
      float facing;
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.25D) {
         speed = projectile.getDamageAmount() * damagemin_mult;
         facing = projectile.getDamageAmount() * damagemax_mult;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(speed, facing), DamageType.FRAGMENTATION, 0.0F, false, false, projectile.getSource());
         engine.spawnExplosion(point, v_comp, EXPLOSION_COLOR, explosion_size, explosion_dur);
         Global.getSoundPlayer().playSound(spall_sfx, 1.0F, 1.0F, loc_target, v_comp);
         float speed1 = projectile.getVelocity().length();
         angle = projectile.getFacing();

         for(int i = 0; i <= particle_count; ++i) {
            float angle1 = MathUtils.getRandomNumberInRange(angle - 60.0F, angle + 60.0F);
            float vel = MathUtils.getRandomNumberInRange(speed1 * -0.06F, speed * -0.12F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, particle_size, 255.0F, particle_dur, PARTICLE_COLOR);
            engine.addHitParticle(point, vector, particle_size * 4.0F, 255.0F, particle_dur * 0.75F, GLOW_COLOR);
         }
      } else if (target instanceof ShipAPI && !shieldHit) {
         engine.spawnExplosion(point, v_comp, EXPLOSION_COLOR, explosion_size * 0.6F, explosion_dur * 0.6F);
         speed = projectile.getVelocity().length();
         facing = projectile.getFacing();

         for(int i = 0; i <= particle_count / 2; ++i) {
            angle = MathUtils.getRandomNumberInRange(facing - 60.0F, facing + 60.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.06F, speed * -0.12F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, particle_size, 255.0F, particle_dur * 0.5F, PARTICLE_COLOR);
            engine.addHitParticle(point, vector, particle_size * 4.0F, 255.0F, particle_dur * 0.3F, GLOW_COLOR);
         }
      }

      if (pushmult != 0.0F) {
         Vector2f projectile_vel = projectile.getVelocity();
         facing = projectile_vel.length();
         CombatUtils.applyForce(target, projectile_vel, facing * pushmult);
      }

   }
}
