package data.scripts.world;

import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import java.util.Random;

public class SigmaUtils {
   Random characterSaveSeed;
   Random random;
   float selector;
   public static final String ISTL_GUARDIAN_ACOLYTE = "dcp_DME_guardian_turret_std";
   public static final String ISTL_GUARDIAN_DMG = "dcp_DME_bbsuperheavy_dmg";
   public static final String ISTL_GUARDIAN_STD = "dcp_DME_bbsuperheavy_std";
   public static int level = 20;
   public static float radius_star = 300.0F;
   public static float radius_station = 3000.0F;
   public static float radius_variation = 1000.0F;

   public SigmaUtils() {
      this.characterSaveSeed = StarSystemGenerator.random;
      this.random = new Random(this.characterSaveSeed.nextLong());
      this.selector = this.random.nextFloat();
   }
}
