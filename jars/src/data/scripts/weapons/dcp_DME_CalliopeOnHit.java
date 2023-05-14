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

public class dcp_DME_CalliopeOnHit implements OnHitEffectPlugin {
   private static final float CRIT_DAMAGE_MIN_MULT = 0.125F;
   private static final float CRIT_DAMAGE_MAX_MULT = 0.25F;
   private static final float CRIT_CHANCE = 1.0F;
   private static final Color EXPLOSION_COLOR = new Color(185, 175, 100, 255);
   private static final String SFX = "istl_ballistic_crit_sm";
   private static final Color PARTICLE_COLOR = new Color(205, 195, 120, 225);
   private static final float PARTICLE_SIZE = 4.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 0.6F;
   private static final int PARTICLE_COUNT = 3;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.2F;
   private static final float VEL_MAX = 0.3F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      float speed;
      float facing;
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 1.0D) {
         speed = projectile.getDamageAmount() * 0.125F;
         facing = projectile.getDamageAmount() * 0.25F;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(speed, facing), DamageType.HIGH_EXPLOSIVE, 0.0F, false, false, projectile.getSource());
      }

      engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, 75.0F, 0.1F);
      speed = projectile.getVelocity().length();
      facing = projectile.getFacing();

      for(int i = 0; i <= 3; ++i) {
         float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
         float vel = MathUtils.getRandomNumberInRange(speed * -0.2F, speed * -0.3F);
         Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
         engine.addHitParticle(point, vector, 4.0F, 255.0F, 0.6F, PARTICLE_COLOR);
      }

      Global.getSoundPlayer().playSound("istl_ballistic_crit_sm", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
   }
}
