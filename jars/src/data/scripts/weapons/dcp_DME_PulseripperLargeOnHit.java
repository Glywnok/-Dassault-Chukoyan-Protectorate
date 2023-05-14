package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.util.FaderUtil;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PulseripperLargeOnHit extends BaseCombatLayeredRenderingPlugin implements OnHitEffectPlugin {
   public static int NUM_TICKS = 11;
   public static float TOTAL_DAMAGE = 250.0F;
   private static final Color NEBULA_COLOR = new Color(233, 115, 63, 255);
   private static final float NEBULA_SIZE = 7.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 20.0F;
   private static final float NEBULA_DUR = 1.0F;
   private static final float NEBULA_RAMPUP = 0.2F;
   protected List<dcp_DME_PulseripperLargeOnHit.ParticleData> particles = new ArrayList();
   protected DamagingProjectileAPI proj;
   protected ShipAPI target;
   protected Vector2f offset;
   protected int ticks = 0;
   protected IntervalUtil interval;
   protected FaderUtil fader = new FaderUtil(1.0F, 0.15F, 0.45F);
   protected EnumSet<CombatEngineLayers> layers;

   public dcp_DME_PulseripperLargeOnHit() {
      this.layers = EnumSet.of(CombatEngineLayers.BELOW_INDICATORS_LAYER);
   }

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      if (!shieldHit) {
         if (!projectile.isFading()) {
            if (target instanceof ShipAPI) {
               Vector2f offset = Vector2f.sub(point, target.getLocation(), new Vector2f());
               offset = Misc.rotateAroundOrigin(offset, -target.getFacing());
               dcp_DME_PulseripperLargeOnHit effect = new dcp_DME_PulseripperLargeOnHit(projectile, (ShipAPI)target, offset);
               CombatEntityAPI e = engine.addLayeredRenderingPlugin(effect);
               e.getLocation().set(projectile.getLocation());
               engine.addSwirlyNebulaParticle(point, target.getVelocity(), NEBULA_SIZE, 20.0F, 0.2F, 0.2F, 1.0F, NEBULA_COLOR, true);
               engine.spawnExplosion(point, target.getVelocity(), NEBULA_COLOR, NEBULA_SIZE * 4.0F, 0.5F);
            }
         }
      }
   }

   public dcp_DME_PulseripperLargeOnHit(DamagingProjectileAPI proj, ShipAPI target, Vector2f offset) {
      this.layers = EnumSet.of(CombatEngineLayers.BELOW_INDICATORS_LAYER);
      this.proj = proj;
      this.target = target;
      this.offset = offset;
      this.interval = new IntervalUtil(0.8F, 1.0F);
      this.interval.forceIntervalElapsed();
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
         Vector2f loc = new Vector2f(this.offset);
         loc = Misc.rotateAroundOrigin(loc, this.target.getFacing());
         Vector2f.add(this.target.getLocation(), loc, loc);
         this.entity.getLocation().set(loc);
         List<dcp_DME_PulseripperLargeOnHit.ParticleData> remove = new ArrayList();
         Iterator var4 = this.particles.iterator();

         while(var4.hasNext()) {
            dcp_DME_PulseripperLargeOnHit.ParticleData p = (dcp_DME_PulseripperLargeOnHit.ParticleData)var4.next();
            p.advance(amount);
            if (p.elapsed >= p.maxDur) {
               remove.add(p);
            }
         }

         this.particles.removeAll(remove);
         float volume = 1.0F;
         if (this.ticks >= NUM_TICKS || !this.target.isAlive() || !Global.getCombatEngine().isEntityInPlay(this.target)) {
            this.fader.fadeOut();
            this.fader.advance(amount);
            volume = this.fader.getBrightness();
         }

         Global.getSoundPlayer().playLoop("disintegrator_loop", this.target, 1.0F, volume, loc, this.target.getVelocity());
         this.interval.advance(amount);
         if (this.interval.intervalElapsed() && this.ticks < NUM_TICKS) {
            this.dealDamage();
            ++this.ticks;
         }

      }
   }

   protected void dealDamage() {
      CombatEngineAPI engine = Global.getCombatEngine();
      int num = 3;

      for(int i = 0; i < num; ++i) {
         dcp_DME_PulseripperLargeOnHit.ParticleData p = new dcp_DME_PulseripperLargeOnHit.ParticleData(30.0F, 3.0F + (float)Math.random() * 2.0F, 2.0F);
         this.particles.add(p);
         p.offset = Misc.getPointWithinRadius(p.offset, 20.0F);
      }

      Vector2f point = new Vector2f(this.entity.getLocation());
      ArmorGridAPI grid = this.target.getArmorGrid();
      int[] cell = grid.getCellAtLocation(point);
      if (cell != null) {
         int gridWidth = grid.getGrid().length;
         int gridHeight = grid.getGrid()[0].length;
         float damageTypeMult = getDamageTypeMult(this.proj.getSource(), this.target);
         float damagePerTick = TOTAL_DAMAGE / (float)NUM_TICKS;
         float damageDealt = 0.0F;

         for(int i = -2; i <= 2; ++i) {
            for(int j = -2; j <= 2; ++j) {
               if (i != 2 && i != -2 || j != 2 && j != -2) {
                  int cx = cell[0] + i;
                  int cy = cell[1] + j;
                  if (cx >= 0 && cx < gridWidth && cy >= 0 && cy < gridHeight) {
                     float damMult = 0.033333335F;
                     if (i == 0 && j == 0) {
                        damMult = 0.06666667F;
                     } else if (i <= 1 && i >= -1 && j <= 1 && j >= -1) {
                        damMult = 0.06666667F;
                     } else {
                        damMult = 0.033333335F;
                     }

                     float armorInCell = grid.getArmorValue(cx, cy);
                     float damage = damagePerTick * damMult * damageTypeMult;
                     damage = Math.min(damage, armorInCell);
                     if (!(damage <= 0.0F)) {
                        this.target.getArmorGrid().setArmorValue(cx, cy, Math.max(0.0F, armorInCell - damage));
                        damageDealt += damage;
                     }
                  }
               }
            }
         }

         if (damageDealt > 0.0F) {
            if (Misc.shouldShowDamageFloaty(this.proj.getSource(), this.target)) {
               engine.addFloatingDamageText(point, damageDealt, Misc.FLOATY_ARMOR_DAMAGE_COLOR, this.target, this.proj.getSource());
            }

            this.target.syncWithArmorGridState();
         }

      }
   }

   public boolean isExpired() {
      return this.particles.isEmpty() && (this.ticks >= NUM_TICKS || !this.target.isAlive() || !Global.getCombatEngine().isEntityInPlay(this.target));
   }

   public void render(CombatEngineLayers layer, ViewportAPI viewport) {
      float x = this.entity.getLocation().x;
      float y = this.entity.getLocation().y;
      Color color = new Color(25, 50, 110, 75);
      float b = viewport.getAlphaMult();
      GL14.glBlendEquation(32779);
      Iterator var7 = this.particles.iterator();

      while(var7.hasNext()) {
         dcp_DME_PulseripperLargeOnHit.ParticleData p = (dcp_DME_PulseripperLargeOnHit.ParticleData)var7.next();
         float size = p.baseSize * p.scale;
         Vector2f loc = new Vector2f(x + p.offset.x, y + p.offset.y);
         float alphaMult = 1.0F;
         p.sprite.setAngle(p.angle);
         p.sprite.setSize(size, size);
         p.sprite.setAlphaMult(b * alphaMult * p.fader.getBrightness());
         p.sprite.setColor(color);
         p.sprite.renderAtCenter(loc.x, loc.y);
      }

      GL14.glBlendEquation(32774);
   }

   public static float getDamageTypeMult(ShipAPI source, ShipAPI target) {
      if (source != null && target != null) {
         float damageTypeMult = target.getMutableStats().getArmorDamageTakenMult().getModifiedValue();
         switch(target.getHullSize()) {
         case CAPITAL_SHIP:
            damageTypeMult *= source.getMutableStats().getDamageToCapital().getModifiedValue();
            break;
         case CRUISER:
            damageTypeMult *= source.getMutableStats().getDamageToCruisers().getModifiedValue();
            break;
         case DESTROYER:
            damageTypeMult *= source.getMutableStats().getDamageToDestroyers().getModifiedValue();
            break;
         case FRIGATE:
            damageTypeMult *= source.getMutableStats().getDamageToFrigates().getModifiedValue();
            break;
         case FIGHTER:
            damageTypeMult *= source.getMutableStats().getDamageToFighters().getModifiedValue();
         }

         return damageTypeMult;
      } else {
         return 1.0F;
      }
   }

   public static class ParticleData {
      public SpriteAPI sprite = Global.getSettings().getSprite("misc", "nebula_particles");
      public Vector2f offset = new Vector2f();
      public Vector2f vel = new Vector2f();
      public float scale = 1.0F;
      public float scaleIncreaseRate = 1.0F;
      public float turnDir = 1.0F;
      public float angle = 1.0F;
      public float maxDur;
      public FaderUtil fader;
      public float elapsed = 0.0F;
      public float baseSize;

      public ParticleData(float baseSize, float maxDur, float endSizeMult) {
         float i = (float)Misc.random.nextInt(4);
         float j = (float)Misc.random.nextInt(4);
         this.sprite.setTexWidth(0.25F);
         this.sprite.setTexHeight(0.25F);
         this.sprite.setTexX(i * 0.25F);
         this.sprite.setTexY(j * 0.25F);
         this.sprite.setAdditiveBlend();
         this.angle = (float)Math.random() * 360.0F;
         this.maxDur = maxDur;
         this.scaleIncreaseRate = endSizeMult / maxDur;
         if (endSizeMult < 1.0F) {
            this.scaleIncreaseRate = -1.0F * endSizeMult;
         }

         this.scale = 1.0F;
         this.baseSize = baseSize;
         this.turnDir = Math.signum((float)Math.random() - 0.5F) * 20.0F * (float)Math.random();
         float driftDir = (float)Math.random() * 360.0F;
         this.vel = Misc.getUnitVectorAtDegreeAngle(driftDir);
         this.vel.scale(0.25F * baseSize / maxDur * (1.0F + (float)Math.random() * 1.0F));
         this.fader = new FaderUtil(0.0F, 0.15F, 0.45F);
         this.fader.forceOut();
         this.fader.fadeIn();
      }

      public void advance(float amount) {
         this.scale += this.scaleIncreaseRate * amount;
         Vector2f var10000 = this.offset;
         var10000.x += this.vel.x * amount;
         var10000 = this.offset;
         var10000.y += this.vel.y * amount;
         this.angle += this.turnDir * amount;
         this.elapsed += amount;
         if (this.maxDur - this.elapsed <= this.fader.getDurationOut() + 0.1F) {
            this.fader.fadeOut();
         }

         this.fader.advance(amount);
      }
   }
}
