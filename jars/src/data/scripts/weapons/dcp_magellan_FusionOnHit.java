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

public class dcp_magellan_FusionOnHit implements OnHitEffectPlugin {
   private static final Color EXPLOSION_BRIGHT = new Color(240, 30, 90, 200);
   private static final Color EXPLOSION_DIM = new Color(240, 30, 90, 155);

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f loc_target = new Vector2f(target.getLocation());
      Vector2f v_target = new Vector2f(target.getVelocity());
      Vector2f v_proj = new Vector2f(projectile.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
      String projid = projectile.getProjectileSpecId();
      byte var21 = -1;
      switch(projid.hashCode()) {
      case -1740691591:
         if (projid.equals("dcp_magellan_balefiresmall")) {
            var21 = 2;
         }
         break;
      case -407949746:
         if (projid.equals("dcp_magellan_balefire")) {
            var21 = 4;
         }
         break;
      case -103560718:
         if (projid.equals("dcp_magellan_scatterblaster_shot")) {
            var21 = 3;
         }
         break;
      case 176808081:
         if (projid.equals("dcp_magellan_trinitycannon_shot")) {
            var21 = 1;
         }
         break;
      case 756148494:
         if (projid.equals("dcp_magellan_fusbomb_ftr_shot")) {
            var21 = 0;
         }
      }

      float explosion_size;
      float explosion_dur;
      float nebula_size_mult;
      float damagemin_mult;
      float damagemax_mult;
      float boom_radius;
      byte boom_count;
      String hit_sfx;
      switch(var21) {
      case 0:
         explosion_size = 75.0F;
         explosion_dur = 0.4F;
         nebula_size_mult = 20.0F;
         damagemin_mult = 0.2F;
         damagemax_mult = 0.6F;
         boom_radius = 45.0F;
         boom_count = 3;
         hit_sfx = "dcp_magellan_fusion_sm_crit";
         break;
      case 1:
         explosion_size = 120.0F;
         explosion_dur = 0.5F;
         nebula_size_mult = 20.0F;
         damagemin_mult = 0.25F;
         damagemax_mult = 0.5F;
         boom_radius = 60.0F;
         boom_count = 0;
         hit_sfx = "dcp_magellan_fusion_sm_crit";
         break;
      case 2:
         explosion_size = 120.0F;
         explosion_dur = 0.5F;
         nebula_size_mult = 20.0F;
         damagemin_mult = 0.25F;
         damagemax_mult = 0.5F;
         boom_radius = 60.0F;
         boom_count = 5;
         hit_sfx = "dcp_magellan_fusion_crit";
         break;
      case 3:
         explosion_size = 150.0F;
         explosion_dur = 0.6F;
         nebula_size_mult = 20.0F;
         damagemin_mult = 0.25F;
         damagemax_mult = 0.5F;
         boom_radius = 90.0F;
         boom_count = 0;
         hit_sfx = "dcp_magellan_fusion_sm_crit";
         break;
      case 4:
         explosion_size = 150.0F;
         explosion_dur = 0.6F;
         nebula_size_mult = 20.0F;
         damagemin_mult = 0.25F;
         damagemax_mult = 0.5F;
         boom_radius = 90.0F;
         boom_count = 7;
         hit_sfx = "dcp_magellan_fusion_crit";
         break;
      default:
         return;
      }

      float explosion_half = explosion_size / 2.0F;
      float nebula_size = explosion_size / 10.0F * (0.75F + (float)Math.random() * 0.5F);
      float nebula_half = explosion_size / 15.0F * (0.75F + (float)Math.random() * 0.5F);
      float nebula_dur = explosion_dur * 2.0F;
      float nebula_rampup = 0.3F;
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         float critminmult = projectile.getDamageAmount() * damagemin_mult;
         float critmaxmult = projectile.getDamageAmount() * damagemax_mult;
         engine.applyDamage(target, point, MathUtils.getRandomNumberInRange(critminmult, critmaxmult), DamageType.ENERGY, 0.0F, false, false, projectile.getSource());
      }

      engine.addSmoothParticle(point, v_comp, explosion_size * 2.0F, 1.0F, 0.3F, explosion_dur / 3.0F, EXPLOSION_BRIGHT);
      engine.spawnExplosion(point, v_comp, EXPLOSION_DIM, explosion_size, explosion_dur);
      engine.addNebulaParticle(point, v_comp, nebula_size, nebula_size_mult, 0.3F, 0.3F, nebula_dur, EXPLOSION_BRIGHT, true);

      for(int i = 0; i <= boom_count - 1; ++i) {
         Vector2f random_point = new Vector2f(MathUtils.getRandomPointInCircle(point, boom_radius));
         engine.spawnExplosion(random_point, v_comp, EXPLOSION_DIM, explosion_half, explosion_dur / 1.5F);
         engine.addNebulaParticle(random_point, v_comp, nebula_half, nebula_size_mult, 0.3F, 0.5F, nebula_dur / 1.5F, EXPLOSION_BRIGHT, true);
      }

      Global.getSoundPlayer().playSound(hit_sfx, 1.0F, 1.0F, loc_target, v_comp);
   }
}
