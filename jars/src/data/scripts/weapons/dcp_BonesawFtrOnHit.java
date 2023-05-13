package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

import org.lwjgl.util.vector.Vector2f;

import java.util.Random;
import java.awt.Color;

public class dcp_BonesawFtrOnHit implements OnHitEffectPlugin
{

    // Declare important values as constants so that our
    // code isn't littered with magic numbers. If we want to
    // re-use this effect, we can easily just copy this class
    // and tweak some of these constants to get a similar effect.
    // == explosions =========================================================
    // these explosions are created every time a projectile impacts on hull or
    // armour, regardless of whether or not we spawn an EMP arc.
    // -- explosion bonus damage ---------------------------------------------
    // this extra damage is always dealt to hull/armour regardless of whether
    // or not we make an EMP arc.
    private static final int EXPLOSION_DAMAGE_MIN = 0;
    private static final int EXPLOSION_DAMAGE_MAX = 20;
    // amount of EMP damage dealt by the explosion
    private static final float EXPLOSION_EMP = 0f;

    // -- explosion graphics -------------------------------------------------
    // color of the explosion
    private static final Color EXPLOSION_COLOR = new Color(255,235,200,200);
    // radius of the explosion
    private static final float EXPLOSION_RADIUS = 18f;
    // how long the explosion lingers for
    private static final float EXPLOSION_DURATION = 0.07f;
    
    // placeholder, please change this once you have a nice explosion sound :)
    private static final String SFX = "magellan_bonesaw_ftr_crit";

    // == don't mess with this stuff =========================================
    private static Random rng = new Random();

    // @Inline
    private static float explosionDamage()
    {
        return (float) (rng.nextInt(
                (EXPLOSION_DAMAGE_MAX - EXPLOSION_DAMAGE_MIN) + 1)
                + EXPLOSION_DAMAGE_MIN);
    }

    @Override
    public void onHit(DamagingProjectileAPI projectile,
            CombatEntityAPI target,
            Vector2f point,
            boolean shieldHit,
            ApplyDamageResultAPI damageResult, 
            CombatEngineAPI engine)
    {

        // check whether we've hit armour/hull
        if (target instanceof ShipAPI && !shieldHit)
        {
            // -- make an explosion ------------------------------------------
            // apply the extra damage to the target
            engine.applyDamage(target, point, // where to apply damage
                    explosionDamage(), // amount of damage
                    DamageType.HIGH_EXPLOSIVE, // damage type
                    EXPLOSION_EMP, // amount of EMP damage
                    false, // does this bypass shields? (no)
                    false, // does this deal soft flux? (no)
                    projectile.getSource());
            // get the target's velocity to render the crit FX
            Vector2f v_target = new Vector2f(target.getVelocity());

            // do visual effects
            engine.spawnExplosion(point, v_target,
                    EXPLOSION_COLOR, // color of the explosion
                    EXPLOSION_RADIUS,
                    EXPLOSION_DURATION
            );
            //play a sound
            Global.getSoundPlayer().playSound(SFX, 1f, 1f, target.getLocation(), target.getVelocity());
        }
    }

}