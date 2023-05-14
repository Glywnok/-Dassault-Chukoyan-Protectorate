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

public class dcp_magellan_SilverdartBaseOnHit implements OnHitEffectPlugin {
   private static final float FLUXRAISE_MIN_MULT = 0.5F;
   private static final float FLUXRAISE_MAX_MULT = 1.5F;
   private static final Color EXPLOSION_COLOR = new Color(125, 215, 245, 155);
   private static final float EXPLOSION_SIZE = 8.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float EXPLOSION_DUR = 0.2F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         Vector2f v_target = new Vector2f(target.getVelocity());
         Vector2f v_proj = new Vector2f(projectile.getVelocity());
         Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
         engine.spawnExplosion(point, v_comp, EXPLOSION_COLOR, EXPLOSION_SIZE, 0.2F);
         ShipAPI targetship = (ShipAPI)target;
         float fluxminmult = projectile.getDamageAmount() * 0.5F;
         float fluxmaxmult = projectile.getDamageAmount() * 1.5F;
         float maxflux = targetship.getMaxFlux();
         if (maxflux > fluxmaxmult * 1.5F) {
            targetship.getFluxTracker().increaseFlux(MathUtils.getRandomNumberInRange(fluxminmult, fluxmaxmult), true);
         }
      }

   }
}
