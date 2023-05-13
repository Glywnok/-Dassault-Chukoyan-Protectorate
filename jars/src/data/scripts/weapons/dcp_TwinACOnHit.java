package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

public class dcp_TwinACOnHit implements OnHitEffectPlugin
{
    private static final Color EXPLOSION_COLOR = new Color(235,235,255,255);
    private static final float FLUXRAISE = 100f;

    @Override
    public void onHit(DamagingProjectileAPI projectile,
            CombatEntityAPI target,
            Vector2f point,
            boolean shieldHit,
            ApplyDamageResultAPI damageResult, 
            CombatEngineAPI engine)
    {

        // check whether or not you're hitting hull
        if (!shieldHit && target instanceof ShipAPI) {
            ShipAPI targetship = (ShipAPI) target;
            
            // get the target's velocity to render the crit FX
            Vector2f v_target = new Vector2f(target.getVelocity());

            // do visual effects
            engine.spawnExplosion(point, v_target,
                    EXPLOSION_COLOR, // color of the explosion
                    30f, // sets the size of the explosion
                    0.1f // how long the explosion lingers for
            );
            //Raise target ship flux on hull hit
            targetship.getFluxTracker().increaseFlux(FLUXRAISE, true);
        }
    }

}
