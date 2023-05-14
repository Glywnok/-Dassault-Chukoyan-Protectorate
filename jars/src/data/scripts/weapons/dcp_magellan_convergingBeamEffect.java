package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_convergingBeamEffect implements EveryFrameWeaponEffectPlugin {
   public static final float ANGLE_MAX_LARGE = 30.0F;
   public static final float ANGLE_MAX_MEDIUM = 20.0F;
   public static final float ANGLE_MAX_SMALL = 12.0F;
   public static final float ANGLE_MIN_LARGE = 3.0F;
   public static final float ANGLE_MIN_MEDIUM = 2.0F;
   public static final float ANGLE_MIN_SMALL = 1.0F;
   public static final float ROTATION_SPEED = 1.8F;
   private float counter = 0.0F;

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      if (!engine.isPaused() && weapon != null) {
         this.counter += amount * 1.8F;
         boolean physicalOffset = true;
         float angleMax = 30.0F;
         float angleMin = 3.0F;
         if (weapon.getSize() == WeaponSize.MEDIUM) {
            physicalOffset = false;
            angleMax = 20.0F;
            angleMin = 2.0F;
         } else if (weapon.getSize() == WeaponSize.SMALL) {
            physicalOffset = false;
            angleMax = 12.0F;
            angleMin = 1.0F;
         }

         int startMod = 0;
         if (weapon.getSpec().getTags().contains("MG_BEAM_FULL_CONVERGE_OFFSET")) {
            angleMin = 0.0F;
         } else if (weapon.getSpec().getTags().contains("MG_BEAM_NEGATIVE_CONVERGE_OFFSET")) {
            angleMin = -0.2F;
         } else if (weapon.getSpec().getTags().contains("MG_BEAM_CENTER_BEAM")) {
            startMod = 1;
         }

         int i;
         float angleToSet;
         Vector2f offsetToSet;
         for(i = 0; i < weapon.getSpec().getHardpointAngleOffsets().size() - startMod; ++i) {
            angleToSet = (float)Math.sin((double)this.counter + (double)(i * 2) * 3.141592653589793D / (double)(weapon.getSpec().getHardpointAngleOffsets().size() - startMod));
            if (physicalOffset) {
               offsetToSet = (Vector2f)weapon.getSpec().getHardpointFireOffsets().get(i);
               offsetToSet.y = angleToSet * 6.0F;
               weapon.getSpec().getHardpointFireOffsets().set(i, offsetToSet);
            }

            angleToSet *= angleMin * weapon.getChargeLevel() + angleMax * (1.0F - weapon.getChargeLevel());
            weapon.getSpec().getHardpointAngleOffsets().set(i, angleToSet);
         }

         for(i = 0; i < weapon.getSpec().getHiddenAngleOffsets().size() - startMod; ++i) {
            angleToSet = (float)Math.sin((double)this.counter + (double)(i * 2) * 3.141592653589793D / (double)(weapon.getSpec().getHiddenAngleOffsets().size() - startMod));
            if (physicalOffset) {
               offsetToSet = (Vector2f)weapon.getSpec().getHiddenFireOffsets().get(i);
               offsetToSet.y = angleToSet * 6.0F;
               weapon.getSpec().getHiddenFireOffsets().set(i, offsetToSet);
            }

            angleToSet *= angleMin * weapon.getChargeLevel() + angleMax * (1.0F - weapon.getChargeLevel());
            weapon.getSpec().getHiddenAngleOffsets().set(i, angleToSet);
         }

         for(i = 0; i < weapon.getSpec().getTurretAngleOffsets().size() - startMod; ++i) {
            angleToSet = (float)Math.sin((double)this.counter + (double)(i * 2) * 3.141592653589793D / (double)(weapon.getSpec().getTurretAngleOffsets().size() - startMod));
            if (physicalOffset) {
               offsetToSet = (Vector2f)weapon.getSpec().getTurretFireOffsets().get(i);
               offsetToSet.y = angleToSet * 6.0F;
               weapon.getSpec().getTurretFireOffsets().set(i, offsetToSet);
            }

            angleToSet *= angleMin * weapon.getChargeLevel() + angleMax * (1.0F - weapon.getChargeLevel());
            weapon.getSpec().getTurretAngleOffsets().set(i, angleToSet);
         }

      }
   }
}
