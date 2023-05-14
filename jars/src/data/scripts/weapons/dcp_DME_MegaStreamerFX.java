package data.scripts.weapons;

import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DMEUtils;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lazywizard.lazylib.FastTrig;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_MegaStreamerFX implements EveryFrameWeaponEffectPlugin {
   private static final float FIRE_DURATION = 2.4F;
   private static final Color FLASH_COLOR = new Color(75, 255, 175, 255);
   private static final float OFFSET = 2.0F;
   private static final Color PARTICLE_COLOR = new Color(75, 255, 175, 255);
   private float elapsed = 0.0F;
   private final float oscillationTimePrim = 0.12F;
   private final float oscillationTimeSec = 0.3F;
   private float counter = 0.0F;
   private boolean runOnce = true;
   private Map<Integer, BeamAPI> beamMap = new HashMap();
   private Map<Integer, Float> oscillationWidthMap = new HashMap();

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         float radCountPrim;
         float radCountSec;
         if (weapon.isFiring()) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            Vector2f particle_offset;
            if (this.elapsed <= 0.0F) {
               particle_offset = DMEUtils.translate_polar(weapon_location, 5.0F, weapon.getCurrAngle());
               engine.spawnExplosion(particle_offset, ship.getVelocity(), FLASH_COLOR, 75.0F, 0.4F);
            }

            this.elapsed += amount;
            particle_offset = DMEUtils.translate_polar(weapon_location, 2.0F, weapon.getCurrAngle());
            int particle_count_this_frame = (int)(7.0F * (2.4F - this.elapsed));

            for(int x = 0; x < particle_count_this_frame; ++x) {
               float size = DMEUtils.get_random(3.0F, 12.0F);
               radCountPrim = DMEUtils.get_random(200.0F, 400.0F);
               radCountSec = weapon.getCurrAngle() + DMEUtils.get_random(-7.0F, 7.0F);
               Vector2f velocity = DMEUtils.translate_polar(ship.getVelocity(), radCountPrim, radCountSec);
               engine.addHitParticle(particle_offset, velocity, size, 1.5F, 0.3F, PARTICLE_COLOR);
            }
         } else {
            this.elapsed = 0.0F;
         }

         if (!engine.isPaused() && weapon != null) {
            if (weapon.getChargeLevel() <= 0.0F) {
               this.counter = 0.0F;
               this.beamMap.clear();
               this.oscillationWidthMap.clear();
               this.runOnce = true;
            } else {
               int counterForBeams;
               Iterator var14;
               if (weapon.getChargeLevel() > 0.0F && this.runOnce) {
                  counterForBeams = 0;
                  var14 = engine.getBeams().iterator();

                  while(var14.hasNext()) {
                     BeamAPI beam = (BeamAPI)var14.next();
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

               for(var14 = this.beamMap.keySet().iterator(); var14.hasNext(); ++counterForBeams) {
                  Integer i = (Integer)var14.next();
                  BeamAPI beam = (BeamAPI)this.beamMap.get(i);
                  if (this.oscillationWidthMap.get(i) == null) {
                     this.oscillationWidthMap.put(i, beam.getWidth());
                  }

                  radCountPrim = this.counter * 2.0F * 3.1415927F / 0.12F;
                  radCountSec = this.counter * 2.0F * 3.1415927F / 0.3F;
                  float oscillationPhasePrim = (float)FastTrig.sin((double)radCountPrim) * 0.4F + 0.6F;
                  float oscillationPhaseSec = (float)FastTrig.sin((double)radCountSec) * 0.2F + 0.8F;
                  float visMult = oscillationPhasePrim * oscillationPhaseSec;
                  beam.setWidth((Float)this.oscillationWidthMap.get(i) * visMult);
               }

            }
         }
      }
   }
}
