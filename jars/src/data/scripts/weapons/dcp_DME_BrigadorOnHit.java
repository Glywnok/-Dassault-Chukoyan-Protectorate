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

public class dcp_DME_BrigadorOnHit implements OnHitEffectPlugin {
   public static float DAMAGE = 50.0F;
   private static final float FLUXRAISE_MULT = 0.25F;
   private static final Color EXPLOSION_COLOR = new Color(75, 125, 200, 215);
   private static final Color PARTICLE_COLOR = new Color(75, 125, 200, 255);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 1.2F;
   private static final int PARTICLE_COUNT = 2;
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.06F;
   private static final float VEL_MAX = 0.1F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      engine.spawnExplosion(point, target.getVelocity(), EXPLOSION_COLOR, 45.0F, 0.4F);
      float speed = projectile.getVelocity().length();
      float facing = projectile.getFacing();

      float fluxmult;
      for(int i = 0; i <= 2; ++i) {
         fluxmult = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
         float vel = MathUtils.getRandomNumberInRange(speed * -0.06F, speed * -0.1F);
         Vector2f vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, fluxmult);
         engine.addHitParticle(point, vector, 5.0F, 255.0F, 1.2F, PARTICLE_COLOR);
      }

      if (shieldHit && target instanceof ShipAPI) {
         ShipAPI targetship = (ShipAPI)target;
         fluxmult = projectile.getDamageAmount() * 0.25F;
         targetship.getFluxTracker().increaseFlux(fluxmult, false);
      }

      if (!shieldHit && target instanceof ShipAPI) {
         dealArmorDamage(projectile, (ShipAPI)target, point);
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
