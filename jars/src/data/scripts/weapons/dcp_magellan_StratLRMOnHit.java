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
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_StratLRMOnHit implements OnHitEffectPlugin {
   private static final Color DIM_COLOR = new Color(200, 165, 50, 75);
   private static final Color BRIGHT_COLOR = new Color(255, 225, 125, 155);
   private static final Color SMOKE_COLOR = new Color(75, 75, 75, 155);
   private static final float NEBULA_RAMPUP = 0.15F;
   private static final String SFX_MED = "dcp_magellan_mine_explosion_vsm";
   private static final String SFX_LG = "dcp_magellan_mine_explosion_sm";
   private float fx_dur;
   private float extra_armor_damage;
   private String projectile_sfx;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      float projdamage = projectile.getDamageAmount();
      String projid = projectile.getProjectileSpecId();
      byte var14 = -1;
      switch(projid.hashCode()) {
      case -1194117231:
         if (projid.equals("dcp_magellan_stratLRM_cruise")) {
            var14 = 2;
         }
         break;
      case -993775357:
         if (projid.equals("dcp_magellan_stratLRM")) {
            var14 = 1;
         }
         break;
      case 277153428:
         if (projid.equals("dcp_magellan_slamfiretorp")) {
            var14 = 0;
         }
      }

      float nebula_size;
      float nebula_size_mult;
      float smoke_radius;
      byte smoke_count;
      switch(var14) {
      case 0:
         nebula_size = 15.0F * (0.75F + (float)Math.random() * 0.5F);
         nebula_size_mult = 25.0F;
         smoke_count = 3;
         smoke_radius = 60.0F;
         this.fx_dur = 2.0F;
         this.extra_armor_damage = 0.25F * projdamage;
         this.projectile_sfx = "dcp_magellan_mine_explosion_vsm";
         break;
      case 1:
         nebula_size = 25.0F * (0.75F + (float)Math.random() * 0.5F);
         nebula_size_mult = 20.0F;
         smoke_count = 6;
         smoke_radius = 100.0F;
         this.fx_dur = 3.0F;
         this.extra_armor_damage = 0.0F;
         this.projectile_sfx = "dcp_magellan_mine_explosion_vsm";
         break;
      case 2:
         nebula_size = 40.0F * (0.75F + (float)Math.random() * 0.5F);
         nebula_size_mult = 20.0F;
         smoke_count = 8;
         smoke_radius = 120.0F;
         this.fx_dur = 5.0F;
         this.extra_armor_damage = 0.25F * projdamage;
         this.projectile_sfx = "dcp_magellan_mine_explosion_sm";
         break;
      default:
         return;
      }

      Vector2f loc_target = new Vector2f(target.getLocation());
      Vector2f v_target = new Vector2f(target.getVelocity());
      Vector2f v_proj = new Vector2f(projectile.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.03F);
      float nebula_half = nebula_size / 2.0F;
      float nebula_quarter = nebula_size / 4.0F;
      float nebula_x_2 = nebula_size * 2.0F;
      float nebula_x_4 = nebula_size * 4.0F;
      float nebula_x_8 = nebula_size * 8.0F;
      float fx_1pt5 = 1.0F;
      float fx_5th = this.fx_dur / 5.0F;
      float fx_10th = this.fx_dur / 10.0F;
      int i;
      Vector2f random_point;
      if (!shieldHit && !projectile.isFading() && target instanceof ShipAPI) {
         if (this.extra_armor_damage != 0.0F) {
            this.dealArmorDamage(projectile, (ShipAPI)target, point);
         }

         engine.addNebulaSmokeParticle(point, v_comp, nebula_size, nebula_size_mult, 0.15F, 0.3F, this.fx_dur, SMOKE_COLOR);
         engine.addNebulaSmokeParticle(point, v_comp, nebula_size, nebula_size_mult, 0.15F, 0.6F, this.fx_dur, SMOKE_COLOR);
         engine.addSwirlyNebulaParticle(point, v_comp, nebula_half, nebula_size_mult, 0.15F, 0.8F, this.fx_dur / 2.5F, DIM_COLOR, true);
         engine.spawnExplosion(point, v_target, BRIGHT_COLOR, nebula_x_8, fx_5th);
         Global.getSoundPlayer().playSound(this.projectile_sfx, 1.0F, 1.0F, loc_target, v_target);

         for(i = 0; i <= smoke_count - 1; ++i) {
            random_point = new Vector2f(MathUtils.getRandomPointInCircle(point, smoke_radius));
            engine.spawnExplosion(random_point, v_target, DIM_COLOR, nebula_x_2, fx_5th);
            engine.addNebulaSmokeParticle(random_point, v_comp, nebula_quarter, nebula_size_mult, 0.15F, 0.3F, this.fx_dur, SMOKE_COLOR);
         }
      } else {
         engine.addNebulaSmokeParticle(point, v_comp, nebula_half, nebula_size_mult, 0.15F, 0.3F, 1.0F, SMOKE_COLOR);
         engine.addNebulaSmokeParticle(point, v_comp, nebula_half, nebula_size_mult, 0.15F, 0.6F, 1.0F, SMOKE_COLOR);
         engine.addSwirlyNebulaParticle(point, v_comp, nebula_quarter, nebula_size_mult, 0.15F, 0.8F, fx_5th, DIM_COLOR, true);
         engine.spawnExplosion(point, v_target, BRIGHT_COLOR, nebula_x_4, fx_10th);
         Global.getSoundPlayer().playSound(this.projectile_sfx, 1.0F, 1.0F, loc_target, v_target);

         for(i = 0; i <= smoke_count - 3; ++i) {
            random_point = new Vector2f(MathUtils.getRandomPointInCircle(point, smoke_radius / 1.5F));
            engine.spawnExplosion(random_point, v_target, DIM_COLOR, nebula_x_2, fx_10th);
            engine.addNebulaSmokeParticle(random_point, v_target, nebula_quarter, nebula_size_mult, 0.15F, 0.3F, 1.0F, SMOKE_COLOR);
         }
      }

   }

   public void dealArmorDamage(DamagingProjectileAPI projectile, ShipAPI target, Vector2f point) {
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
                     float damage = this.extra_armor_damage * damMult * damageTypeMult;
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
