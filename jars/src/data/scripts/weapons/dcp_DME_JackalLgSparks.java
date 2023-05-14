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
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_JackalLgSparks implements OnHitEffectPlugin {
   private static final float BOOM_CHANCE = 0.5F;
   private static final Color BOOM_COLOR = new Color(255, 155, 75, 90);
   public static float DAMAGE = 100.0F;
   public static float DAMAGE_MAXRADIUS = 60.0F;
   public static float DAMAGE_MINRADIUS = 20.0F;
   private static final Color NEBULA_COLOR = new Color(255, 155, 75, 175);
   private static final float NEBULA_SIZE = 7.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 15.0F;
   private static final float NEBULA_DUR = 0.6F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final float SPARK_CHANCE = 0.2F;
   private static final Color PARTICLE_COLOR = new Color(255, 155, 75, 225);
   private static final float PARTICLE_SIZE = 5.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float PARTICLE_DURATION = 2.0F;
   private static final int PARTICLE_COUNT = 3;
   private static final String SFX = "istl_jackal_crit";
   private static final float CONE_ANGLE = 150.0F;
   private static final float VEL_MIN = 0.1F;
   private static final float VEL_MAX = 0.2F;
   private static final float A_2 = 75.0F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (target instanceof ShipAPI && !shieldHit && Math.random() <= 0.5D) {
         engine.spawnDamagingExplosion(this.createExplosionSpec(), projectile.getSource(), point);
         engine.addSwirlyNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 15.0F, 0.1F, 0.2F, 0.6F, NEBULA_COLOR, true);
         engine.spawnExplosion(point, target.getVelocity(), NEBULA_COLOR, NEBULA_SIZE * 4.0F, 0.3F);
      }

      if (target instanceof ShipAPI && shieldHit && Math.random() <= 0.20000000298023224D) {
         float speed = projectile.getVelocity().length();
         float facing = projectile.getFacing();

         float newangle;
         Vector2f spawn;
         for(int i = 0; i <= 3; ++i) {
            float angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
            newangle = MathUtils.getRandomNumberInRange(speed * -0.1F, speed * -0.2F);
            spawn = MathUtils.getPointOnCircumference((Vector2f)null, newangle, angle);
            engine.addHitParticle(point, spawn, 5.0F, 255.0F, 2.0F, PARTICLE_COLOR);
         }

         float angle = VectorUtils.getAngle(target.getLocation(), projectile.getLocation());

         for(int i = 0; i < 1; ++i) {
            newangle = angle - 30.0F + (float)(Math.random() * 60.0D);
            spawn = MathUtils.getPoint(projectile.getLocation(), 30.0F, newangle);
            engine.spawnProjectile(projectile.getSource(), projectile.getWeapon(), "istl_twinjack_sub", spawn, newangle, new Vector2f());
         }

         Global.getSoundPlayer().playSound("istl_jackal_crit", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
      }

   }

   public DamagingExplosionSpec createExplosionSpec() {
      DamagingExplosionSpec spec = new DamagingExplosionSpec(0.25F, DAMAGE_MAXRADIUS, DAMAGE_MINRADIUS, DAMAGE, DAMAGE / 2.0F, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 7.0F, 3.0F, 1.0F, 24, NEBULA_COLOR, BOOM_COLOR);
      spec.setDamageType(DamageType.FRAGMENTATION);
      spec.setUseDetailedExplosion(false);
      spec.setSoundSetId("istl_ballistic_crit");
      return spec;
   }
}
