package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_LensHDEMOnHit implements OnHitEffectPlugin {
   private static final float CRIT_DAMAGE_MIN_MULT = 0.5F;
   private static final float CRIT_DAMAGE_MAX_MULT = 1.0F;
   private static final float CRIT_CHANCE = 0.5F;
   private static final Color EXPLOSION_COLOR = new Color(75, 255, 175, 200);
   private static final float NEBULA_SIZE = 40.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_DUR = 1.0F;
   private static final float NEBULA_RAMPUP = 0.15F;
   private static final Color BRIGHT_COLOR = new Color(155, 255, 225, 255);
   private static final Color DIM_COLOR = new Color(0, 100, 100, 30);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 1.2F;
   private static final int PARTICLE_COUNT = 6;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.08F;
   private static final float VEL_MAX = 0.12F;
   private static final float A_2 = 75.0F;
   private static final String SFX = "istl_plasmadriver_fire";

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      float NEBULA_SIZE_MULT;
      float speed;
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.5D) {
         NEBULA_SIZE_MULT = projectile.getDamageAmount() * 0.5F;
         speed = projectile.getDamageAmount() * 1.0F;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(NEBULA_SIZE_MULT, speed), DamageType.FRAGMENTATION, 0.0F, false, false, projectile.getSource());
      }

      NEBULA_SIZE_MULT = Misc.getHitGlowSize(60.0F, projectile.getDamage().getBaseDamage(), damageResult) / 100.0F;
      engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 5.0F + 3.0F * NEBULA_SIZE_MULT, 0.15F, 0.0F, 1.0F, EXPLOSION_COLOR, true);
      engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, NEBULA_SIZE * 4.0F, 0.25F);
      Global.getSoundPlayer().playSound("istl_plasmadriver_fire", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
      speed = projectile.getVelocity().length();
      float facing = projectile.getFacing();

      for(int i = 0; i <= 6; ++i) {
         float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
         float vel = MathUtils.getRandomNumberInRange(speed * -0.08F, speed * -0.12F);
         Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
         engine.addHitParticle(point, vector, 5.0F, 255.0F, 1.2F, BRIGHT_COLOR);
         engine.addHitParticle(point, vector, 25.0F, 255.0F, 0.90000004F, DIM_COLOR);
      }

   }
}
