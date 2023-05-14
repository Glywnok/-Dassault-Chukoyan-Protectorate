package data.scripts.weapons;

import com.fs.starfarer.api.Global;
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

public class dcp_DME_SmartPlasmidOnHit implements OnHitEffectPlugin {
   public static float DAMAGE = 150.0F;
   public static float DAMAGE_MAXRADIUS = 120.0F;
   public static float DAMAGE_MINRADIUS = 60.0F;
   private static final Color BOOM_COLOR = new Color(135, 75, 255, 155);
   private static final Color NEBULA_COLOR = new Color(155, 100, 255, 255);
   private static final float NEBULA_SIZE = 15.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 30.0F;
   private static final float NEBULA_DUR = 1.8F;
   private static final float NEBULA_RAMPUP = 0.2F;
   private static final String SFX = "istl_mine_explosion_sm";

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      new Vector2f(target.getVelocity());
      if (target instanceof ShipAPI) {
         engine.spawnDamagingExplosion(this.createExplosionSpec(), projectile.getSource(), point);
         engine.addNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 30.0F, 0.2F, 0.3F, 1.8F, NEBULA_COLOR, true);
         Global.getSoundPlayer().playSound("istl_mine_explosion_sm", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
      }

      engine.spawnExplosion(point, target.getVelocity(), NEBULA_COLOR, NEBULA_SIZE * 4.0F, 0.9F);
   }

   public DamagingExplosionSpec createExplosionSpec() {
      DamagingExplosionSpec spec = new DamagingExplosionSpec(0.3F, DAMAGE_MAXRADIUS, DAMAGE_MINRADIUS, DAMAGE, DAMAGE / 2.0F, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 7.0F, 3.0F, 2.4F, 30, NEBULA_COLOR, BOOM_COLOR);
      spec.setDamageType(DamageType.ENERGY);
      spec.setUseDetailedExplosion(false);
      spec.setSoundSetId("devastator_explosion");
      return spec;
   }
}
