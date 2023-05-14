package data.scripts.weapons.MagicGuidance;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DMEUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_ThermalMortarGuidedFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final float OFFSET = 7.0F;
   private static final Color BOOM_COLOR = new Color(175, 100, 255, 255);
   private static final Color NEBULA_COLOR = new Color(155, 75, 255, 255);
   private static final float NEBULA_SIZE = 5.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 12.0F;
   private static final float NEBULA_DUR = 0.6F;
   private static final float NEBULA_RAMPUP = 0.1F;
   private List<DamagingProjectileAPI> alreadyRegisteredProjectiles = new ArrayList();

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      float ship_facing = ship.getFacing();
      Vector2f ship_velocity = ship.getVelocity();
      MathUtils.getPointOnCircumference(weapon_location, 7.0F, ship_facing);
      Vector2f explosion_offset = DMEUtils.translate_polar(weapon_location, 10.0F, weapon.getCurrAngle());
      engine.addSwirlyNebulaParticle(explosion_offset, ship_velocity, NEBULA_SIZE, 12.0F, 0.1F, 0.2F, 0.6F, NEBULA_COLOR, true);
      engine.addSmoothParticle(explosion_offset, ship_velocity, NEBULA_SIZE * 4.0F, 0.75F, 0.1F, 0.3F, BOOM_COLOR);
      engine.spawnExplosion(explosion_offset, ship_velocity, BOOM_COLOR, NEBULA_SIZE * 6.0F, 0.45000002F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      ShipAPI source = weapon.getShip();
      ShipAPI target = null;
      if (source.getWeaponGroupFor(weapon) != null) {
         if (source.getWeaponGroupFor(weapon).isAutofiring() && source.getSelectedGroupAPI() != source.getWeaponGroupFor(weapon)) {
            target = source.getWeaponGroupFor(weapon).getAutofirePlugin(weapon).getTargetShip();
         } else {
            target = source.getShipTarget();
         }
      }

      Iterator var6 = CombatUtils.getProjectilesWithinRange(weapon.getLocation(), 200.0F).iterator();

      while(var6.hasNext()) {
         DamagingProjectileAPI proj = (DamagingProjectileAPI)var6.next();
         if (proj.getWeapon() == weapon && !this.alreadyRegisteredProjectiles.contains(proj) && engine.isEntityInPlay(proj) && !proj.didDamage()) {
            engine.addPlugin(new dcp_DME_MortarGuidanceProjScript(proj, target));
            this.alreadyRegisteredProjectiles.add(proj);
         }
      }

      List<DamagingProjectileAPI> cloneList = new ArrayList(this.alreadyRegisteredProjectiles);
      Iterator var10 = cloneList.iterator();

      while(true) {
         DamagingProjectileAPI proj;
         do {
            if (!var10.hasNext()) {
               return;
            }

            proj = (DamagingProjectileAPI)var10.next();
         } while(engine.isEntityInPlay(proj) && !proj.didDamage());

         this.alreadyRegisteredProjectiles.remove(proj);
      }
   }
}
