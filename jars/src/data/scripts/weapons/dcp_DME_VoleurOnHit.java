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

public class dcp_DME_VoleurOnHit implements OnHitEffectPlugin {
   private static final int CRIT_DAMAGE_MIN = 50;
   private static final int CRIT_DAMAGE_MAX = 150;
   private static final float CRIT_CHANCE = 1.0F;
   private static final Color EXPLOSION_COLOR = new Color(85, 115, 225, 200);
   private static final String SFX = "dcp_DME_shockarty_crit";
   private static final Color PARTICLE_COLOR = new Color(115, 145, 255, 255);
   private static final float PARTICLE_SIZE = 2.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 1.0F;
   private static final int PARTICLE_COUNT = 7;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.1F;
   private static final float VEL_MAX = 0.2F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 1.0D) {
         engine.applyDamage(target, point, (float)MathUtils.getRandomNumberInRange(50, 150), DamageType.HIGH_EXPLOSIVE, 0.0F, false, false, projectile.getSource());
      }

      engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, 125.0F, 0.15F);
      float speed = projectile.getVelocity().length();
      float facing = projectile.getFacing();

      for(int i = 0; i <= 7; ++i) {
         float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
         float vel = MathUtils.getRandomNumberInRange(speed * -0.1F, speed * -0.2F);
         Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
         engine.addHitParticle(point, vector, 2.0F, 255.0F, 1.0F, PARTICLE_COLOR);
      }

      Global.getSoundPlayer().playSound("dcp_DME_shockarty_crit", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
   }
}
