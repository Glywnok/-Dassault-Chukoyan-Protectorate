package data.scripts.weapons;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lazywizard.lazylib.FastTrig;

public class dcp_magellan_beamOscillationScript implements EveryFrameWeaponEffectPlugin {
   private final float oscillationTimePrim = 0.12F;
   private final float oscillationTimeSec = 0.3F;
   private float counter = 0.0F;
   private boolean runOnce = true;
   private Map<Integer, BeamAPI> beamMap = new HashMap();
   private Map<Integer, Float> oscillationWidthMap = new HashMap();

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused() && weapon != null) {
         if (weapon.getChargeLevel() <= 0.0F) {
            this.counter = 0.0F;
            this.beamMap.clear();
            this.oscillationWidthMap.clear();
            this.runOnce = true;
         } else {
            int counterForBeams;
            Iterator var5;
            if (weapon.getChargeLevel() > 0.0F && this.runOnce) {
               counterForBeams = 0;
               var5 = engine.getBeams().iterator();

               while(var5.hasNext()) {
                  BeamAPI beam = (BeamAPI)var5.next();
                  if (beam.getWeapon() == weapon && !this.beamMap.containsValue(beam)) {
                     this.beamMap.put(counterForBeams, beam);
                     ++counterForBeams;
                  }
               }

               if (!this.beamMap.isEmpty()) {
                  this.runOnce = false;
               }
            }

            this.counter += amount;
            counterForBeams = 0;

            for(var5 = this.beamMap.keySet().iterator(); var5.hasNext(); ++counterForBeams) {
               Integer i = (Integer)var5.next();
               BeamAPI beam = (BeamAPI)this.beamMap.get(i);
               if (this.oscillationWidthMap.get(i) == null) {
                  this.oscillationWidthMap.put(i, beam.getWidth());
               }

               float radCountPrim = this.counter * 2.0F * 3.1415927F / 0.12F;
               float radCountSec = this.counter * 2.0F * 3.1415927F / 0.3F;
               float oscillationPhasePrim = (float)FastTrig.sin((double)radCountPrim) * 0.4F + 0.6F;
               float oscillationPhaseSec = (float)FastTrig.sin((double)radCountSec) * 0.2F + 0.8F;
               float visMult = oscillationPhasePrim * oscillationPhaseSec;
               beam.setWidth((Float)this.oscillationWidthMap.get(i) * visMult);
            }

         }
      }
   }
}
