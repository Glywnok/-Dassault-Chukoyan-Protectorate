package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_AutolaserSmallSparks implements OnHitEffectPlugin {
   private static final float SPARK_CHANCE = 0.9F;
   private static final Color PARTICLE_COLOR = new Color(110, 160, 255, 255);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 0.8F;
   private static final int PARTICLE_COUNT = 2;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.11F;
   private static final float VEL_MAX = 0.22F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.8999999761581421D) {
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         for(int i = 0; i <= 2; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.11F, speed * -0.22F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, 5.0F, 255.0F, 0.8F, PARTICLE_COLOR);
         }
      }

   }
}
