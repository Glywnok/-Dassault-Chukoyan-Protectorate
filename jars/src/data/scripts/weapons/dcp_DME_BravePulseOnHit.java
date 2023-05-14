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

public class dcp_DME_BravePulseOnHit implements OnHitEffectPlugin {
   private static final float CRIT_DAMAGE_MAX_MULT = 0.5F;
   private static final float CRIT_CHANCE = 1.0F;
   private static final Color EXPLOSION_COLOR = new Color(50, 255, 175, 255);
   private static final float NEBULA_SIZE = 7.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 13.0F;
   private static final float NEBULA_DUR = 0.5F;
   private static final float NEBULA_RAMPUP = 0.1F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 1.0D) {
         float critmult = projectile.getDamageAmount() * 0.5F;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(0.0F, critmult), DamageType.FRAGMENTATION, 0.0F, false, false, projectile.getSource());
      }

      engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 13.0F, 0.1F, 0.2F, 0.5F, EXPLOSION_COLOR, true);
   }
}
