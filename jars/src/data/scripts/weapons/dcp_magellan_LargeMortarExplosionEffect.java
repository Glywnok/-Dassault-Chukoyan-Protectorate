package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_LargeMortarExplosionEffect implements ProximityExplosionEffect {
   private static final float FX_DURATION = 5.0F;
   private static final float NEBULA_RAMPUP = 0.15F;
   private static final Color BURST_COLOR = new Color(210, 170, 60, 155);
   private static final Color SMOKE_COLOR = new Color(75, 75, 75, 155);
   private static final float NEBULA_SIZE = 50.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 20.0F;
   private static final float SMOKE_RADIUS = 75.0F;
   private static final int SMOKE_COUNT = 5;
   private static final Color PARTICLE_COLOR = new Color(210, 170, 60, 255);
   private static final Color GLOW_COLOR = new Color(90, 75, 0, 45);
   private static final int PARTICLE_COUNT = 12;
   private static final float PARTICLE_SIZE = 1.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float VELMINMULT = 0.03F;
   private static final float VELMAXMULT = 0.3F;

   public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
      CombatEngineAPI engine = Global.getCombatEngine();
      Vector2f v_proj = new Vector2f(originalProjectile.getVelocity());
      Vector2f loc_boom = new Vector2f(explosion.getLocation());
      Vector2f v_boom = new Vector2f(explosion.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_boom, new Vector2f()).scale(0.1F);
      engine.spawnExplosion(loc_boom, v_comp, BURST_COLOR, NEBULA_SIZE * 5.0F, 1.6666666F);
      engine.addSmoothParticle(loc_boom, v_comp, 150.0F, 255.0F, 0.5F, Color.white);
      engine.addHitParticle(loc_boom, v_comp, 200.0F, 255.0F, 1.6666666F, BURST_COLOR);
      engine.addNebulaSmokeParticle(loc_boom, v_comp, NEBULA_SIZE, 20.0F, 0.15F, 0.3F, 5.0F, SMOKE_COLOR);
      engine.addNebulaSmokeParticle(loc_boom, v_comp, NEBULA_SIZE, 20.0F, 0.15F, 0.6F, 5.0F, SMOKE_COLOR);

      for(int i = 0; i <= 4; ++i) {
         Vector2f random_point = new Vector2f(MathUtils.getRandomPointInCircle(loc_boom, 75.0F));
         engine.spawnExplosion(random_point, v_comp, BURST_COLOR, NEBULA_SIZE * 1.5F, 1.6666666F);
         engine.addNebulaSmokeParticle(random_point, v_comp, NEBULA_SIZE / 2.0F, 20.0F, 0.15F, 0.3F, 5.0F, SMOKE_COLOR);
      }

      float speed = v_proj.length();

      float angle;
      float vel;
      Vector2f vector;
      int i;
      for(i = 0; i <= 12; ++i) {
         angle = MathUtils.getRandomNumberInRange(0.0F, 360.0F);
         vel = MathUtils.getRandomNumberInRange(speed * -0.03F, speed * -0.3F);
         vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
         float particlesize = MathUtils.getRandomNumberInRange(1.0F, 4.0F);
         engine.addHitParticle(loc_boom, vector, particlesize, 255.0F, 5.0F, Color.white);
         engine.addHitParticle(loc_boom, vector, particlesize * 5.0F, 255.0F, 3.75F, GLOW_COLOR);
      }

      for(i = 0; i <= 36; ++i) {
         angle = MathUtils.getRandomNumberInRange(0.0F, 360.0F);
         vel = MathUtils.getRandomNumberInRange(speed * -0.03F, speed * -0.3F);
         vector = MathUtils.getPointOnCircumference((Vector2f)null, vel * 1.5F, angle);
         engine.addHitParticle(loc_boom, vector, MathUtils.getRandomNumberInRange(3.0F, 7.0F), 255.0F, 3.75F, PARTICLE_COLOR);
      }

   }
}
