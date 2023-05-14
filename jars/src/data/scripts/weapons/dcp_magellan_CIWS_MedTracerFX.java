package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_CIWS_MedTracerFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   public static final int TRACER_EVERY = 4;
   public static final String TRACER_WPN_ID = "magellan_grinder_tracer";
   private static final Color FLASH_CORE = new Color(255, 225, 165, 125);
   private static final Color FLASH_FRINGE = new Color(215, 175, 115, 75);
   private static final float FLASH_SIZE = 1.0F;
   private static final float FLASH_DUR = 0.1F;
   private int roundCounter = 0;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ++this.roundCounter;
      Vector2f ship_velocity;
      if (this.roundCounter >= 4) {
         this.roundCounter = 0;
         Vector2f loc = proj.getLocation();
         ship_velocity = proj.getVelocity();
         DamagingProjectileAPI newProj = (DamagingProjectileAPI)engine.spawnProjectile(weapon.getShip(), weapon, "magellan_grinder_tracer", loc, proj.getFacing(), weapon.getShip().getVelocity());
         Global.getCombatEngine().removeEntity(proj);
      }

      ShipAPI ship = weapon.getShip();
      ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addSmoothParticle(proj_location, ship_velocity, 25.0F, 1.0F, 0.3F, 0.05F, FLASH_CORE);
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_FRINGE, 1.0F, 0.1F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
