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

public class dcp_magellan_TwinACOnHit implements OnHitEffectPlugin {
   private static final float FLUXRAISE_MIN_MULT = 0.5F;
   private static final float FLUXRAISE_MAX_MULT = 0.8333F;
   private static final Color EXPLOSION_COLOR = new Color(140, 190, 210, 255);
   private static final Color NEBULA_COLOR = new Color(140, 190, 210, 155);
   private static final float NEBULA_SIZE = 5.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 25.0F;
   private static final float NEBULA_DUR = 1.25F;
   private static final float NEBULA_RAMPUP = 0.1F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         Vector2f v_target = new Vector2f(target.getVelocity());
         Vector2f v_proj = new Vector2f(projectile.getVelocity());
         Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
         engine.addSwirlyNebulaParticle(point, v_comp, NEBULA_SIZE, 25.0F, 0.1F, 0.2F, 1.25F, NEBULA_COLOR, true);
         engine.spawnExplosion(point, v_comp, EXPLOSION_COLOR, NEBULA_SIZE * 8.0F, 0.75F);
         ShipAPI targetship = (ShipAPI)target;
         float fluxminmult = projectile.getDamageAmount() * 0.5F;
         float fluxmaxmult = projectile.getDamageAmount() * 0.8333F;
         float maxflux = targetship.getMaxFlux();
         if (maxflux > fluxmaxmult * 1.5F) {
            targetship.getFluxTracker().increaseFlux(MathUtils.getRandomNumberInRange(fluxminmult, fluxmaxmult), true);
         }
      }

   }
}
