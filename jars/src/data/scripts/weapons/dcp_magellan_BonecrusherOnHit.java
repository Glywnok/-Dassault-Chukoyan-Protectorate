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

public class dcp_magellan_BonecrusherOnHit implements OnHitEffectPlugin {
   private static final float EXPLOSION_RADIUS = 30.0F;
   private static final float EXPLOSION_DURATION = 0.3F;
   private static final String SFX = "dcp_magellan_bonecrusher_crit";
   private static final float ARC_CHANCE = 0.2F;
   private static final float ARC_RANGE = 100000.0F;
   private static final float ARC_DAMAGE_MULT = 0.2F;
   private static final float ARC_EMP_MULT = 2.0F;
   private static final String ARC_SFX = "tachyon_lance_emp_impact";
   private static final float ARC_WIDTH = 20.0F;
   private static final Color FRINGE_COLOR = new Color(255, 235, 200, 155);
   private static final Color CORE_COLOR = new Color(255, 235, 200, 255);

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.20000000298023224D) {
         float emp = projectile.getEmpAmount() * 2.0F;
         float dam = projectile.getDamageAmount() * 0.2F;
         engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 100000.0F, "tachyon_lance_emp_impact", 20.0F, FRINGE_COLOR, CORE_COLOR);
      }

      Vector2f loc_target = new Vector2f(target.getLocation());
      Vector2f v_target = new Vector2f(target.getVelocity());
      engine.addSmoothParticle(point, v_target, 50.001F, 1.0F, 0.3F, 0.09990001F, CORE_COLOR);
      engine.spawnExplosion(point, v_target, FRINGE_COLOR, 30.0F, 0.3F);
      Global.getSoundPlayer().playSound("dcp_magellan_bonecrusher_crit", 1.0F, 1.0F, loc_target, v_target);
   }
}
