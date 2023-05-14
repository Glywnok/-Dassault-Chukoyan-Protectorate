package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_BonecrusherFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_BRIGHT = new Color(255, 235, 200, 255);
   private static final Color FLASH_DIM = new Color(255, 235, 200, 155);
   private static final float FLASH_SIZE = 4.0F;
   private static final float FLASH_DUR = 0.2F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addSmoothParticle(proj_location, ship_velocity, 40.0F, 1.0F, 0.3F, 0.1F, FLASH_BRIGHT);
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_DIM, 4.0F, 0.2F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
