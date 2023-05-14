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

public class dcp_DME_LinearMGSparks implements OnHitEffectPlugin {
   private static final float SPARK_CHANCE = 0.3F;
   private static final Color PARTICLE_COLOR = new Color(155, 225, 255, 255);
   private static final float PARTICLE_SIZE = 2.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 0.6F;
   private static final int PARTICLE_COUNT = 1;
   private static final float CONE_ANGLE = 100.0F;
   private static final float VEL_MIN = 0.1F;
   private static final float VEL_MAX = 0.2F;
   private static final float A_2 = 50.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.30000001192092896D) {
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         for(int i = 0; i <= 1; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 50.0F, facing + 50.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.1F, speed * -0.2F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, 2.0F, 255.0F, 0.6F, PARTICLE_COLOR);
         }
      }

   }
}
