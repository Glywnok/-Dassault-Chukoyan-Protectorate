package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_BlazeGunOnHit implements OnHitEffectPlugin {
   private static final Color EXPLOSION_COLOR = new Color(255, 120, 75, 255);

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit) {
         Vector2f v_target = new Vector2f(target.getVelocity());
         String projid = projectile.getProjectileSpecId();
         byte var12 = -1;
         switch(projid.hashCode()) {
         case 966761876:
            if (projid.equals("istl_blaze_shot")) {
               var12 = 0;
            }
            break;
         case 1921300972:
            if (projid.equals("istl_blaze_medshot")) {
               var12 = 1;
            }
         }

         float size;
         float dur;
         switch(var12) {
         case 0:
            size = 15.0F;
            dur = 0.3F;
            break;
         case 1:
            size = 30.0F;
            dur = 0.6F;
            break;
         default:
            return;
         }

         engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, size, dur);
         engine.addHitParticle(point, v_target, size * 2.0F, 1.0F, dur / 3.0F, EXPLOSION_COLOR);
      }

   }
}
