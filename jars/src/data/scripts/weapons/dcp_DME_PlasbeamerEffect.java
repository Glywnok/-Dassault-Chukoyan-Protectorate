package data.scripts.weapons;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PlasbeamerEffect implements BeamEffectPlugin {
   private IntervalUtil fireInterval = new IntervalUtil(0.2F, 0.3F);
   private boolean wasZero = true;

   public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
      CombatEntityAPI target = beam.getDamageTarget();
      if (target instanceof ShipAPI && beam.getBrightness() >= 1.0F) {
         float dur = beam.getDamage().getDpsDuration();
         if (!this.wasZero) {
            dur = 0.0F;
         }

         this.wasZero = beam.getDamage().getDpsDuration() <= 0.0F;
         this.fireInterval.advance(dur);
         if (this.fireInterval.intervalElapsed()) {
            ShipAPI ship = (ShipAPI)target;
            boolean hitShield = target.getShield() != null && target.getShield().isWithinArc(beam.getTo());
            float pierceChance = ((ShipAPI)target).getHardFluxLevel() - 0.1F;
            pierceChance *= ship.getMutableStats().getDynamic().getValue("shield_pierced_mult");
            boolean piercedShield = hitShield && (float)Math.random() < pierceChance;
            if (!hitShield || piercedShield) {
               Vector2f point = beam.getRayEndPrevFrame();
               float emp = beam.getDamage().getFluxComponent() * 1.0F;
               float dam = beam.getDamage().getDamage() * 0.5F;
               engine.spawnEmpArcPierceShields(beam.getSource(), point, beam.getDamageTarget(), beam.getDamageTarget(), DamageType.FRAGMENTATION, dam, emp, 100000.0F, "tachyon_lance_emp_impact", beam.getWidth() + 5.0F, beam.getFringeColor(), beam.getCoreColor());
            }
         }
      }

   }
}
