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

public class dcp_DME_LinearCannonSparks implements OnHitEffectPlugin {
   private static final Color BRIGHT_COLOR = new Color(155, 225, 255, 255);
   private static final Color DIM_COLOR = new Color(0, 50, 100, 25);
   private static final float PARTICLE_SIZE = 2.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 1.0F;
   private static final int PARTICLE_COUNT = 1;
   private static final float CONE_ANGLE = 120.0F;
   private static final float VEL_MIN = 0.12F;
   private static final float VEL_MAX = 0.3F;
   private static final float A_2 = 60.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit) {
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         for(int i = 1; i <= 1; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 60.0F, facing + 60.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.12F, speed * -0.3F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, 2.0F, 255.0F, 1.0F, BRIGHT_COLOR);
            engine.addHitParticle(point, vector, 6.0F, 255.0F, 0.5F, DIM_COLOR);
         }
      }

   }
}
