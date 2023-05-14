package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_ShellbackFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   private static final Color NEBULA_COLOR = new Color(100, 110, 255, 255);
   private static final float NEBULA_SIZE = 3.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 10.0F;
   private static final float NEBULA_DUR = 0.4F;
   private static final float NEBULA_RAMPUP = 0.1F;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ShipAPI ship = weapon.getShip();
      Vector2f ship_velocity = ship.getVelocity();
      Vector2f proj_location = proj.getLocation();
      engine.addSwirlyNebulaParticle(proj_location, ship_velocity, NEBULA_SIZE, 10.0F, 0.1F, 0.2F, 0.4F, NEBULA_COLOR, true);
      engine.addSmoothParticle(proj_location, ship_velocity, NEBULA_SIZE * 2.0F, 0.75F, 0.1F, 0.2F, NEBULA_COLOR);
      engine.spawnExplosion(proj_location, ship_velocity, NEBULA_COLOR, NEBULA_SIZE * 2.5F, 0.24000001F);
   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
