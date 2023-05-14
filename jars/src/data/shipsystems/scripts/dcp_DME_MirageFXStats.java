package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import data.scripts.DMEUtils;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public abstract class dcp_DME_MirageFXStats implements ShipSystemStatsScript {
   private static final Color PARTICLE_COLOR_1 = new Color(145, 175, 255);
   private static final Color PARTICLE_COLOR_2 = new Color(85, 115, 215);
   private static final float PARTICLE_SIZE_MIN = 5.0F;
   private static final float PARTICLE_SIZE_MAX = 9.0F;
   private static final int PARTICLE_MAX = 28;
   private static final float PARTICLE_DURATION = 0.66F;
   private static final float PARTICLE_VELOCITY_MULT = 1.2F;
   private static final float MAX_RADIUS_MULT = 1.25F;

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      if (stats.getEntity() instanceof ShipAPI) {
         ShipAPI ship = (ShipAPI)stats.getEntity();
         CombatEngineAPI engine = Global.getCombatEngine();
         if (state == State.IN) {
            Vector2f loc = new Vector2f(ship.getLocation());
            float min_radius = ship.getCollisionRadius();
            float max_radius = min_radius * 1.25F;
            int n_particles = Math.round(28.0F - effectLevel * 28.0F);

            for(int i = 0; i < n_particles; ++i) {
               float radius = MathUtils.getRandomNumberInRange(min_radius, max_radius);
               Vector2f pos = MathUtils.getRandomPointOnCircumference(loc, radius);
               Vector2f vel = (Vector2f)Vector2f.sub(loc, pos, new Vector2f()).scale(1.2F);
               float size = MathUtils.getRandomNumberInRange(5.0F, 9.0F);
               float brightness = (float)Math.random();
               Color color = DMEUtils.lerpRGB(PARTICLE_COLOR_2, PARTICLE_COLOR_1, effectLevel);
               engine.addSmoothParticle(pos, vel, size, brightness, 0.66F, color);
            }
         }

      }
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      return state == State.IN && index == 0 ? new StatusData("Skipspace jaunt in progress", false) : null;
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
   }
}
