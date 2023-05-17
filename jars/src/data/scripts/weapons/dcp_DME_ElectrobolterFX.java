package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.DCPUtils;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_ElectrobolterFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final float OFFSET = 28.0F;
   private static final Color NEBULA_COLOR = new Color(100, 110, 255, 255);
   private static final float NEBULA_SIZE = 6.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 10.0F;
   private static final float NEBULA_DUR = 0.5F;
   private static final float NEBULA_RAMPUP = 0.1F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      float ship_facing = ship.getFacing();
      Vector2f ship_velocity = ship.getVelocity();
      MathUtils.getPointOnCircumference(weapon_location, 28.0F, ship_facing);
      Vector2f explosion_offset = DCPUtils.translate_polar(weapon_location, 35.0F, weapon.getCurrAngle());
      Vector2f explosion_offset2 = DCPUtils.translate_polar(weapon_location, 31.0F, weapon.getCurrAngle());
      engine.addSwirlyNebulaParticle(explosion_offset, ship_velocity, NEBULA_SIZE, 10.0F, 0.1F, 0.2F, 0.5F, NEBULA_COLOR, true);
      engine.addSmoothParticle(explosion_offset, ship_velocity, NEBULA_SIZE * 2.0F, 1.0F, 0.1F, 0.25F, NEBULA_COLOR);
      engine.spawnExplosion(explosion_offset2, ship.getVelocity(), NEBULA_COLOR, NEBULA_SIZE * 2.0F, 0.3F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
