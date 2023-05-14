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

public class dcp_magellan_BonegrinderOnHit implements OnHitEffectPlugin {
   private static final Color EXPLOSION_COLOR = new Color(255, 235, 200, 200);
   private static final String SFX = "magellan_bonesaw_ftr_crit";

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         Vector2f loc_target = new Vector2f(target.getLocation());
         Vector2f v_target = new Vector2f(target.getVelocity());
         engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, 24.0F, 0.2F);
         Global.getSoundPlayer().playSound("magellan_bonesaw_ftr_crit", 1.0F, 1.0F, loc_target, v_target);
      }

   }
}
