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

public class dcp_DME_SigmaBusterLaunchFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final float OFFSET = 0.0F;
   private static final Color BOOM_COLOR = new Color(50, 225, 200, 255);
   private static final Color NEBULA_COLOR = new Color(75, 255, 175, 255);
   private static final float NEBULA_SIZE = 7.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 15.0F;
   private static final float NEBULA_DUR = 0.45F;
   private static final float NEBULA_RAMPUP = 0.1F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      Vector2f weapon_location = weapon.getLocation();
      ShipAPI ship = weapon.getShip();
      float ship_facing = ship.getFacing();
      Vector2f ship_velocity = ship.getVelocity();
      MathUtils.getPointOnCircumference(weapon_location, 0.0F, ship_facing);
      Vector2f explosion_offset = DMEUtils.translate_polar(weapon_location, 3.0F, weapon.getCurrAngle());
      engine.addSwirlyNebulaParticle(explosion_offset, ship_velocity, NEBULA_SIZE, 15.0F, 0.1F, 0.2F, 0.45F, NEBULA_COLOR, true);
      engine.addSmoothParticle(explosion_offset, ship_velocity, NEBULA_SIZE * 4.0F, 0.75F, 0.1F, 0.1125F, BOOM_COLOR);
      engine.spawnExplosion(explosion_offset, ship_velocity, BOOM_COLOR, NEBULA_SIZE * 6.0F, 0.225F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
