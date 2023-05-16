package data.scripts.weapons;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.Iterator;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_SigmaBeamAblationEffect implements BeamEffectPlugin {
   private boolean done = false;

   public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
      if (!this.done) {
         CombatEntityAPI target = beam.getDamageTarget();
         boolean first = beam.getWeapon().getBeams().indexOf(beam) == 0;
         if (target != null && beam.getBrightness() >= 1.0F && first) {
            Vector2f point = beam.getTo();
            float maxDist = 0.0F;

            BeamAPI curr;
            for(Iterator var8 = beam.getWeapon().getBeams().iterator(); var8.hasNext(); maxDist = Math.max(maxDist, Misc.getDistance(point, curr.getTo()))) {
               curr = (BeamAPI)var8.next();
            }

            if (maxDist < 15.0F) {
               DamagingProjectileAPI e = engine.spawnDamagingExplosion(this.createExplosionSpec(), beam.getSource(), point);
               e.addDamagedAlready(target);
               this.done = true;
            }
         }

      }
   }

   public DamagingExplosionSpec createExplosionSpec() {
      float damage = 60.0F;
      DamagingExplosionSpec spec = new DamagingExplosionSpec(0.05F, 16.0F, 9.0F, damage, damage / 2.0F, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 7.0F, 3.0F, 0.6F, 12, new Color(75, 255, 175, 255), new Color(25, 255, 125, 155));
      spec.setDamageType(DamageType.ENERGY);
      spec.setUseDetailedExplosion(false);
      spec.setSoundSetId("dcp_DME_energy_crit_sm");
      return spec;
   }
}
