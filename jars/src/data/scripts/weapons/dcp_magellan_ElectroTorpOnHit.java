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

public class dcp_magellan_ElectroTorpOnHit implements OnHitEffectPlugin {
   private static final float ARC_DAMAGE_MULT = 0.2F;
   private static final float ARC_EMP_MULT = 0.5F;
   private static final Color EXPLOSION_BRIGHT = new Color(100, 110, 255, 255);
   private static final Color EXPLOSION_DIM = new Color(100, 110, 255, 155);
   private static final Color ARC_CORE = new Color(200, 220, 255, 255);

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         Vector2f loc_target = new Vector2f(target.getLocation());
         Vector2f v_target = new Vector2f(target.getVelocity());
         Vector2f v_proj = new Vector2f(projectile.getVelocity());
         Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
         String projid = projectile.getProjectileSpecId();
         byte var18 = -1;
         switch(projid.hashCode()) {
         case -455203911:
            if (projid.equals("magellan_electrontorp_ftr_shot")) {
               var18 = 0;
            }
            break;
         case 225852350:
            if (projid.equals("magellan_electrontorp_shot")) {
               var18 = 1;
            }
         }

         byte min_arcs;
         byte max_arcs;
         float explosion_size;
         float explosion_dur;
         String hit_sfx;
         switch(var18) {
         case 0:
            min_arcs = 2;
            max_arcs = 3;
            explosion_size = 50.0F;
            explosion_dur = 0.2F;
            hit_sfx = "magellan_electron_crit_sm";
            break;
         case 1:
            min_arcs = 3;
            max_arcs = 5;
            explosion_size = 75.0F;
            explosion_dur = 0.3F;
            hit_sfx = "magellan_electron_crit";
            break;
         default:
            return;
         }

         float dam = projectile.getDamageAmount() * 0.2F;
         float emp = projectile.getEmpAmount() * 0.5F;
         int arcs = MathUtils.getRandomNumberInRange(min_arcs, max_arcs);

         for(int i = 0; i < arcs; ++i) {
            engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 100000.0F, "tachyon_lance_emp_impact", 25.0F, EXPLOSION_DIM, ARC_CORE);
            engine.addSmoothParticle(point, v_comp, explosion_size * 2.0F, 1.0F, 0.3F, explosion_dur / 2.0F, EXPLOSION_BRIGHT);
            engine.spawnExplosion(point, v_comp, EXPLOSION_DIM, explosion_size, explosion_dur);
            Global.getSoundPlayer().playSound(hit_sfx, 1.0F, 1.0F, loc_target, v_comp);
         }
      }

   }
}
