package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_MarteauEMPOnHit implements OnHitEffectPlugin {
   private static final Color EXPLOSION_COLOR = new Color(165, 165, 150, 225);
   private static final Color NEBULA_COLOR = new Color(100, 100, 100, 200);
   private static final float NEBULA_SIZE = 7.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 35.0F;
   private static final float NEBULA_DUR = 1.5F;
   private static final float NEBULA_RAMPUP = 0.3F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if ((float)Math.random() > 0.3F && !shieldHit && target instanceof ShipAPI) {
         float emp = projectile.getEmpAmount() * 2.0F;
         float dam = projectile.getDamageAmount() * 0.25F;
         engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 1000.0F, "tachyon_lance_emp_impact", 20.0F, EXPLOSION_COLOR, new Color(255, 255, 255, 255));
         engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 35.0F, 0.3F, 0.3F, 1.5F, NEBULA_COLOR);
         engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, NEBULA_SIZE * 3.0F, 9.0F);
      }

   }
}
