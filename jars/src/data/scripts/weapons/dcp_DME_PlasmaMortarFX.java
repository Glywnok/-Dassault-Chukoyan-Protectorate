package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DMEUtils;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PlasmaMortarFX implements EveryFrameWeaponEffectPlugin {
   private static final float OFFSET = 10.0F;
   private static final Color FLASH_COLOR = new Color(175, 125, 255, 255);
   private float last_charge_level = 0.0F;
   private int last_weapon_ammo = 0;

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         float charge_level = weapon.getChargeLevel();
         int weapon_ammo = weapon.getAmmo();
         if (charge_level > this.last_charge_level || weapon_ammo < this.last_weapon_ammo) {
            Vector2f weapon_location = weapon.getLocation();
            ShipAPI ship = weapon.getShip();
            float ship_facing = ship.getFacing();
            Vector2f ship_velocity = ship.getVelocity();
            MathUtils.getPointOnCircumference(weapon_location, 10.0F, ship_facing);
            if (weapon_ammo < this.last_weapon_ammo) {
               Vector2f explosion_offset = DMEUtils.translate_polar(weapon_location, 13.0F, weapon.getCurrAngle());
               engine.spawnExplosion(explosion_offset, ship.getVelocity(), FLASH_COLOR, 40.0F, 0.07F);
            }
         }

         this.last_charge_level = charge_level;
         this.last_weapon_ammo = weapon_ammo;
      }
   }
}
