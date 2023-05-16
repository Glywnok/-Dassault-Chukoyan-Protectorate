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
import java.util.Random;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PlasmaOnHit implements OnHitEffectPlugin {
   private static final int CRIT_DAMAGE_MIN = 0;
   private static final int CRIT_DAMAGE_MAX = 50;
   private static final float CRIT_CHANCE = 1.0F;
   private static final Color EXPLOSION_COLOR = new Color(155, 100, 255, 255);
   private static final float NEBULA_SIZE = 8.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 12.0F;
   private static final float NEBULA_DUR = 1.0F;
   private static final float NEBULA_RAMPUP = 0.0F;
   private static final String SFX = "dcp_DME_energy_crit";
   private static Random rng = new Random();

   private static float damageAmount() {
      return (float)(rng.nextInt(51) + 0);
   }

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f v_target = new Vector2f(target.getVelocity());
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 1.0D) {
         engine.applyDamage(target, point, damageAmount(), DamageType.FRAGMENTATION, 100.0F, false, false, projectile.getSource());
      }

      engine.addNebulaParticle(point, v_target, NEBULA_SIZE, 12.0F, 0.0F, 0.0F, 1.0F, EXPLOSION_COLOR, true);
      Global.getSoundPlayer().playSound("dcp_DME_energy_crit", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
   }
}
