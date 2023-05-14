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

public class dcp_DME_ShellerOnHit implements OnHitEffectPlugin {
   private static final float CRIT_DAMAGE_MULT = 1.25F;
   private static final float CRIT_CHANCE = 0.15F;
   private static final Color NEBULA_COLOR = new Color(75, 90, 255, 200);
   private static final float NEBULA_SIZE = 6.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 21.0F;
   private static final float NEBULA_DUR = 0.8F;
   private static final float NEBULA_RAMPUP = 0.2F;
   private static final Color EXPLOSION_COLOR = new Color(125, 135, 255, 255);
   private static final float EXPLOSION_DUR_MULT = 0.6F;
   private static final String SFX = "istl_kinetic_crit_micro";
   private static final Color PARTICLE_COLOR = new Color(100, 110, 255, 255);
   private static final float PARTICLE_SIZE = 3.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 0.6F;
   private static final int PARTICLE_COUNT = 3;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.2F;
   private static final float VEL_MAX = 0.3F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.15000000596046448D) {
         float critmult = projectile.getDamageAmount() * 1.25F;
         engine.applyDamage(target, point, critmult, DamageType.ENERGY, 100.0F, false, false, projectile.getSource());
         engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 21.0F, 0.2F, 0.2F, 0.8F, NEBULA_COLOR, true);
         Global.getSoundPlayer().playSound("istl_kinetic_crit_micro", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         for(int i = 0; i <= 3; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.2F, speed * -0.3F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, 3.0F, 255.0F, 0.6F, PARTICLE_COLOR);
         }
      }

      engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, NEBULA_SIZE * 2.0F, 0.48000002F);
   }
}
