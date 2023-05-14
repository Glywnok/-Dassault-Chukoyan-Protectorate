package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_LargeMortarOnHit implements OnHitEffectPlugin {
   private static final float FX_DURATION = 3.0F;
   private static final float PUSHMULT = 0.15F;
   private static final float NEBULA_RAMPUP = 0.15F;
   private static final Color BURST_COLOR = new Color(210, 170, 60, 155);
   private static final Color SMOKE_COLOR = new Color(75, 75, 75, 155);
   private static final float NEBULA_SIZE = 40.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 20.0F;
   private static final float SMOKE_RADIUS = 50.0F;
   private static final int SMOKE_COUNT = 3;
   private static final Color PARTICLE_COLOR = new Color(210, 170, 60, 255);
   private static final Color GLOW_COLOR = new Color(90, 75, 0, 45);
   private static final int PARTICLE_COUNT = 6;
   private static final float PARTICLE_SIZE = 1.0F;
   private static final float PARTICLE_BRIGHTNESS = 255.0F;
   private static final float VELMINMULT = 0.03F;
   private static final float VELMAXMULT = 0.3F;
   private static final float CONE_ANGLE = 150.0F;
   private static final float A_2 = 75.0F;
   private static final float A_3 = 50.0F;
   private static final String SFX = "magellan_mine_explosion_sm";

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f v_proj = new Vector2f(projectile.getVelocity());
      Vector2f v_boom = new Vector2f(target.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_boom, new Vector2f()).scale(0.1F);
      engine.spawnExplosion(point, v_comp, BURST_COLOR, NEBULA_SIZE * 6.0F, 1.0F);
      engine.addNebulaSmokeParticle(point, v_comp, NEBULA_SIZE, 20.0F, 0.15F, 0.3F, 3.0F, SMOKE_COLOR);
      engine.addNebulaSmokeParticle(point, v_comp, NEBULA_SIZE, 20.0F, 0.15F, 0.6F, 3.0F, SMOKE_COLOR);

      for(int i = 0; i <= 2; ++i) {
         Vector2f random_point = new Vector2f(MathUtils.getRandomPointInCircle(point, 50.0F));
         engine.spawnExplosion(random_point, v_comp, BURST_COLOR, NEBULA_SIZE * 1.5F, 1.0F);
         engine.addNebulaSmokeParticle(random_point, v_comp, NEBULA_SIZE / 2.0F, 20.0F, 0.15F, 0.3F, 3.0F, SMOKE_COLOR);
      }

      float speed = v_proj.length();
      float facing = projectile.getFacing();

      int i;
      float angle;
      float vel;
      Vector2f vector;
      for(i = 0; i <= 6; ++i) {
         angle = MathUtils.getRandomNumberInRange(facing - 50.0F, facing + 50.0F);
         vel = MathUtils.getRandomNumberInRange(speed * -0.03F, speed * -0.3F);
         vector = MathUtils.getPointOnCircumference((Vector2f)null, vel, angle);
         float particlesize = MathUtils.getRandomNumberInRange(1.0F, 4.0F);
         engine.addHitParticle(point, vector, particlesize, 255.0F, 3.0F, Color.white);
         engine.addHitParticle(point, vector, particlesize * 5.0F, 255.0F, 2.25F, GLOW_COLOR);
      }

      for(i = 0; i <= 18; ++i) {
         angle = MathUtils.getRandomNumberInRange(facing - 75.0F, facing + 75.0F);
         vel = MathUtils.getRandomNumberInRange(speed * -0.03F, speed * -0.3F);
         vector = MathUtils.getPointOnCircumference((Vector2f)null, vel * 1.5F, angle);
         engine.addHitParticle(point, vector, MathUtils.getRandomNumberInRange(3.0F, 7.0F), 255.0F, 2.25F, PARTICLE_COLOR);
      }

      CombatUtils.applyForce(target, v_proj, speed * 0.15F);
      Global.getSoundPlayer().playSound("magellan_mine_explosion_sm", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
   }
}
