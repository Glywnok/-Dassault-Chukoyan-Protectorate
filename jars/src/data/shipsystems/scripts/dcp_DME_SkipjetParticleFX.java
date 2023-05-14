package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import data.scripts.DMEUtils;
import java.awt.Color;
import java.util.Iterator;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_SkipjetParticleFX {
   private final float baseSize;
   private final float baseDuration;
   private final float baseBrightness;
   private final float baseChance;
   private final float velMult;
   private final float coneAngle;
   private final Color fullColor;
   private static final float CONTRIBUTION_WEIGHT = 0.2F;
   private static final float EFFECT_LEVEL_WEIGHT = 0.05F;

   public dcp_DME_SkipjetParticleFX(float baseSize, float baseDuration, float baseBrightness, float baseChance, float velMult, float coneAngle, Color fullColor) {
      this.baseSize = baseSize;
      this.baseDuration = baseDuration;
      this.baseBrightness = baseBrightness;
      this.baseChance = baseChance;
      this.velMult = velMult;
      this.coneAngle = coneAngle;
      this.fullColor = fullColor;
   }

   public void apply(ShipAPI ship, CombatEngineAPI combat, float effectLevel) {
      Vector2f baseVel = ship.getVelocity().negate(new Vector2f());
      Iterator var5 = ship.getEngineController().getShipEngines().iterator();

      while(var5.hasNext()) {
         ShipEngineAPI engine = (ShipEngineAPI)var5.next();
         float chance = effectLevel * 0.05F + engine.getContribution() * 0.2F;
         if (engine.isActive() && !engine.isSystemActivated() && Math.random() <= (double)(this.baseChance + chance)) {
            float angle = MathUtils.getRandomNumberInRange(-this.coneAngle, this.coneAngle);
            Vector2f vel = (Vector2f)VectorUtils.rotate(baseVel, angle, new Vector2f()).scale(this.velMult);
            float size = this.baseSize * (effectLevel + (float)Math.random());
            Color color = DMEUtils.lerpRGB(engine.getEngineColor(), this.fullColor, effectLevel);
            combat.addSmoothParticle(engine.getLocation(), vel, size, this.baseBrightness + chance, this.baseDuration + chance, color);
         }
      }

   }
}
