package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.IntervalUtil;
import data.scripts.util.MagicRender;
import java.awt.Color;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_SuperWitchJetsStats extends BaseShipSystemScript {
   private static final Color AFTERIMAGE_COLOR = new Color(55, 215, 105, 135);
   private static final float AFTERIMAGE_THRESHOLD = 0.2F;
   public static final float MAX_TIME_MULT = 3.0F;
   public static final Color JITTER_COLOR = new Color(75, 225, 125, 75);
   public static final Color JITTER_UNDER_COLOR = new Color(105, 255, 155, 155);
   private IntervalUtil interval = new IntervalUtil(0.15F, 0.15F);

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      ShipAPI ship = null;
      boolean player = false;
      CombatEngineAPI engine = Global.getCombatEngine();
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         player = ship == Global.getCombatEngine().getPlayerShip();
         id = id + "_" + ship.getId();
         float TimeMult = 1.0F + 2.0F * effectLevel;
         stats.getTimeMult().modifyMult(id, TimeMult);
         if (player) {
            Global.getCombatEngine().getTimeMult().modifyMult(id, 1.0F / TimeMult);
         } else {
            Global.getCombatEngine().getTimeMult().unmodify(id);
         }

         float driftamount = engine.getElapsedInLastFrame();
         if (state == State.IN) {
            ship.getMutableStats().getMaxSpeed().modifyFlat(id, 400.0F);
            ship.getMutableStats().getAcceleration().modifyFlat(id, 6000.0F);
            ship.getMutableStats().getDeceleration().modifyFlat(id, 4800.0F);
            ship.getMutableStats().getTurnAcceleration().modifyFlat(id, 800.0F);
            ship.getMutableStats().getMaxTurnRate().modifyFlat(id, 240.0F);
         } else {
            float speed;
            if (state == State.ACTIVE) {
               stats.getMaxSpeed().unmodify(id);
               stats.getAcceleration().unmodify(id);
               stats.getDeceleration().unmodify(id);
               stats.getTurnAcceleration().unmodify(id);
               stats.getMaxTurnRate().unmodify(id);
               speed = ship.getVelocity().length();
               if (speed <= 0.1F) {
                  ship.getVelocity().set(VectorUtils.getDirectionalVector(ship.getLocation(), ship.getVelocity()));
               }

               if (speed < 900.0F) {
                  ship.getVelocity().normalise();
                  ship.getVelocity().scale(speed + driftamount * 3200.0F);
               }
            } else {
               speed = ship.getVelocity().length();
               if (speed > ship.getMutableStats().getMaxSpeed().getModifiedValue()) {
                  ship.getVelocity().normalise();
                  ship.getVelocity().scale(speed - driftamount * 3200.0F);
               }
            }
         }

         if (!Global.getCombatEngine().isPaused()) {
            this.interval.advance(Global.getCombatEngine().getElapsedInLastFrame());
            if (this.interval.intervalElapsed()) {
               SpriteAPI sprite = ship.getSpriteAPI();
               float offsetX = sprite.getWidth() / 2.0F - sprite.getCenterX();
               float offsetY = sprite.getHeight() / 2.0F - sprite.getCenterY();
               float trueOffsetX = (float)FastTrig.cos(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetX - (float)FastTrig.sin(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetY;
               float trueOffsetY = (float)FastTrig.sin(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetX + (float)FastTrig.cos(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetY;
               MagicRender.battlespace(Global.getSettings().getSprite(ship.getHullSpec().getSpriteName()), new Vector2f(ship.getLocation().getX() + trueOffsetX, ship.getLocation().getY() + trueOffsetY), new Vector2f(0.0F, 0.0F), new Vector2f(ship.getSpriteAPI().getWidth(), ship.getSpriteAPI().getHeight()), new Vector2f(0.0F, 0.0F), ship.getFacing() - 90.0F, 0.0F, AFTERIMAGE_COLOR, true, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F, 0.1F, 1.0F, CombatEngineLayers.BELOW_SHIPS_LAYER);
            }
         }

      }
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      ShipAPI ship = null;
      boolean player = false;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         player = ship == Global.getCombatEngine().getPlayerShip();
         id = id + "_" + ship.getId();
         Global.getCombatEngine().getTimeMult().unmodify(id);
         stats.getTimeMult().unmodify(id);
         stats.getAcceleration().unmodify(id);
         stats.getDeceleration().unmodify(id);
         stats.getMaxSpeed().unmodify(id);
         stats.getTurnAcceleration().unmodify(id);
         stats.getMaxTurnRate().unmodify(id);
      }
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      return null;
   }

   public float getActiveOverride(ShipAPI ship) {
      return -1.0F;
   }

   public float getInOverride(ShipAPI ship) {
      return -1.0F;
   }

   public float getOutOverride(ShipAPI ship) {
      return -1.0F;
   }

   public float getRegenOverride(ShipAPI ship) {
      return -1.0F;
   }

   public int getUsesOverride(ShipAPI ship) {
      return -1;
   }
}
