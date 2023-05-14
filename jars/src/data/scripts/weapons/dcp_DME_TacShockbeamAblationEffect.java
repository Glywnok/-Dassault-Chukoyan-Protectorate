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

public class dcp_DME_TacShockbeamAblationEffect implements BeamEffectPlugin {
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
      float damage = 50.0F;
      DamagingExplosionSpec spec = new DamagingExplosionSpec(0.12F, 30.0F, 12.0F, damage, damage / 2.0F, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 5.0F, 2.0F, 0.5F, 16, new Color(140, 125, 255, 255), new Color(75, 100, 255, 200));
      spec.setDamageType(DamageType.FRAGMENTATION);
      spec.setUseDetailedExplosion(false);
      spec.setSoundSetId("istl_energy_crit_sm");
      return spec;
   }
}
