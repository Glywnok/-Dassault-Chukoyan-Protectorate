package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.FaderUtil;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_FusCutterEffect extends BaseCombatLayeredRenderingPlugin implements OnFireEffectPlugin, OnHitEffectPlugin, EveryFrameWeaponEffectPlugin {
   protected List<dcp_magellan_FusCutterEffect> trails;
   protected List<dcp_magellan_FusCutterEffect.ParticleData> particles = new ArrayList();
   protected DamagingProjectileAPI proj;
   protected DamagingProjectileAPI prev;
   protected float baseFacing = 0.0F;
   protected EnumSet<CombatEngineLayers> layers;

   public dcp_magellan_FusCutterEffect() {
      this.layers = EnumSet.of(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (this.trails != null) {
         Iterator iter = this.trails.iterator();

         while(iter.hasNext()) {
            if (((dcp_magellan_FusCutterEffect)iter.next()).isExpired()) {
               iter.remove();
            }
         }

         float maxRange;
         float dist1;
         float e;
         float t;
         if (weapon.getShip() != null) {
            maxRange = weapon.getRange();
            ShipAPI ship = weapon.getShip();
            Vector2f com = new Vector2f();
            float weight = 0.0F;
            dist1 = 0.0F;
            Vector2f source = weapon.getLocation();
            Iterator var11 = this.trails.iterator();

            while(var11.hasNext()) {
               dcp_magellan_FusCutterEffect curr = (dcp_magellan_FusCutterEffect)var11.next();
               if (curr.proj != null) {
                  Vector2f.add(com, curr.proj.getLocation(), com);
                  weight += curr.proj.getBrightness();
                  dist1 += Misc.getDistance(source, curr.proj.getLocation());
               }
            }

            if (weight > 0.1F) {
               com.scale(1.0F / weight);
               e = Math.min(weight, 1.0F);
               if (this.trails.size() > 0) {
                  dist1 /= (float)this.trails.size();
                  t = dist1 / Math.max(maxRange, 1.0F);
                  t = 1.0F - t;
                  if (t > 1.0F) {
                     t = 1.0F;
                  }

                  if (t < 0.0F) {
                     t = 0.0F;
                  }

                  t = (float)Math.sqrt((double)t);
                  e *= t;
               }

               Global.getSoundPlayer().playLoop("magellan_flamer_loop", ship, 1.0F, e, com, ship.getVelocity());
            }
         }

         maxRange = 1.0F;
         amount /= maxRange;

         for(int i = 0; (float)i < maxRange; ++i) {
            Iterator var17 = this.trails.iterator();

            while(var17.hasNext()) {
               dcp_magellan_FusCutterEffect trail = (dcp_magellan_FusCutterEffect)var17.next();
               if (trail.prev != null && !trail.prev.isExpired() && Global.getCombatEngine().isEntityInPlay(trail.prev)) {
                  dist1 = Misc.getDistance(trail.prev.getLocation(), trail.proj.getLocation());
                  if (dist1 < trail.proj.getProjectileSpec().getLength() * 1.0F) {
                     float maxSpeed = trail.prev.getMoveSpeed() * 0.5F;
                     e = trail.prev.getElapsed();
                     t = 0.5F;
                     if (e > t) {
                        maxSpeed *= Math.max(0.25F, 1.0F - (e - t) * 0.5F);
                     }

                     if (dist1 < 20.0F && e > t) {
                        maxSpeed *= dist1 / 20.0F;
                     }

                     Vector2f driftTo = Misc.closestPointOnLineToPoint(trail.proj.getLocation(), trail.proj.getTailEnd(), trail.prev.getLocation());
                     float dist = Misc.getDistance(driftTo, trail.prev.getLocation());
                     Vector2f diff = Vector2f.sub(driftTo, trail.prev.getLocation(), new Vector2f());
                     diff = Misc.normalise(diff);
                     diff.scale(Math.min(dist, maxSpeed * amount));
                     Vector2f.add(trail.prev.getLocation(), diff, trail.prev.getLocation());
                     Vector2f.add(trail.prev.getTailEnd(), diff, trail.prev.getTailEnd());
                  }
               }
            }
         }

      }
   }

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Color color = projectile.getProjectileSpec().getFringeColor();
      Vector2f vel = new Vector2f();
      if (target instanceof ShipAPI) {
         vel.set(target.getVelocity());
      }

      float size = projectile.getProjectileSpec().getWidth() * 1.0F;
      float sizeMult = Misc.getHitGlowSize(100.0F, projectile.getDamage().getBaseDamage(), damageResult) / 100.0F;
      float dur = 1.0F;
      float rampUp = 0.0F;
      engine.addNebulaParticle(point, vel, size, 3.0F + 2.0F * sizeMult, rampUp, 0.0F, dur, color);
      Misc.playSound(damageResult, point, vel, "cryoflamer_hit_shield_light", "cryoflamer_hit_shield_solid", "cryoflamer_hit_shield_heavy", "cryoflamer_hit_light", "cryoflamer_hit_solid", "cryoflamer_hit_heavy");
   }

   public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
      String prevKey = "cryo_prev_" + weapon.getShip().getId() + "_" + weapon.getSlot().getId();
      DamagingProjectileAPI prev = (DamagingProjectileAPI)engine.getCustomData().get(prevKey);
      dcp_magellan_FusCutterEffect trail = new dcp_magellan_FusCutterEffect(projectile, prev);
      CombatEntityAPI e = engine.addLayeredRenderingPlugin(trail);
      e.getLocation().set(projectile.getLocation());
      engine.getCustomData().put(prevKey, projectile);
      if (this.trails == null) {
         this.trails = new ArrayList();
      }

      this.trails.add(0, trail);
   }

   public dcp_magellan_FusCutterEffect(DamagingProjectileAPI proj, DamagingProjectileAPI prev) {
      this.layers = EnumSet.of(CombatEngineLayers.ABOVE_SHIPS_AND_MISSILES_LAYER);
      this.proj = proj;
      this.prev = prev;
      this.baseFacing = proj.getFacing();
      int num = 7;

      for(int i = 0; i < num; ++i) {
         this.particles.add(new dcp_magellan_FusCutterEffect.ParticleData(proj));
      }

      float length = proj.getProjectileSpec().getLength();
      float width = proj.getProjectileSpec().getWidth();
      float index = 0.0F;

      for(Iterator var7 = this.particles.iterator(); var7.hasNext(); ++index) {
         dcp_magellan_FusCutterEffect.ParticleData p = (dcp_magellan_FusCutterEffect.ParticleData)var7.next();
         float f = index / (float)(this.particles.size() - 1);
         Vector2f dir = Misc.getUnitVectorAtDegreeAngle(proj.getFacing() + 180.0F);
         dir.scale(length * f);
         Vector2f.add(p.offset, dir, p.offset);
         p.offset = Misc.getPointWithinRadius(p.offset, width * 0.5F);
      }

   }

   public float getRenderRadius() {
      return 120.0F;
   }

   public EnumSet<CombatEngineLayers> getActiveLayers() {
      return this.layers;
   }

   public void init(CombatEntityAPI entity) {
      super.init(entity);
   }

   public void advance(float amount) {
      if (!Global.getCombatEngine().isPaused()) {
         this.entity.getLocation().set(this.proj.getLocation());
         Iterator var2 = this.particles.iterator();

         while(var2.hasNext()) {
            dcp_magellan_FusCutterEffect.ParticleData p = (dcp_magellan_FusCutterEffect.ParticleData)var2.next();
            p.advance(amount);
         }

      }
   }

   public boolean isExpired() {
      return this.proj.isExpired() || !Global.getCombatEngine().isEntityInPlay(this.proj);
   }

   public void render(CombatEngineLayers layer, ViewportAPI viewport) {
      float x = this.entity.getLocation().x;
      float y = this.entity.getLocation().y;
      Color color = this.proj.getProjectileSpec().getFringeColor();
      color = Misc.setAlpha(color, 50);
      float b = this.proj.getBrightness();
      b *= viewport.getAlphaMult();
      Iterator var7 = this.particles.iterator();

      while(var7.hasNext()) {
         dcp_magellan_FusCutterEffect.ParticleData p = (dcp_magellan_FusCutterEffect.ParticleData)var7.next();
         float size = this.proj.getProjectileSpec().getWidth() * 0.5F;
         size *= p.scale;
         float alphaMult = 1.0F;
         Vector2f offset = p.offset;
         float diff = Misc.getAngleDiff(this.baseFacing, this.proj.getFacing());
         if (Math.abs(diff) > 0.1F) {
            offset = Misc.rotateAroundOrigin(offset, diff);
         }

         Vector2f loc = new Vector2f(x + offset.x, y + offset.y);
         p.sprite.setAngle(p.angle);
         p.sprite.setSize(size, size);
         p.sprite.setAlphaMult(b * alphaMult * p.fader.getBrightness());
         p.sprite.setColor(color);
         p.sprite.renderAtCenter(loc.x, loc.y);
      }

   }

   public static class ParticleData {
      public SpriteAPI sprite;
      public Vector2f offset = new Vector2f();
      public Vector2f vel = new Vector2f();
      public float scale = 1.0F;
      public DamagingProjectileAPI proj;
      public float scaleIncreaseRate = 1.0F;
      public float turnDir = 1.0F;
      public float angle = 1.0F;
      public FaderUtil fader;

      public ParticleData(DamagingProjectileAPI proj) {
         this.proj = proj;
         this.sprite = Global.getSettings().getSprite("misc", "nebula_particles");
         float i = (float)Misc.random.nextInt(4);
         float j = (float)Misc.random.nextInt(4);
         this.sprite.setTexWidth(0.25F);
         this.sprite.setTexHeight(0.25F);
         this.sprite.setTexX(i * 0.25F);
         this.sprite.setTexY(j * 0.25F);
         this.sprite.setAdditiveBlend();
         this.angle = (float)Math.random() * 360.0F;
         float maxDur = proj.getWeapon().getRange() / proj.getWeapon().getProjectileSpeed();
         this.scaleIncreaseRate = 2.0F / maxDur;
         this.scale = 1.0F;
         this.turnDir = Math.signum((float)Math.random() - 0.5F) * 60.0F * (float)Math.random();
         float driftDir = (float)Math.random() * 360.0F;
         this.vel = Misc.getUnitVectorAtDegreeAngle(driftDir);
         this.vel.scale(proj.getProjectileSpec().getWidth() / maxDur * 0.33F);
         this.fader = new FaderUtil(0.0F, 0.2F, 0.4F);
         this.fader.fadeIn();
      }

      public void advance(float amount) {
         this.scale += this.scaleIncreaseRate * amount;
         if (this.scale < 1.0F) {
            this.scale += this.scaleIncreaseRate * amount * 1.0F;
         }

         Vector2f var10000 = this.offset;
         var10000.x += this.vel.x * amount;
         var10000 = this.offset;
         var10000.y += this.vel.y * amount;
         this.angle += this.turnDir * amount;
         this.fader.advance(amount);
      }
   }
}
