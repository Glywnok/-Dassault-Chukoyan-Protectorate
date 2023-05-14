package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PulseripperFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   public static final int REPLACE_EVERY_MIN = 3;
   public static final int REPLACE_EVERY_MAX = 5;
   public static final String REPLACE_WPN_ID = "istl_pulseripper_large";
   private static final Color FLASH_CORE = new Color(183, 95, 70, 255);
   private static final Color FLASH_FRINGE = new Color(183, 95, 70, 155);
   private static final float FLASH_SIZE = 20.0F;
   private static final float FLASH_DUR = 0.2F;
   private int roundCounter = 0;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ++this.roundCounter;
      int projreplace = MathUtils.getRandomNumberInRange(3, 5);
      Vector2f ship_velocity;
      if (this.roundCounter >= projreplace) {
         this.roundCounter = 0;
         Vector2f loc = proj.getLocation();
         ship_velocity = proj.getVelocity();
         DamagingProjectileAPI newProj = (DamagingProjectileAPI)engine.spawnProjectile(weapon.getShip(), weapon, "istl_pulseripper_large", loc, proj.getFacing(), weapon.getShip().getVelocity());
         Global.getCombatEngine().removeEntity(proj);
      }

      ShipAPI ship = weapon.getShip();
      ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addSmoothParticle(proj_location, ship_velocity, 3.0F, 1.0F, 0.3F, 0.15F, FLASH_CORE);
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_FRINGE, 20.0F, 0.2F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
