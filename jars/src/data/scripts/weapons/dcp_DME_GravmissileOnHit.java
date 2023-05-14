package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import java.util.Random;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_GravmissileOnHit implements OnHitEffectPlugin {
   private static final int CRIT_DAMAGE_MIN = 50;
   private static final int CRIT_DAMAGE_MAX = 150;
   private static final Color EXPLOSION_COLOR = new Color(185, 175, 100, 255);
   private static Random rng = new Random();

   private static float damageAmount() {
      return (float)(rng.nextInt(101) + 50);
   }

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit) {
         engine.applyDamage(target, point, damageAmount(), DamageType.HIGH_EXPLOSIVE, 0.0F, false, false, projectile.getSource());
         Vector2f v_target = new Vector2f(target.getVelocity());
         engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, 90.0F, 1.0F);
      }

   }
}
