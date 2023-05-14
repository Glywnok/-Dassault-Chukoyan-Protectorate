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

public class dcp_DME_ScatterlaserSparks implements OnHitEffectPlugin {
   private static final int CRIT_DAMAGE_MIN = 50;
   private static final int CRIT_DAMAGE_MAX = 150;
   private static final float CRIT_CHANCE = 1.0F;
   private static final Color EXPLOSION_COLOR = new Color(125, 175, 255, 255);
   private static final Color PARTICLE_COLOR = new Color(125, 175, 255, 255);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 1.2F;
   private static final int PARTICLE_COUNT = 16;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.12F;
   private static final float VEL_MAX = 0.24F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 1.0D) {
         engine.applyDamage(target, point, (float)MathUtils.getRandomNumberInRange(50, 150), DamageType.FRAGMENTATION, 0.0F, false, false, projectile.getSource());
         engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, 50.0F, 0.3F);
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         for(int i = 0; i <= 16; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.12F, speed * -0.24F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, 5.0F, 255.0F, 1.2F, PARTICLE_COLOR);
         }
      }

   }
}
