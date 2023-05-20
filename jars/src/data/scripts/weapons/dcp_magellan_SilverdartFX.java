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

public class dcp_magellan_SilverdartFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   public static final int REPLACE_EVERY_MIN = 4;
   public static final int REPLACE_EVERY_MAX = 8;
   public static final String REPLACE_WPN_ID = "dcp_magellan_silverdart_emp";
   private static final Color FLASH_CORE = new Color(25, 170, 245, 125);
   private static final Color FLASH_FRINGE = new Color(125, 215, 245, 100);
   private static final float FLASH_SIZE = 1.0F;
   private static final float FLASH_DUR = 0.1F;
   private int roundCounter = 0;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ++this.roundCounter;
      int projreplace = MathUtils.getRandomNumberInRange(4, 8);
      Vector2f ship_velocity;
      if (this.roundCounter >= projreplace) {
         this.roundCounter = 0;
         Vector2f loc = proj.getLocation();
         ship_velocity = proj.getVelocity();
         DamagingProjectileAPI newProj = (DamagingProjectileAPI)engine.spawnProjectile(weapon.getShip(), weapon, "dcp_magellan_silverdart_emp", loc, proj.getFacing(), weapon.getShip().getVelocity());
         Global.getCombatEngine().removeEntity(proj);
      }

      ShipAPI ship = weapon.getShip();
      ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addSmoothParticle(proj_location, ship_velocity, 25.0F, 1.0F, 0.3F, 0.075F, FLASH_CORE);
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_FRINGE, 1.0F, 0.1F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
