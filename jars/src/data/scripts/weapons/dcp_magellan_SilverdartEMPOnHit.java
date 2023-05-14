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

public class dcp_magellan_SilverdartEMPOnHit implements OnHitEffectPlugin {
   private static final Color EXPLOSION_COLOR = new Color(125, 215, 245, 255);
   private static final Color NEBULA_COLOR = new Color(125, 215, 245, 155);
   private static final float NEBULA_SIZE = 5.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 20.0F;
   private static final float NEBULA_DUR = 1.0F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final String SFX = "magellan_electron_crit_sm";
   private static final float ARC_CHANCE = 0.5F;
   private static final float ARC_RANGE = 100000.0F;
   private static final float ARC_DAMAGE_MULT = 0.5F;
   private static final float ARC_EMP_MULT = 2.0F;
   private static final String ARC_SFX = "tachyon_lance_emp_impact";
   private static final float ARC_WIDTH = 20.0F;
   private static final Color ARC_FRINGE_COLOR = new Color(25, 100, 155, 255);
   private static final Color ARC_CORE_COLOR = new Color(255, 255, 255, 255);

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit) {
         if (Math.random() <= 0.5D) {
            float emp = projectile.getEmpAmount() * 2.0F;
            float dam = projectile.getDamageAmount() * 0.5F;
            engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 100000.0F, "tachyon_lance_emp_impact", 20.0F, ARC_FRINGE_COLOR, ARC_CORE_COLOR);
         }

         Vector2f loc_target = new Vector2f(target.getLocation());
         Vector2f v_target = new Vector2f(target.getVelocity());
         Vector2f v_proj = new Vector2f(projectile.getVelocity());
         Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
         engine.addSwirlyNebulaParticle(point, v_comp, NEBULA_SIZE, 20.0F, 0.1F, 0.2F, 1.0F, NEBULA_COLOR, true);
         engine.spawnExplosion(point, v_comp, EXPLOSION_COLOR, NEBULA_SIZE * 7.0F, 0.6F);
         Global.getSoundPlayer().playSound("magellan_electron_crit_sm", 1.0F, 1.0F, loc_target, v_comp);
      }

   }
}
