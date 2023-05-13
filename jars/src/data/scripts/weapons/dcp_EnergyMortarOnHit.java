package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

import org.lwjgl.util.vector.Vector2f;

import org.lazywizard.lazylib.MathUtils;

import java.awt.Color;

public class dcp_EnergyMortarOnHit implements OnHitEffectPlugin
{
    // -- crit damage -------------------------------------------------------
    //private static final int CRIT_DAMAGE_MIN = 50;
    //private static final int CRIT_DAMAGE_MAX = 100;
    private static final float CRIT_DAMAGE_MIN_MULT = 0.25f;
    private static final float CRIT_DAMAGE_MAX_MULT = 0.5f; 
    private static final float CRIT_CHANCE = 1.0f;
    //  -- crit fx ----------------------------------------------------------
    private static final Color EXPLOSION_COLOR = new Color(50,255,150,200);
    private static final float EXPLOSION_SIZE = 60f;
    private static final float EXPLOSION_DUR = 0.2f;
    //  -- nebula particles -------------------------------------------------
    private static final Color NEBULA_COLOR = new Color(25,255,155,255);
    private static final float NEBULA_SIZE = 9f * (0.75f + (float) Math.random() * 0.5f);
    private static final float NEBULA_SIZE_MULT = 15f;
    private static final float NEBULA_DUR = 0.8f;
    private static final float NEBULA_RAMPUP = 0.2f;
    // -- stuff for tweaking particle characteristics -----------------------
    private static final Color BRIGHT_COLOR = new Color(155,255,225,255);
    private static final Color DIM_COLOR = new Color(0,100,100,30);
    private static final float PARTICLE_SIZE = 5f;
    private static final float PARTICLE_BRIGHTNESS = 255f;
    private static final float PARTICLE_DURATION = 2f;
    private static final int PARTICLE_COUNT = 2;
    // -- particle geometry -------------------------------------------------
    private static final float CONE_ANGLE = 150f;
    private static final float VEL_MIN = 0.16f;
    private static final float VEL_MAX = 0.3f;
    // one half of the angle. used internally, don't mess with this
    private static final float A_2 = CONE_ANGLE / 2;

    @Override
    public void onHit(DamagingProjectileAPI projectile,
            CombatEntityAPI target,
            Vector2f point,
            boolean shieldHit,
            ApplyDamageResultAPI damageResult, 
            CombatEngineAPI engine)
    {
        // check whether or not we want to apply critical damage
        if (target instanceof ShipAPI && !shieldHit && Math.random() <= CRIT_CHANCE)
        {
            //calculate the crit min/max damage based on projectile damage.
            float critminmult = projectile.getDamageAmount() * CRIT_DAMAGE_MIN_MULT;
            float critmaxmult = projectile.getDamageAmount() * CRIT_DAMAGE_MAX_MULT;
            // apply the extra damage to the target
            engine.applyDamage(target, point, // where to apply damage
                    MathUtils.getRandomNumberInRange(
                            critminmult, critmaxmult),
                    DamageType.HIGH_EXPLOSIVE, // damage type
                    0f, // amount of EMP damage (none)
                    false, // does this bypass shields? (no)
                    false, // does this deal soft flux? (no)
                    projectile.getSource());
        }
        // get the target's velocity to render the crit FX
        Vector2f v_target = new Vector2f(target.getVelocity());        
        // do visual effects ---------------------------------------------
        engine.spawnExplosion(
            point,
            v_target,
            EXPLOSION_COLOR, // color of the explosion
            EXPLOSION_SIZE, // sets the size of the explosion
            EXPLOSION_DUR // how long the explosion lingers for
        );
        engine.addNebulaParticle(
            point,
            v_target,
            NEBULA_SIZE,
            NEBULA_SIZE_MULT,
            NEBULA_RAMPUP,
            0.25f,
            NEBULA_DUR,
            NEBULA_COLOR,
            true
        );
        engine.addHitParticle(
            point,
            v_target,
            EXPLOSION_SIZE * 2,
            1f,
            EXPLOSION_DUR / 3,
            EXPLOSION_COLOR
        );    
        float speed = projectile.getVelocity().length();
        float facing = projectile.getFacing();
        for (int i = 0; i <= PARTICLE_COUNT; i++)
        {
            float angle = MathUtils.getRandomNumberInRange(facing - A_2,
                    facing + A_2);
            float vel = MathUtils.getRandomNumberInRange(speed * -VEL_MIN,
                    speed * -VEL_MAX);
            Vector2f vector = MathUtils.getPointOnCircumference(null,
                    vel,
                    angle);
                engine.addHitParticle(point,
                        vector,
                        PARTICLE_SIZE,
                        PARTICLE_BRIGHTNESS,
                        PARTICLE_DURATION,
                        BRIGHT_COLOR
                );
                engine.addHitParticle(point,
                        vector,
                        PARTICLE_SIZE * 5,
                        PARTICLE_BRIGHTNESS,
                        PARTICLE_DURATION * 0.75f,
                        DIM_COLOR
                );
        }
    }
}
