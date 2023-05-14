package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.impl.combat.DisintegratorEffect;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_StunfireOnHit implements OnHitEffectPlugin {
   public static float explosion_damage;
   public static float explosion_size;
   private static final Color NEBULA_COLOR = new Color(100, 110, 255, 255);
   private static final float NEBULA_SIZE_MULT = 20.0F;
   private static final float NEBULA_DUR = 1.2F;
   private static final float NEBULA_RAMPUP = 0.2F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         dealArmorDamage(projectile, (ShipAPI)target, point);
         Vector2f v_target = new Vector2f(target.getVelocity());
         Vector2f v_proj = new Vector2f(projectile.getVelocity());
         Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
         float selector = 1.0F * (float)Math.random();
         if (selector < 0.2F) {
            explosion_damage = 50.0F;
            explosion_size = 5.0F * (0.75F + (float)Math.random() * 0.5F);
         } else if (selector >= 0.2F && selector < 0.8F) {
            explosion_damage = 100.0F;
            explosion_size = 9.0F * (0.75F + (float)Math.random() * 0.5F);
         } else if (selector >= 0.8F) {
            explosion_damage = 150.0F;
            explosion_size = 13.0F * (0.75F + (float)Math.random() * 0.5F);
         }

         engine.addHitParticle(point, v_target, explosion_size * 6.0F, 1.0F, 0.4F, NEBULA_COLOR);
         engine.addSwirlyNebulaParticle(point, v_comp, explosion_size, 20.0F, 0.2F, 0.3F, 1.2F, NEBULA_COLOR, true);
      }

   }

   public static void dealArmorDamage(DamagingProjectileAPI projectile, ShipAPI target, Vector2f point) {
      CombatEngineAPI engine = Global.getCombatEngine();
      ArmorGridAPI grid = target.getArmorGrid();
      int[] cell = grid.getCellAtLocation(point);
      if (cell != null) {
         int gridWidth = grid.getGrid().length;
         int gridHeight = grid.getGrid()[0].length;
         float damageTypeMult = DisintegratorEffect.getDamageTypeMult(projectile.getSource(), target);
         float damageDealt = 0.0F;

         for(int i = -2; i <= 2; ++i) {
            for(int j = -2; j <= 2; ++j) {
               if (i != 2 && i != -2 || j != 2 && j != -2) {
                  int cx = cell[0] + i;
                  int cy = cell[1] + j;
                  if (cx >= 0 && cx < gridWidth && cy >= 0 && cy < gridHeight) {
                     float damMult = 0.033333335F;
                     if (i == 0 && j == 0) {
                        damMult = 0.06666667F;
                     } else if (i <= 1 && i >= -1 && j <= 1 && j >= -1) {
                        damMult = 0.06666667F;
                     } else {
                        damMult = 0.033333335F;
                     }

                     float armorInCell = grid.getArmorValue(cx, cy);
                     float damage = explosion_damage * damMult * damageTypeMult;
                     damage = Math.min(damage, armorInCell);
                     if (!(damage <= 0.0F)) {
                        target.getArmorGrid().setArmorValue(cx, cy, Math.max(0.0F, armorInCell - damage));
                        damageDealt += damage;
                     }
                  }
               }
            }
         }

         if (damageDealt > 0.0F) {
            if (Misc.shouldShowDamageFloaty(projectile.getSource(), target)) {
               engine.addFloatingDamageText(point, damageDealt, Misc.FLOATY_ARMOR_DAMAGE_COLOR, target, projectile.getSource());
            }

            target.syncWithArmorGridState();
         }

      }
   }
}
