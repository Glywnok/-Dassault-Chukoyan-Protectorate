package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_FusionBlasterFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color FLASH_COLOR = new Color(240, 30, 90, 155);
   private static final float FLASH_SIZE = 36.0F;
   private static final Color NEBULA_COLOR = new Color(240, 30, 90, 255);
   private static final float NEBULA_SIZE = 6.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 12.0F;
   private static final float NEBULA_DUR = 0.8F;
   private static final float NEBULA_RAMPUP = 0.2F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addSwirlyNebulaParticle(proj_location, ship_velocity, NEBULA_SIZE, 12.0F, 0.2F, 0.2F, 0.8F, NEBULA_COLOR, true);
      engine.spawnExplosion(proj_location, ship_velocity, FLASH_COLOR, 36.0F, 0.2F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
