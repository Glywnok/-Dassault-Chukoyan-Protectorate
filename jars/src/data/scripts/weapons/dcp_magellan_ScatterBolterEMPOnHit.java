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
import java.util.Random;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_ScatterBolterEMPOnHit implements OnHitEffectPlugin {
   private static final int EXPLOSION_DAMAGE_MIN = 0;
   private static final int EXPLOSION_DAMAGE_MAX = 0;
   private static final float EXPLOSION_EMP = 0.0F;
   private static final Color EXPLOSION_COLOR = new Color(200, 220, 255, 255);
   private static final float EXPLOSION_RADIUS = 10.0F;
   private static final float EXPLOSION_DURATION = 0.2F;
   private static final String SFX = "magellan_electron_crit_sm";
   private static final float ARC_CHANCE = 0.6F;
   private static final float ARC_RANGE = 20000.0F;
   private static final float ARC_DAMAGE_MULT = 0.5F;
   private static final float ARC_EMP_MULT = 1.0F;
   private static final String ARC_SFX = "tachyon_lance_emp_impact";
   private static final float ARC_WIDTH = 20.0F;
   private static final Color ARC_FRINGE_COLOR = new Color(50, 55, 155, 255);
   private static final Color ARC_CORE_COLOR = new Color(200, 220, 255, 255);
   private static Random rng = new Random();

   private static float explosionDamage() {
      return (float)(rng.nextInt(1) + 0);
   }

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f v_target = new Vector2f(target.getVelocity());
      Vector2f v_proj = new Vector2f(projectile.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
      if (target instanceof ShipAPI && !shieldHit) {
         if (Math.random() <= 0.6000000238418579D) {
            float emp = projectile.getEmpAmount() * 1.0F;
            float dam = projectile.getDamageAmount() * 0.5F;
            engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 20000.0F, "tachyon_lance_emp_impact", 20.0F, ARC_FRINGE_COLOR, ARC_CORE_COLOR);
         }

         engine.applyDamage(target, point, explosionDamage(), DamageType.ENERGY, 0.0F, false, false, projectile.getSource());
      }

      engine.spawnExplosion(point, v_comp, EXPLOSION_COLOR, 10.0F, 0.2F);
      Global.getSoundPlayer().playSound("magellan_electron_crit_sm", 1.0F, 1.0F, target.getLocation(), v_comp);
   }
}
