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

public class dcp_magellan_EBolterEMPOnHit implements OnHitEffectPlugin {
   private static final float ARC_CHANCE = 0.3F;
   private static final float ARC_DAMAGE_MULT = 1.0F;
   private static final float ARC_EMP_MULT = 2.0F;
   private static final Color EXPLOSION_BRIGHT = new Color(100, 110, 255, 255);
   private static final Color EXPLOSION_DIM = new Color(100, 110, 255, 155);
   private static final Color ARC_CORE = new Color(200, 220, 255, 255);

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f loc_target = new Vector2f(target.getLocation());
      Vector2f v_target = new Vector2f(target.getVelocity());
      Vector2f v_proj = new Vector2f(projectile.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
      String projid = projectile.getProjectileSpecId();
      byte var16 = -1;
      switch(projid.hashCode()) {
      case 768080301:
         if (projid.equals("dcp_magellan_ebolter_ftr_shot")) {
            var16 = 0;
         }
         break;
      case 1979788210:
         if (projid.equals("dcp_magellan_ebolter_shot")) {
            var16 = 1;
         }
      }

      float explosion_size;
      float explosion_dur;
      String hit_sfx;
      switch(var16) {
      case 0:
         explosion_size = 8.0F;
         explosion_dur = 0.2F;
         hit_sfx = "dcp_magellan_ebolter_crit";
         break;
      case 1:
         explosion_size = 10.0F;
         explosion_dur = 0.3F;
         hit_sfx = "dcp_magellan_ebolter_crit";
         break;
      default:
         return;
      }

      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.30000001192092896D) {
         float emp = projectile.getEmpAmount() * 2.0F;
         float dam = projectile.getDamageAmount() * 1.0F;
         engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 100000.0F, "tachyon_lance_emp_impact", 25.0F, EXPLOSION_DIM, ARC_CORE);
      }

      engine.addSmoothParticle(point, v_comp, explosion_size * 2.0F, 1.0F, 0.3F, explosion_dur / 3.0F, EXPLOSION_BRIGHT);
      engine.spawnExplosion(point, v_comp, EXPLOSION_DIM, explosion_size, explosion_dur);
      Global.getSoundPlayer().playSound(hit_sfx, 1.0F, 1.0F, loc_target, v_comp);
   }
}
