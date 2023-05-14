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

public class dcp_DME_ShockArtyOnHit implements OnHitEffectPlugin {
   private static final Color BOOM_COLOR = new Color(75, 100, 255, 200);
   private static final Color NEBULA_COLOR = new Color(140, 125, 255, 255);
   private static final float NEBULA_SIZE = 15.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 25.0F;
   private static final float NEBULA_DUR = 1.2F;
   private static final float NEBULA_RAMPUP = 0.15F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      new Vector2f(target.getVelocity());
      if (target instanceof ShipAPI) {
         engine.spawnDamagingExplosion(this.createExplosionSpec(), projectile.getSource(), point);
         engine.addSwirlyNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 25.0F, 0.15F, 0.2F, 1.2F, NEBULA_COLOR, true);
         engine.spawnExplosion(point, target.getVelocity(), NEBULA_COLOR, NEBULA_SIZE * 3.0F, 0.4F);
      }

   }

   public DamagingExplosionSpec createExplosionSpec() {
      float damage = 100.0F;
      DamagingExplosionSpec spec = new DamagingExplosionSpec(0.3F, 120.0F, 40.0F, damage, damage / 3.0F, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 5.0F, 2.0F, 1.0F, 48, NEBULA_COLOR, BOOM_COLOR);
      spec.setDamageType(DamageType.ENERGY);
      spec.setUseDetailedExplosion(false);
      spec.setSoundSetId("istl_shockarty_crit");
      return spec;
   }
}
