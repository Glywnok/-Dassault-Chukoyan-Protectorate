package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_QuenchGunFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(200, 200, 255, 155);
   private static final float FLASH_SIZE = 36.0F;
   private static final Color NEBULA_COLOR = new Color(175, 175, 225, 200);
   private static final float NEBULA_SIZE = 7.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 9.0F;
   private static final float NEBULA_DUR = 0.6F;
   private static final float NEBULA_RAMPUP = 0.1F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addNebulaParticle(proj_location, ship_velocity, NEBULA_SIZE, 9.0F, 0.1F, 0.2F, 0.6F, NEBULA_COLOR, true);
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_COLOR, 36.0F, 0.2F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
