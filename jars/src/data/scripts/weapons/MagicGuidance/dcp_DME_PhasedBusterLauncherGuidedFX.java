package data.scripts.weapons.MagicGuidance;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DCPUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_PhasedBusterLauncherGuidedFX implements EveryFrameWeaponEffectPlugin {
   private static final float OFFSET = 5.0F;
   private static final Color FLASH_COLOR = new Color(185, 175, 100, 255);
   private static final float FLASH_SIZE = 135.0F;
   private float last_charge_level = 0.0F;
   private int last_weapon_ammo = 0;
   private List<DamagingProjectileAPI> alreadyRegisteredProjectiles = new ArrayList();

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused()) {
         float charge_level = weapon.getChargeLevel();
         int weapon_ammo = weapon.getAmmo();
         ShipAPI target;
         if (charge_level > this.last_charge_level || weapon_ammo < this.last_weapon_ammo) {
            Vector2f weapon_location = weapon.getLocation();
            target = weapon.getShip();
            float ship_facing = target.getFacing();
            Vector2f ship_velocity = target.getVelocity();
            MathUtils.getPointOnCircumference(weapon_location, 5.0F, ship_facing);
            if (weapon_ammo < this.last_weapon_ammo) {
               Vector2f explosion_offset = DCPUtils.translate_polar(weapon_location, 8.0F, weapon.getCurrAngle());
               engine.spawnExplosion(explosion_offset, target.getVelocity(), FLASH_COLOR, 135.0F, 0.1F);
            }
         }

         this.last_charge_level = charge_level;
         this.last_weapon_ammo = weapon_ammo;
         ShipAPI source = weapon.getShip();
         target = null;
         if (source.getWeaponGroupFor(weapon) != null) {
            if (source.getWeaponGroupFor(weapon).isAutofiring() && source.getSelectedGroupAPI() != source.getWeaponGroupFor(weapon)) {
               target = source.getWeaponGroupFor(weapon).getAutofirePlugin(weapon).getTargetShip();
            } else {
               target = source.getShipTarget();
            }
         }

         Iterator var13 = CombatUtils.getProjectilesWithinRange(weapon.getLocation(), 200.0F).iterator();

         while(var13.hasNext()) {
            DamagingProjectileAPI proj = (DamagingProjectileAPI)var13.next();
            if (proj.getWeapon() == weapon && !this.alreadyRegisteredProjectiles.contains(proj) && engine.isEntityInPlay(proj) && !proj.didDamage()) {
               engine.addPlugin(new dcp_DME_BusterGuidanceProjScript(proj, target));
               this.alreadyRegisteredProjectiles.add(proj);
            }
         }

         List<DamagingProjectileAPI> cloneList = new ArrayList(this.alreadyRegisteredProjectiles);
         Iterator var16 = cloneList.iterator();

         while(true) {
            DamagingProjectileAPI proj;
            do {
               if (!var16.hasNext()) {
                  return;
               }

               proj = (DamagingProjectileAPI)var16.next();
            } while(engine.isEntityInPlay(proj) && !proj.didDamage());

            this.alreadyRegisteredProjectiles.remove(proj);
         }
      }
   }
}
