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

public class dcp_DME_FrappeurOnHit implements OnHitEffectPlugin {
   private static final int DAMAGE = 350;
   public static float DAMAGE_MAXRADIUS = 75.0F;
   public static float DAMAGE_MINRADIUS = 25.0F;
   private static final Color EXPLOSION_COLOR = new Color(125, 155, 255, 200);
   private static final Color NEBULA_COLOR = new Color(75, 105, 255, 255);
   private static final float NEBULA_SIZE = 12.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 30.0F;
   private static final float NEBULA_DUR = 1.5F;
   private static final float NEBULA_RAMPUP = 0.2F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      new Vector2f(target.getVelocity());
      if (target instanceof ShipAPI) {
         engine.spawnDamagingExplosion(this.createExplosionSpec(), projectile.getSource(), point);
         engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 30.0F, 0.2F, 0.3F, 1.5F, NEBULA_COLOR, true);
      }

      engine.spawnExplosion(point, target.getVelocity(), NEBULA_COLOR, NEBULA_SIZE * 4.0F, 0.75F);
   }

   public DamagingExplosionSpec createExplosionSpec() {
      DamagingExplosionSpec spec = new DamagingExplosionSpec(0.3F, DAMAGE_MAXRADIUS, DAMAGE_MINRADIUS, 350.0F, 175.0F, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 7.0F, 3.0F, 3.0F, 12, NEBULA_COLOR, EXPLOSION_COLOR);
      spec.setDamageType(DamageType.FRAGMENTATION);
      spec.setUseDetailedExplosion(false);
      spec.setSoundSetId("istl_mine_explosion_sm");
      return spec;
   }
}
