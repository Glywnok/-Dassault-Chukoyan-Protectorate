package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_CIWS_SmTracerFX implements OnFireEffectPlugin, EveryFrameWeaponEffectPlugin {
   public static final int TRACER_EVERY = 4;
   public static final String TRACER_WPN_ID = "magellan_flenser_tracer";
   private int roundCounter = 0;

   public void onFire(DamagingProjectileAPI proj, WeaponAPI weapon, CombatEngineAPI engine) {
      ++this.roundCounter;
      if (this.roundCounter >= 4) {
         this.roundCounter = 0;
         Vector2f loc = proj.getLocation();
         Vector2f vel = proj.getVelocity();
         DamagingProjectileAPI newProj = (DamagingProjectileAPI)engine.spawnProjectile(weapon.getShip(), weapon, "magellan_flenser_tracer", loc, proj.getFacing(), weapon.getShip().getVelocity());
         Global.getCombatEngine().removeEntity(proj);
      }

   }

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
   }
}
