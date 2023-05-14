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

public class dcp_DME_PlasmaDriverOnHit implements OnHitEffectPlugin {
   private static final float CRIT_DAMAGE_MIN_MULT = 0.1F;
   private static final float CRIT_DAMAGE_MAX_MULT = 0.25F;
   private static final float CRIT_CHANCE = 0.3F;
   private static final Color EXPLOSION_COLOR = new Color(155, 100, 255, 255);
   private static final float NEBULA_SIZE = 12.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 20.0F;
   private static final float NEBULA_DUR = 1.8F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final String SFX = "istl_energy_crit";

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      new Vector2f(target.getVelocity());
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.30000001192092896D) {
         float critminmult = projectile.getDamageAmount() * 0.1F;
         float critmaxmult = projectile.getDamageAmount() * 0.25F;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(critminmult, critmaxmult), DamageType.FRAGMENTATION, 0.0F, false, false, projectile.getSource());
      }

      engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 20.0F, 0.1F, 0.2F, 1.8F, EXPLOSION_COLOR, true);
      Global.getSoundPlayer().playSound("istl_energy_crit", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
   }
}
