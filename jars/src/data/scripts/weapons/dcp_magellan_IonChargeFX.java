package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DMEUtils;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_IonChargeFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color BRIGHT_COLOR = new Color(120, 180, 210, 175);
   private static final Color DIM_COLOR = new Color(60, 90, 120, 125);
   private static final float FLASH_SIZE = 12.0F;
   private static final float FLASH_DUR = 0.1F;
   private static final float OFFSET = 11.0F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      Vector2f explosion_offset = DMEUtils.translate_polar(weapon_location, 18.0F, weapon.getCurrAngle());
      Vector2f explosion_offset2 = DMEUtils.translate_polar(weapon_location, 14.0F, weapon.getCurrAngle());
      engine.spawnExplosion(explosion_offset, ship.getVelocity(), DIM_COLOR, 12.0F, 0.1F);
      engine.spawnExplosion(explosion_offset2, ship.getVelocity(), BRIGHT_COLOR, 6.0F, 0.060000002F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
