package data.scripts.weapons;

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

public class dcp_DME_SigmaBusterOnHit implements OnHitEffectPlugin {
   private static final float CRIT_DAMAGE_MIN_MULT = 0.25F;
   private static final float CRIT_DAMAGE_MAX_MULT = 0.5F;
   private static final float CRIT_CHANCE = 1.0F;
   private static final Color EXPLOSION_COLOR = new Color(75, 255, 175, 255);
   private static final Color NEBULA_COLOR = new Color(25, 255, 155, 255);
   private static final float NEBULA_DUR = 0.8F;
   private static final float NEBULA_RAMPUP = 0.2F;
   private static final Color BRIGHT_COLOR = new Color(155, 255, 225, 255);
   private static final Color DIM_COLOR = new Color(0, 100, 100, 30);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 2.0F;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.12F;
   private static final float VEL_MAX = 0.2F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      float nebula_size;
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 1.0D) {
         float critminmult = projectile.getDamageAmount() * 0.25F;
         nebula_size = projectile.getDamageAmount() * 0.5F;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(critminmult, nebula_size), DamageType.HIGH_EXPLOSIVE, 0.0F, false, false, projectile.getSource());
      }

      Vector2f v_target = new Vector2f(target.getVelocity());
      String projid = projectile.getProjectileSpecId();
      byte var13 = -1;
      switch(projid.hashCode()) {
      case 84483858:
         if (projid.equals("istl_phasedbuster_shot")) {
            var13 = 1;
         }
         break;
      case 1701283094:
         if (projid.equals("istl_bravemissile_shot")) {
            var13 = 0;
         }
      }

      float nebula_size_mult;
      byte particle_count;
      switch(var13) {
      case 0:
         nebula_size = 5.0F;
         nebula_size_mult = 15.0F;
         particle_count = 2;
         break;
      case 1:
         nebula_size = 7.0F;
         nebula_size_mult = 21.0F;
         particle_count = 3;
         break;
      default:
         return;
      }

      engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, nebula_size * 8.0F, 0.2F);
      engine.addNebulaParticle(point, v_target, nebula_size, nebula_size_mult, 0.2F, 0.25F, 0.8F, NEBULA_COLOR, true);
      engine.addHitParticle(point, v_target, nebula_size * 16.0F, 1.0F, 0.1F, DIM_COLOR);
      float speed = projectile.getVelocity().length();
      float facing = projectile.getFacing();

      for(int i = 0; i <= particle_count; ++i) {
         float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
         float vel = MathUtils.getRandomNumberInRange(speed * -0.12F, speed * -0.2F);
         Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
         engine.addHitParticle(point, vector, 5.0F, 255.0F, 2.0F, BRIGHT_COLOR);
         engine.addHitParticle(point, vector, 25.0F, 255.0F, 1.5F, DIM_COLOR);
      }

   }
}
