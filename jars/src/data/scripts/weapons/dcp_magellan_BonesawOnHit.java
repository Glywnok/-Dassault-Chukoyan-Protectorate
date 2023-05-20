package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_BonesawOnHit implements OnHitEffectPlugin {
   private static final Color EXPLOSION_BRIGHT = new Color(255, 235, 200, 200);
   private static final Color EXPLOSION_DIM = new Color(255, 235, 200, 125);

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f loc_target = new Vector2f(target.getLocation());
      Vector2f v_target = new Vector2f(target.getVelocity());
      String projid = projectile.getProjectileSpecId();
      byte var14 = -1;
      switch(projid.hashCode()) {
      case -1183910284:
         if (projid.equals("dcp_magellan_bonesaw_shot")) {
            var14 = 1;
         }
         break;
      case 304188873:
         if (projid.equals("dcp_magellan_boneshaker_shot")) {
            var14 = 2;
         }
         break;
      case 468620143:
         if (projid.equals("dcp_magellan_bonesaw_ftr_shot")) {
            var14 = 0;
         }
      }

      float explosion_size;
      float explosion_dur;
      String hit_sfx;
      switch(var14) {
      case 0:
         explosion_size = 15.0F;
         explosion_dur = 0.12F;
         hit_sfx = "dcp_magellan_bonesaw_ftr_crit";
         break;
      case 1:
         explosion_size = 20.0F;
         explosion_dur = 0.16F;
         hit_sfx = "dcp_magellan_bonesaw_ftr_crit";
         break;
      case 2:
         explosion_size = 30.0F;
         explosion_dur = 0.2F;
         hit_sfx = "dcp_magellan_bonesaw_crit";
         break;
      default:
         return;
      }

      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         engine.addSmoothParticle(point, v_target, explosion_size * 1.5F, 1.0F, 0.3F, explosion_dur / 3.0F, EXPLOSION_BRIGHT);
         engine.spawnExplosion(point, v_target, EXPLOSION_DIM, explosion_size, explosion_dur);
         Global.getSoundPlayer().playSound(hit_sfx, 1.0F, 1.0F, loc_target, v_target);
      }

   }
}
