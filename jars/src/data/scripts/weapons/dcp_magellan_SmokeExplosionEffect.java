package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_SmokeExplosionEffect implements ProximityExplosionEffect {
   private static final float FX_DURATION = 7.5F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final Color BURST_COLOR = new Color(75, 50, 25, 25);
   private static final Color SMOKE_COLOR = new Color(135, 135, 135, 120);
   private static final float NEBULA_SIZE = 30.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 15.0F;
   private static final float SMOKE_RADIUS = 75.0F;
   private static final int SMOKE_COUNT = 4;
   private static final float FLARE_RADIUS = 30.0F;
   private static final int FLARE_COUNT = 2;

   public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
      CombatEngineAPI engine = Global.getCombatEngine();
      Vector2f loc_proj = new Vector2f(originalProjectile.getLocation());
      Vector2f v_proj = new Vector2f(originalProjectile.getVelocity());
      Vector2f v_boom = new Vector2f(explosion.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_boom, new Vector2f()).scale(0.3F);
      engine.addNebulaSmokeParticle(loc_proj, v_comp, NEBULA_SIZE, 15.0F, 0.1F, 0.3F, 7.5F, SMOKE_COLOR);
      engine.addNebulaSmokeParticle(loc_proj, v_comp, NEBULA_SIZE, 15.0F, 0.1F, 0.6F, 7.5F, SMOKE_COLOR);

      int i;
      Vector2f random_point;
      for(i = 0; i <= 3; ++i) {
         random_point = new Vector2f(MathUtils.getRandomPointInCircle(loc_proj, 75.0F));
         engine.spawnExplosion(random_point, v_comp, BURST_COLOR, NEBULA_SIZE * 2.0F, 0.07F);
         engine.addNebulaSmokeParticle(random_point, v_comp, NEBULA_SIZE / 2.0F, 15.0F, 0.1F, 0.3F, 7.5F, SMOKE_COLOR);
      }

      for(i = 0; i <= 1; ++i) {
         random_point = new Vector2f(MathUtils.getRandomPointInCircle(loc_proj, 30.0F));
         float angle = (float)(Math.random() * 360.0D);
         engine.spawnProjectile(originalProjectile.getSource(), originalProjectile.getWeapon(), "magellan_microflares", random_point, angle, v_comp);
      }

   }
}
