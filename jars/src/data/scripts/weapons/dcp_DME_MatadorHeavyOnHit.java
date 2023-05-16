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

public class dcp_DME_MatadorHeavyOnHit implements OnHitEffectPlugin {
   private static final float CRIT_DAMAGE_MIN_MULT = 1.5F;
   private static final float CRIT_DAMAGE_MAX_MULT = 2.0F;
   private static final float CRIT_CHANCE = 0.2F;
   private static final float FLUXRAISE_MULT = 0.25F;
   private static final Color NEBULA_COLOR = new Color(75, 125, 200, 215);
   private static final float NEBULA_SIZE = 6.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 15.0F;
   private static final float NEBULA_DUR = 0.8F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final String SFX = "dcp_DME_energy_crit_sm";
   private static final Color PARTICLE_COLOR = new Color(235, 235, 255, 255);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 1.0F;
   private static final int PARTICLE_COUNT = 3;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.08F;
   private static final float VEL_MAX = 0.12F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      float critmaxmult;
      if (shieldHit && target instanceof ShipAPI) {
         ShipAPI targetship = (ShipAPI)target;
         critmaxmult = projectile.getDamageAmount() * 0.25F;
         targetship.getFluxTracker().increaseFlux(critmaxmult, false);
      }

      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.20000000298023224D) {
         float critminmult = projectile.getDamageAmount() * 1.5F;
         critmaxmult = projectile.getDamageAmount() * 2.0F;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(critminmult, critmaxmult), DamageType.ENERGY, 0.0F, false, false, projectile.getSource());
         engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 15.0F, 0.1F, 0.2F, 0.8F, NEBULA_COLOR, true);
         engine.spawnExplosion(point, target.getVelocity(), NEBULA_COLOR, NEBULA_SIZE * 4.0F, 0.4F);
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         for(int i = 0; i <= 3; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
            float vel = MathUtils.getRandomNumberInRange(speed * -0.08F, speed * -0.12F);
            Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
            engine.addHitParticle(point, vector, 5.0F, 255.0F, 1.0F, PARTICLE_COLOR);
         }

         Global.getSoundPlayer().playSound("dcp_DME_energy_crit_sm", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
      }

   }
}
