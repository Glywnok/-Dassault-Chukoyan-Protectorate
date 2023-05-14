package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class dcp_DME_StrikeCommandStats extends BaseShipSystemScript {
   public static final Object KEY_JITTER = new Object();
   public static final float SPEED_INCREASE_PERCENT = 33.0F;
   public static final float MANEUVER_INCREASE_PERCENT = 10.0F;
   public static final float AUTOFIRE_BONUS = 60.0F;
   public static final Color JITTER_COLOR = new Color(45, 75, 255, 255);
   public static final Color JITTER_UNDER_COLOR = new Color(45, 75, 255, 125);

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      ShipAPI ship = null;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         if (effectLevel > 0.0F) {
            float jitterLevel = effectLevel;
            float maxRangeBonus = 5.0F;
            float jitterRangeBonus = effectLevel * maxRangeBonus;
            Iterator var9 = this.getFighters(ship).iterator();

            while(var9.hasNext()) {
               ShipAPI fighter = (ShipAPI)var9.next();
               if (!fighter.isHulk()) {
                  MutableShipStatsAPI fStats = fighter.getMutableStats();
                  fStats.getMaxSpeed().modifyPercent(id, 33.0F * effectLevel);
                  fStats.getAcceleration().modifyPercent(id, 33.0F * effectLevel);
                  fStats.getAutofireAimAccuracy().modifyFlat(id, 0.59999996F);
                  fStats.getMaxRecoilMult().modifyMult(id, 0.40000004F);
                  fStats.getRecoilPerShotMult().modifyMult(id, 0.40000004F);
                  fStats.getDeceleration().modifyPercent(id, 10.0F * effectLevel);
                  fStats.getMaxTurnRate().modifyPercent(id, 10.0F * effectLevel);
                  fStats.getTurnAcceleration().modifyPercent(id, 10.0F * effectLevel);
                  if (jitterLevel > 0.0F) {
                     fighter.getEngineController().fadeToOtherColor(this, new Color(145, 175, 255, 255), new Color(45, 75, 255, 25), jitterLevel, 1.0F);
                     fighter.getEngineController().extendFlame(this, 0.6F, 0.15F, 0.6F);
                     fighter.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 3, 0.0F, jitterRangeBonus);
                     fighter.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 1, 0.0F, 0.0F + jitterRangeBonus * 1.0F);
                     Global.getSoundPlayer().playLoop("system_targeting_feed_loop", ship, 1.0F, 1.0F, fighter.getLocation(), fighter.getVelocity());
                  }
               }
            }
         }

      }
   }

   private List<ShipAPI> getFighters(ShipAPI carrier) {
      List<ShipAPI> result = new ArrayList();
      Iterator var3 = Global.getCombatEngine().getShips().iterator();

      while(var3.hasNext()) {
         ShipAPI ship = (ShipAPI)var3.next();
         if (ship.isFighter() && ship.getWing() != null && ship.getWing().getSourceShip() == carrier) {
            result.add(ship);
         }
      }

      return result;
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      ShipAPI ship = null;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         Iterator var4 = this.getFighters(ship).iterator();

         while(var4.hasNext()) {
            ShipAPI fighter = (ShipAPI)var4.next();
            if (!fighter.isHulk()) {
               MutableShipStatsAPI fStats = fighter.getMutableStats();
               fStats.getBallisticWeaponDamageMult().unmodify(id);
               fStats.getEnergyWeaponDamageMult().unmodify(id);
               fStats.getMissileWeaponDamageMult().unmodify(id);
            }
         }

      }
   }

   public StatusData getStatusData(int index, State state, float effectLevel) {
      float percent = 33.0F * effectLevel;
      if (index == 0) {
         return new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(33.0F * effectLevel) + "% more fighter top speed", false);
      } else if (index == 1) {
         return new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(10.0F * effectLevel) + "% more fighter agility", false);
      } else {
         return index == 2 ? new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(60.0F * effectLevel) + "% more fighter accuracy", false) : null;
      }
   }
}
