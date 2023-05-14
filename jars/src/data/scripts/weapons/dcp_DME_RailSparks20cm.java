package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_RailSparks20cm implements OnHitEffectPlugin {
   private static final Color BRIGHT_COLOR = new Color(155, 225, 255, 255);
   private static final Color DIM_COLOR = new Color(0, 50, 100, 25);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 3.2F;
   private static final int PARTICLE_COUNT = 9;
   private static final String SFX = "istl_rail_crit_20cm";
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.05F;
   private static final float VEL_MAX = 0.1F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit) {
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         for(int i = 1; i <= 9; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.05F, speed * -0.1F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, 5.0F, 255.0F, 3.2F, BRIGHT_COLOR);
            engine.addHitParticle(point, vector, 20.0F, 255.0F, 1.6F, DIM_COLOR);
            Global.getSoundPlayer().playSound("istl_rail_crit_20cm", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
         }
      }

   }
}
