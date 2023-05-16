package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PlasrocketEMPOnHit implements OnHitEffectPlugin {
   private static final Color EXPLOSION_COLOR = new Color(75, 255, 175, 155);
   private static final float EXPLOSION_RADIUS = 30.0F;
   private static final float EXPLOSION_DURATION = 0.15F;
   private static final float NEBULA_SIZE = 24.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_DUR = 0.75F;
   private static final float NEBULA_RAMPUP = 0.15F;
   private static final String SFX = "dcp_DME_energy_crit";
   private static final float ARC_CHANCE = 0.6F;
   private static final float ARC_RANGE = 100000.0F;
   private static final float ARC_DAMAGE_MULT = 1.0F;
   private static final float ARC_EMP_MULT = 2.0F;
   private static final String ARC_SFX = "tachyon_lance_emp_impact";
   private static final float ARC_WIDTH = 32.0F;
   private static final Color ARC_FRINGE_COLOR = new Color(25, 155, 125, 235);
   private static final Color ARC_CORE_COLOR = new Color(75, 255, 175, 255);

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      float NEBULA_SIZE_MULT;
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.6000000238418579D) {
         float emp = projectile.getEmpAmount() * 2.0F;
         NEBULA_SIZE_MULT = projectile.getDamageAmount() * 1.0F;
         engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.HIGH_EXPLOSIVE, NEBULA_SIZE_MULT, emp, 100000.0F, "tachyon_lance_emp_impact", 32.0F, ARC_FRINGE_COLOR, ARC_CORE_COLOR);
      }

      new Vector2f(target.getVelocity());
      NEBULA_SIZE_MULT = Misc.getHitGlowSize(60.0F, projectile.getDamage().getBaseDamage(), damageResult) / 100.0F;
      engine.addSwirlyNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 5.0F + 3.0F * NEBULA_SIZE_MULT, 0.15F, 0.0F, 0.75F, EXPLOSION_COLOR, true);
      engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, NEBULA_SIZE * 4.0F, 0.1875F);
      Global.getSoundPlayer().playSound("dcp_DME_energy_crit", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
   }
}
