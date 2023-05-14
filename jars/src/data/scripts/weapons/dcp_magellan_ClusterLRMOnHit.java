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

public class dcp_magellan_ClusterLRMOnHit implements OnHitEffectPlugin {
   public static float DAMAGE = 150.0F;
   private static final Color EXPLOSION_COLOR = new Color(255, 205, 155, 255);
   private static final Color NEBULA_COLOR = new Color(100, 100, 100, 200);
   private static final float NEBULA_SIZE = 15.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 20.0F;
   private static final float NEBULA_DUR = 1.5F;
   private static final float NEBULA_RAMPUP = 0.3F;
   private static final String SFX = "magellan_bonecrusher_crit";

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         dealArmorDamage(projectile, (ShipAPI)target, point);
         Vector2f loc_target = new Vector2f(target.getLocation());
         Vector2f v_target = new Vector2f(target.getVelocity());
         Vector2f v_proj = new Vector2f(projectile.getVelocity());
         Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.1F);
         engine.addNebulaParticle(point, v_comp, NEBULA_SIZE, 20.0F, 0.3F, 0.3F, 1.5F, NEBULA_COLOR);
         engine.spawnExplosion(point, v_comp, EXPLOSION_COLOR, NEBULA_SIZE * 6.0F, 0.15F);
         Global.getSoundPlayer().playSound("magellan_bonecrusher_crit", 1.0F, 1.0F, loc_target, v_comp);
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
                     float damage = DAMAGE * damMult * damageTypeMult;
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
