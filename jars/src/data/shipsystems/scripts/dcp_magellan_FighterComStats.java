package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class dcp_magellan_FighterComStats extends BaseShipSystemScript {
   public static final Object KEY_JITTER = new Object();
   public static final float MAX_DAMAGE_REDUCTION_BONUS = 0.05F;
   public static final float ARMOR_DAMAGE_REDUCTION = 20.0F;
   public static final float DAMAGE_INCREASE_PERCENT = 20.0F;
   public static final float MANEUVER_INCREASE_PERCENT = 10.0F;
   public static final float AUTOFIRE_BONUS = 20.0F;
   public static final Color JITTER_COLOR = new Color(175, 155, 95, 155);
   public static final Color JITTER_UNDER_COLOR = new Color(175, 155, 95, 155);

   private String getString(String key) {
      return Global.getSettings().getString("System", "magellan_" + key);
   }

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
                  fStats.getMaxArmorDamageReduction().modifyPercent(id, 0.05F * effectLevel);
                  fStats.getArmorDamageTakenMult().modifyMult(id, 1.0F - 0.2F * effectLevel);
                  fStats.getBallisticWeaponDamageMult().modifyMult(id, 1.0F + 0.19999999F * effectLevel);
                  fStats.getEnergyWeaponDamageMult().modifyMult(id, 1.0F + 0.19999999F * effectLevel);
                  fStats.getMissileWeaponDamageMult().modifyMult(id, 1.0F + 0.19999999F * effectLevel);
                  fStats.getAutofireAimAccuracy().modifyFlat(id, 0.19999999F);
                  fStats.getMaxRecoilMult().modifyMult(id, 0.8F);
                  fStats.getRecoilPerShotMult().modifyMult(id, 0.8F);
                  fStats.getDeceleration().modifyPercent(id, 10.0F * effectLevel);
                  fStats.getMaxTurnRate().modifyPercent(id, 10.0F * effectLevel);
                  fStats.getTurnAcceleration().modifyPercent(id, 10.0F * effectLevel);
                  if (jitterLevel > 0.0F) {
                     fighter.setWeaponGlow(effectLevel, Misc.setAlpha(JITTER_UNDER_COLOR, 90), EnumSet.allOf(WeaponType.class));
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
      if (index == 0) {
         return new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(20.0F * effectLevel) + this.getString("fightercom_str1"), false);
      } else if (index == 1) {
         return new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(20.0F * effectLevel) + this.getString("fightercom_str2"), false);
      } else if (index == 2) {
         return new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(10.0F * effectLevel) + this.getString("fightercom_str3"), false);
      } else {
         return index == 3 ? new StatusData("" + Misc.getRoundedValueMaxOneAfterDecimal(20.0F * effectLevel) + this.getString("fightercom_str4"), false) : null;
      }
   }
}
