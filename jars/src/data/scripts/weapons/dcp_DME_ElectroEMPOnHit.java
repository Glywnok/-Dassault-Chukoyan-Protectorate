package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_ElectroEMPOnHit implements OnHitEffectPlugin {
   private static final int MIN_ARCS = 2;
   private static final int MAX_ARCS = 4;
   private static final float ARC_DAMAGE = 0.25F;
   private static final float ARC_EMP = 0.6F;
   private static final String SFX = "istl_energy_crit";
   private static final float FLUXRAISE_MULT = 1.0F;
   private static final Color NEBULA_COLOR = new Color(75, 90, 255, 200);
   private static final float NEBULA_SIZE = 7.0F * (0.75F + (float)Math.random() * 0.5F);
   private static final float NEBULA_SIZE_MULT = 20.0F;
   private static final float NEBULA_DUR = 0.6F;
   private static final float NEBULA_RAMPUP = 0.12F;
   private static final Color EXPLOSION_COLOR = new Color(125, 135, 255, 255);
   private static final float EXPLOSION_DUR_MULT = 0.75F;

   public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
      Vector2f v_target = new Vector2f(target.getVelocity());
      engine.addNebulaParticle(point, v_target, NEBULA_SIZE, 20.0F, 0.12F, 0.3F, 0.6F, NEBULA_COLOR, true);
      engine.spawnExplosion(point, v_target, EXPLOSION_COLOR, NEBULA_SIZE * 3.0F, 0.45000002F);
      if (!shieldHit && target instanceof ShipAPI) {
         ShipAPI targetship = (ShipAPI)target;
         float dam = projectile.getDamageAmount() * 0.25F;
         float emp = projectile.getEmpAmount() * 0.6F;
         int arcs = MathUtils.getRandomNumberInRange(2, 4);

         for(int i = 0; i < arcs; ++i) {
            engine.spawnEmpArc(projectile.getSource(), point, target, target, DamageType.ENERGY, dam, emp, 100000.0F, "tachyon_lance_emp_impact", 25.0F, new Color(50, 55, 155, 255), new Color(200, 220, 255, 255));
         }

         Global.getSoundPlayer().playSound("istl_energy_crit", 1.0F, 1.0F, target.getLocation(), target.getVelocity());
         float fluxmult = projectile.getDamageAmount() * 1.0F;
         float maxflux = targetship.getMaxFlux();
         if ((double)maxflux > (double)fluxmult * 1.5D) {
            targetship.getFluxTracker().increaseFlux(fluxmult, true);
         }
      }

   }
}
