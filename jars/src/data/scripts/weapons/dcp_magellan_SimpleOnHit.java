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

public class dcp_magellan_SimpleOnHit implements OnHitEffectPlugin {
   private static final float CRIT_DAMAGE_MIN_MULT = 1.0F;
   private static final float CRIT_DAMAGE_MAX_MULT = 2.0F;
   private static final float CRIT_CHANCE = 0.2F;
   private static final Color EXPLOSION_COLOR = new Color(235, 235, 255, 255);
   private static final float EXPLOSION_SIZE = 30.0F;
   private static final float EXPLOSION_DUR = 0.3F;
   private static final String SFX = "devastator_explosion";

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.20000000298023224D) {
         float critminmult = projectile.getDamageAmount() * 1.0F;
         float critmaxmult = projectile.getDamageAmount() * 2.0F;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(critminmult, critmaxmult), DamageType.FRAGMENTATION, 0.0F, false, false, projectile.getSource());
         Vector2f v_target = new Vector2f(target.getVelocity());
         engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, 30.0F, 0.3F);
         Global.getSoundPlayer().playSound("devastator_explosion", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
      }

   }
}
