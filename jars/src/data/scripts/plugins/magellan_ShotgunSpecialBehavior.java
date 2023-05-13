package data.scripts.plugins;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class magellan_ShotgunSpecialBehavior extends BaseEveryFrameCombatPlugin {

    private static final Set<String> SHOTGUNPROJ_IDS1 = new HashSet<>(1);
    private static final Set<String> SHOTGUNPROJ_IDS2 = new HashSet<>(1);
    private static final Set<String> SHOTGUNPROJ_IDS3 = new HashSet<>(1);
    
    static {
        //add Projectile IDs here.
        SHOTGUNPROJ_IDS1.add("magellan_beehive_shot");
        SHOTGUNPROJ_IDS2.add("magellan_medkinetic_shot");
        SHOTGUNPROJ_IDS3.add("magellan_bonecracker_shot");
    }

    private CombatEngineAPI engine;

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (engine == null) {
            return;
        }
        if (engine.isPaused()) {
            return;
        }

        List<DamagingProjectileAPI> projectiles = engine.getProjectiles();
        int size = projectiles.size();
        for (int i = 0; i < size; i++) {
            DamagingProjectileAPI proj = projectiles.get(i);
            String spec = proj.getProjectileSpecId();

            //Set Scatter Blaster properties here.  
//            if (SHOTGUNPROJ_IDS1.contains(spec)) {
//                Vector2f loc = proj.getLocation();
//                Vector2f vel = proj.getVelocity();
//                int shotCount = (8);
//                for (int j = 0; j < shotCount; j++) {
//                    Vector2f randomVel = MathUtils.getRandomPointOnCircumference(null, MathUtils.getRandomNumberInRange(
//                                                                                 30f, 120f));
//                    randomVel.x += vel.x;
//                    randomVel.y += vel.y;
//                    //spec + "_clone" means this will call the weapon (not projectile! you need a separate weapon) with the id "($projectilename)_clone".
//                    engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "magellan_scatterblaster_sub", loc, proj.getFacing(),
//                                           randomVel);
//                }
//                engine.removeEntity(proj);
//            }
            
            //Set advanced laser shotgun properties here. Core shot does 250 damage, scatter does 50 each.
            if (SHOTGUNPROJ_IDS1.contains(spec)) {
                Vector2f loc = proj.getLocation();
                Vector2f vel = proj.getVelocity();
                int shotCount1 = (5); //Scatter projectiles
                for (int j = 0; j < shotCount1; j++) {
                    Vector2f randomVel = MathUtils.getRandomPointOnCircumference(null, MathUtils.getRandomNumberInRange(
                                                                                 30f, 60f));
                    randomVel.x += vel.x;
                    randomVel.y += vel.y;
                    //spec + "_clone" means this will call the weapon (not projectile! you need a separate weapon) with the id "($projectilename)_clone".
                    engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "magellan_beehive_sub", loc, proj.getFacing(),
                                           randomVel);
                }
                int shotCount2 = (1); //Core projectile
                for (int j = 0; j < shotCount2; j++) {
                    engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "magellan_beehive_core", loc, proj.getFacing(),
                                           null);
                }
                engine.removeEntity(proj); //messy way to do this is just to not remove the original and deal with fucked-up stats, but we're better than that.
            }
            
            //Set Auto Shotgun properties here.  
            if (SHOTGUNPROJ_IDS2.contains(spec)) {
                Vector2f loc = proj.getLocation();
                Vector2f vel = proj.getVelocity();
                int shotCount = (10);
                for (int j = 0; j < shotCount; j++) {
                    Vector2f randomVel = MathUtils.getRandomPointOnCircumference(null, MathUtils.getRandomNumberInRange(
                                                                                 12f, 60f));
                    randomVel.x += vel.x;
                    randomVel.y += vel.y;
                    //spec + "_clone" means this will call the weapon (not projectile! you need a separate weapon) with the id "($projectilename)_clone".
                    engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "magellan_autoshotgun_sub", loc, proj.getFacing(),
                                           randomVel);
                }
                engine.removeEntity(proj);
            }
            
            //Set Bonecracker properties here.
            if (SHOTGUNPROJ_IDS3.contains(spec)) {
                Vector2f loc = proj.getLocation();
                Vector2f vel = proj.getVelocity();
                int shotCount1 = (5); //Scatter projectiles
                for (int j = 0; j < shotCount1; j++) {
                    Vector2f randomVel = MathUtils.getRandomPointOnCircumference(null, MathUtils.getRandomNumberInRange(
                                                                                 15f, 45f));
                    randomVel.x += vel.x;
                    randomVel.y += vel.y;
                    //spec + "_clone" means this will call the weapon (not projectile! you need a separate weapon) with the id "($projectilename)_clone".
                    engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "magellan_bonecracker_sub", loc, proj.getFacing(),
                                           randomVel);
                }
                int shotCount2 = (1); //Core projectile
                for (int j = 0; j < shotCount2; j++) {
                    engine.spawnProjectile(proj.getSource(), proj.getWeapon(), "magellan_bonecracker_core", loc, proj.getFacing(),
                                           null);
                }
                engine.removeEntity(proj); //messy way to do this is just to not remove the original and deal with fucked-up stats, but we're better than that.
            }
        }
    }

    @Override
    public void init(CombatEngineAPI engine) {
        this.engine = engine;
    }
}
