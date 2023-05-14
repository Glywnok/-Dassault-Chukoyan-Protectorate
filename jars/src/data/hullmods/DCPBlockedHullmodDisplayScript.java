package data.hullmods;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import java.util.List;
import org.apache.log4j.Logger;

public class DCPBlockedHullmodDisplayScript extends BaseEveryFrameCombatPlugin implements EveryFrameScript {
   private static final Logger Log = Logger.getLogger(data.hullmods.DCPBlockedHullmodDisplayScript.class);
   private static final String NOTIFICATION_HULLMOD = "DMEBlockedBlankHullmod";
   private static final String NOTIFICATION_SOUND = "cr_allied_critical";
   private static ShipAPI ship;

   public static void showBlocked(ShipAPI blocked) {
      stopDisplaying();
      ship = blocked;
      ship.getVariant().addMod("DMEBlockedBlankHullmod");
      Global.getSoundPlayer().playUISound("cr_allied_critical", 1.0F, 1.0F);
   }

   public boolean isDone() {
      return false;
   }

   public boolean runWhilePaused() {
      return true;
   }

   public static void stopDisplaying() {
      if (ship != null) {
         Log.debug("Removed from existing ship.");
         ship.getVariant().removeMod("DMEBlockedBlankHullmod");
         ship = null;
      }

   }

   public void advance(float amount) {
      stopDisplaying();
   }

   public void advance(float amount, List<InputEventAPI> events) {
      stopDisplaying();
   }

   public void init(CombatEngineAPI engine) {
      if (Global.getSettings().getCurrentState() != GameState.TITLE) {
         stopDisplaying();
         Global.getCombatEngine().removePlugin(this);
      }

   }
}
