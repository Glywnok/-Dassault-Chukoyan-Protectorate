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

public class dcp_DME_PhaseBlasterOnHit implements OnHitEffectPlugin {
   private static final float CRIT_DAMAGE_MIN_MULT = 0.375F;
   private static final float CRIT_DAMAGE_MAX_MULT = 0.625F;
   private static final float CRIT_CHANCE = 1.0F;
   private static final Color EXPLOSION_COLOR = new Color(90, 255, 200, 255);
   private static final Color NEBULA_COLOR = new Color(25, 255, 155, 255);
   private static final float NEBULA_SIZE = 9.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 15.0F;
   private static final float NEBULA_DUR = 0.75F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final Color BRIGHT_COLOR = new Color(155, 255, 225, 255);
   private static final Color DIM_COLOR = new Color(0, 100, 100, 30);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 1.2F;
   private static final int PARTICLE_COUNT = 4;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.08F;
   private static final float VEL_MAX = 0.12F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      float speed;
      float facing;
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 1.0D) {
         speed = projectile.getDamageAmount() * 0.375F;
         facing = projectile.getDamageAmount() * 0.625F;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(speed, facing), DamageType.FRAGMENTATION, 0.0F, false, false, projectile.getSource());
      }

      engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, NEBULA_SIZE * 5.0F, 0.3F);
      engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 15.0F, 0.1F, 0.25F, 0.75F, NEBULA_COLOR, true);
      speed = projectile.getVelocity().length();
      facing = projectile.getFacing();

      for(int i = 0; i <= 4; ++i) {
         float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
         float vel = MathUtils.getRandomNumberInRange(speed * -0.08F, speed * -0.12F);
         Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
         engine.addHitParticle(point, vector, 5.0F, 255.0F, 1.2F, BRIGHT_COLOR);
         engine.addHitParticle(point, vector, 25.0F, 255.0F, 0.90000004F, DIM_COLOR);
      }

   }
}
