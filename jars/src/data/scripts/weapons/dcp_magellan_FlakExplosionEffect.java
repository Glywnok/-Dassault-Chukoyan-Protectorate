package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ProximityExplosionEffect;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_FlakExplosionEffect implements ProximityExplosionEffect {
   private static final float FX_DURATION = 1.5F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private static final Color BURST_COLOR = new Color(105, 85, 55, 60);
   private static final Color SMOKE_COLOR = new Color(100, 100, 100, 200);
   private static final float NEBULA_SIZE = 20.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 10.0F;
   private static final float SMOKE_RADIUS = 20.0F;
   private static final int SMOKE_COUNT = 3;

   public void onExplosion(DamagingProjectileAPI explosion, DamagingProjectileAPI originalProjectile) {
      CombatEngineAPI engine = Global.getCombatEngine();
      Vector2f loc_proj = new Vector2f(originalProjectile.getLocation());
      Vector2f v_proj = new Vector2f(originalProjectile.getVelocity());
      Vector2f v_boom = new Vector2f(explosion.getVelocity());
      Vector2f v_comp = (Vector2f)Vector2f.sub(v_proj, v_boom, new Vector2f()).scale(0.05F);
      engine.addNebulaSmokeParticle(loc_proj, v_comp, NEBULA_SIZE, 10.0F, 0.1F, 0.3F, 1.5F, SMOKE_COLOR);
      engine.addNebulaSmokeParticle(loc_proj, v_comp, NEBULA_SIZE, 10.0F, 0.1F, 0.6F, 1.5F, SMOKE_COLOR);

      for(int i = 0; i <= 2; ++i) {
         Vector2f random_point = new Vector2f(MathUtils.getRandomPointInCircle(loc_proj, 20.0F));
         engine.spawnExplosion(random_point, v_comp, BURST_COLOR, NEBULA_SIZE * 2.0F, 0.1F);
         engine.addNebulaSmokeParticle(random_point, v_comp, NEBULA_SIZE / 2.0F, 10.0F, 0.1F, 0.3F, 1.5F, SMOKE_COLOR);
      }

   }
}
