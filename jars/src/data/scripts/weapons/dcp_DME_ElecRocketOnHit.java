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

public class dcp_DME_ElecRocketOnHit implements OnHitEffectPlugin {
   private static final int MIN_ARCS = 2;
   private static final int MAX_ARCS = 3;
   private static final float ARC_DAMAGE = 0.2F;
   private static final float ARC_EMP = 0.25F;
   private static final String SFX = "dcp_DME_kinetic_crit_med";
   private static final Color EXPLOSION_COLOR = new Color(100, 110, 255, 255);
   private static final float EXPLOSION_RADIUS = 150.0F;
   private static final float EXPLOSION_DURATION = 0.1F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit && target instanceof ShipAPI) {
         float dam = projectile.getDamageAmount() * 0.2F;
         float emp = projectile.getEmpAmount() * 0.25F;
         int arcs = MathUtils.getRandomNumberInRange(2, 3);

         for(int i = 0; i < arcs; ++i) {
            engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 100000.0F, "tachyon_lance_emp_impact", 25.0F, new Color(50, 55, 155, 255), new Color(200, 220, 255, 255));
            Vector2f v_target = new Vector2f(target.getVelocity());
            engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, 150.0F, 0.1F);
            Global.getSoundPlayer().playSound("dcp_DME_kinetic_crit_med", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
         }
      }

   }
}
