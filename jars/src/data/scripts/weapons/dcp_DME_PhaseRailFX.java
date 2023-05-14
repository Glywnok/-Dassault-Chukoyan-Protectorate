package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DMEUtils;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PhaseRailFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final float OFFSET = 17.0F;
   private static final Color FLASH_COLOR = new Color(75, 125, 200, 255);
   private static final float FLASH_SIZE = 105.0F;
   private static final float FLASH_DUR = 0.3F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      float ship_facing = ship.getFacing();
      Vector2f ship_velocity = ship.getVelocity();
      MathUtils.getPointOnCircumference(weapon_location, 17.0F, ship_facing);
      Vector2f explosion_offset = DMEUtils.translate_polar(weapon_location, 20.0F, weapon.getCurrAngle());
      engine.spawnExplosion(explosion_offset, ship.getVelocity(), FLASH_COLOR, 105.0F, 0.3F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
