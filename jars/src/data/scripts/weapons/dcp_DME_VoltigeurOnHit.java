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
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_VoltigeurOnHit implements OnHitEffectPlugin {
   private static final int EXTRA_DAMAGE = 50;
   private static final Color EXPLOSION_COLOR = new Color(125, 175, 255, 255);
   private static final String SFX = "istl_energy_crit";

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f v_target = new Vector2f(target.getVelocity());
      if (target instanceof ShipAPI && !shieldHit) {
         engine.applyDamage(target, point, 50.0F, DamageType.ENERGY, 0.0F, false, false, projectile.getSource());
      }

      engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, 75.0F, 0.2F);
      Global.getSoundPlayer().playSound("istl_energy_crit", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
   }
}
