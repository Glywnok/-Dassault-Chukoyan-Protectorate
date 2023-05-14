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

public class dcp_DME_SlapperEMPOnHit implements OnHitEffectPlugin {
   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if ((float)Math.random() > 0.3F && !shieldHit && target instanceof ShipAPI) {
         float emp = projectile.getEmpAmount() * 2.0F;
         float dam = projectile.getDamageAmount() * 0.25F;
         engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 1000.0F, "tachyon_lance_emp_impact", 20.0F, new Color(165, 165, 150, 225), new Color(255, 255, 255, 255));
      }

   }
}
