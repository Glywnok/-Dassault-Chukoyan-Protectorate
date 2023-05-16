package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.util.HashMap;
import java.util.Map;

public class dcp_DME_DamperStats extends BaseShipSystemScript {
   private static Map mag = new HashMap();
   protected Object STATUSKEY1 = new Object();

   public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
      effectLevel = 1.0F;
      float mult = (Float)mag.get(HullSize.CRUISER);
      if (stats.getVariant() != null) {
         mult = (Float)mag.get(stats.getVariant().getHullSize());
      }

      stats.getHullDamageTakenMult().modifyMult(id, 1.0F - (1.0F - mult) * effectLevel);
      stats.getArmorDamageTakenMult().modifyMult(id, 1.0F - (1.0F - mult) * effectLevel);
      stats.getEmpDamageTakenMult().modifyMult(id, 1.0F - (1.0F - mult) * effectLevel);
      ShipAPI ship = null;
      boolean player = false;
      if (stats.getEntity() instanceof ShipAPI) {
         ship = (ShipAPI)stats.getEntity();
         player = ship == Global.getCombatEngine().getPlayerShip();
      }

      if (player) {
         ShipSystemAPI system = getDamper(ship);
         if (system != null) {
            float percent = (1.0F - mult) * effectLevel * 100.0F;
            Global.getCombatEngine().maintainStatusForPlayerShip(this.STATUSKEY1, system.getSpecAPI().getIconSpriteName(), system.getDisplayName(), Math.round(percent) + "% less damage taken", false);
         }
      }

   }

   public static ShipSystemAPI getDamper(ShipAPI ship) {
      ShipSystemAPI system = ship.getSystem();
      return system != null && system.getId().equals("dcp_DME_damper") ? system : ship.getPhaseCloak();
   }

   public void unapply(MutableShipStatsAPI stats, String id) {
      stats.getHullDamageTakenMult().unmodify(id);
      stats.getArmorDamageTakenMult().unmodify(id);
      stats.getEmpDamageTakenMult().unmodify(id);
   }

   static {
      mag.put(HullSize.FIGHTER, 0.25F);
      mag.put(HullSize.FRIGATE, 0.25F);
      mag.put(HullSize.DESTROYER, 0.25F);
      mag.put(HullSize.CRUISER, 0.4F);
      mag.put(HullSize.CAPITAL_SHIP, 0.4F);
   }
}
