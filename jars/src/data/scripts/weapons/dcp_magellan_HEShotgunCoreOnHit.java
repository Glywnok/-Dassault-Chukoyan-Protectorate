package data.scripts.weapons;

import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_HEShotgunCoreOnHit implements OnHitEffectPlugin {
   public static float DAMAGE = 75.0F;
   public static float DAMAGE_MAXRADIUS = 60.0F;
   public static float DAMAGE_MINRADIUS = 20.0F;
   private static final Color DIM_COLOR = new Color(200, 175, 50, 125);
   private static final Color BRIGHT_COLOR = new Color(255, 225, 125, 200);
   private static final float NEBULA_SIZE = 8.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 24.0F;
   private static final float NEBULA_DUR = 0.6F;
   private static final float NEBULA_RAMPUP = 0.15F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f v_target = new Vector2f(target.getVelocity());
      Vector2f v_proj = new Vector2f(projectile.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_target, new Vector2f()).scale(0.03F);
      if (target instanceof ShipAPI) {
         engine.spawnDamagingExplosion(this.createExplosionSpec(), projectile.getSource(), point);
         engine.addNebulaSmokeParticle(point, v_comp, NEBULA_SIZE, 24.0F, 0.15F, 0.3F, 0.6F, DIM_COLOR);
         engine.spawnExplosion(point, v_comp, BRIGHT_COLOR, NEBULA_SIZE * 4.0F, 0.15F);
      }

   }

   public DamagingExplosionSpec createExplosionSpec() {
      DamagingExplosionSpec spec = new DamagingExplosionSpec(0.25F, DAMAGE_MAXRADIUS, DAMAGE_MINRADIUS, DAMAGE, DAMAGE / 2.0F, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 7.0F, 3.0F, 1.0F, 24, BRIGHT_COLOR, DIM_COLOR);
      spec.setDamageType(DamageType.FRAGMENTATION);
      spec.setUseDetailedExplosion(false);
      spec.setSoundSetId("devastator_explosion");
      return spec;
   }
}
