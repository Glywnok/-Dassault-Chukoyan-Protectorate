package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_CIWS_Sparks implements OnHitEffectPlugin {
   private static final Color BRIGHT_COLOR = new Color(255, 255, 255, 255);
   private static final Color DIM_COLOR = new Color(90, 75, 0, 30);
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.15F;
   private static final float VEL_MAX = 0.25F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      String projid = projectile.getProjectileSpecId();
      byte var13 = -1;
      switch(projid.hashCode()) {
      case -1520227573:
         if (projid.equals("dcp_magellan_lgminigun_shot")) {
            var13 = 2;
         }
         break;
      case 366577168:
         if (projid.equals("dcp_magellan_minigun_shot")) {
            var13 = 0;
         }
         break;
      case 592633595:
         if (projid.equals("dcp_magellan_lgminigun_tracer_shot")) {
            var13 = 3;
         }
         break;
      case 1953891414:
         if (projid.equals("dcp_magellan_minigun_tracer_shot")) {
            var13 = 1;
         }
      }

      float spark_chance;
      byte particle_count;
      float particle_size;
      float particle_dur;
      switch(var13) {
      case 0:
         spark_chance = 0.5F;
         particle_count = 1;
         particle_size = 3.0F;
         particle_dur = 0.6F;
         break;
      case 1:
         spark_chance = 0.5F;
         particle_count = 1;
         particle_size = 3.0F;
         particle_dur = 0.6F;
         break;
      case 2:
         spark_chance = 0.75F;
         particle_count = 1;
         particle_size = 4.0F;
         particle_dur = 0.8F;
         break;
      case 3:
         spark_chance = 0.75F;
         particle_count = 1;
         particle_size = 4.0F;
         particle_dur = 0.8F;
         break;
      default:
         return;
      }

      if (!shieldHit && Math.random() <= (double)spark_chance) {
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         for(int i = 0; i <= particle_count; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.15F, speed * -0.25F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, particle_size, 255.0F, particle_dur, BRIGHT_COLOR);
            engine.addHitParticle(point, vector, particle_size * 4.0F, 255.0F, particle_dur * 0.6F, DIM_COLOR);
         }
      }

   }
}
