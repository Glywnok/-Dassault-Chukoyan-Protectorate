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

public class dcp_DME_OmniEMPOnHit implements OnHitEffectPlugin {
   private static final String SFX = "istl_shockarty_crit_sm";
   private static final Color EXPLOSION_COLOR = new Color(125, 100, 255, 155);
   private static final float EXPLOSION_RADIUS = 75.0F;
   private static final float EXPLOSION_DURATION = 0.8F;
   private static final float ARC_CHANCE = 0.6F;
   private static final float ARC_RANGE = 100000.0F;
   private static final float ARC_DAMAGE_MULT = 0.2F;
   private static final float ARC_EMP_MULT = 10.0F;
   private static final String ARC_SFX = "tachyon_lance_emp_impact";
   private static final float ARC_WIDTH = 20.0F;
   private static final Color ARC_FRINGE_COLOR = new Color(85, 60, 205, 225);
   private static final Color ARC_CORE_COLOR = new Color(235, 175, 235, 255);

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.6000000238418579D) {
         float emp = projectile.getEmpAmount() * 10.0F;
         float dam = projectile.getDamageAmount() * 0.2F;
         engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 100000.0F, "tachyon_lance_emp_impact", 20.0F, ARC_FRINGE_COLOR, ARC_CORE_COLOR);
      }

      Vector2f v_target = new Vector2f(target.getVelocity());
      engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, 75.0F, 0.8F);
      Global.getSoundPlayer().playSound("istl_shockarty_crit_sm", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
   }
}
