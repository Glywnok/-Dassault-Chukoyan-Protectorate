package data.campaign.intel.bases;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.Tuning;
import com.fs.starfarer.api.impl.campaign.intel.BaseEventManager;
import com.fs.starfarer.api.impl.campaign.intel.bases.PirateBaseIntel;
import com.fs.starfarer.api.impl.campaign.intel.bases.PirateBaseIntel.PirateBaseTier;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import java.util.Iterator;
import java.util.Random;

public class magellan_HerdBaseManager extends BaseEventManager {
   public static final String KEY = "$core_dcp_magellan_HerdBaseManager";
   public static final float CHECK_DAYS = 10.0F;
   public static final float CHECK_PROB = 0.5F;
   protected long start = 0L;
   protected float extraDays = 0.0F;
   protected int numDestroyed = 0;
   protected int numSpawnChecksToSkip = 0;
   protected Random random = new Random();
   public static String RECENTLY_USED_FOR_BASE = "$core_recentlyUsedForBase";

   public static magellan_HerdBaseManager getInstance() {
      Object test = Global.getSector().getMemoryWithoutUpdate().get("$core_dcp_magellan_HerdBaseManager");
      return (magellan_HerdBaseManager)test;
   }

   public magellan_HerdBaseManager() {
      Global.getSector().getMemoryWithoutUpdate().set("$core_dcp_magellan_HerdBaseManager", this);
      this.start = Global.getSector().getClock().getTimestamp();
   }

   protected int getMinConcurrent() {
      return Global.getSettings().getInt("minHerdBases");
   }

   protected int getMaxConcurrent() {
      return Global.getSettings().getInt("maxHerdBases");
   }

   protected float getBaseInterval() {
      return 10.0F;
   }

   public void advance(float amount) {
      super.advance(amount);
   }

   protected EveryFrameScript createEvent() {
      if (this.numSpawnChecksToSkip > 0) {
         --this.numSpawnChecksToSkip;
         return null;
      } else if (this.random.nextFloat() < 0.5F) {
         return null;
      } else {
         StarSystemAPI system = this.pickSystemForHerdBase();
         if (system == null) {
            return null;
         } else {
            PirateBaseTier tier = this.pickTier();
            String factionId = this.pickPirateFaction();
            if (factionId == null) {
               return null;
            } else {
               PirateBaseIntel intel = new PirateBaseIntel(system, factionId, tier);
               if (intel.isDone()) {
                  intel = null;
               }

               return intel;
            }
         }
      }
   }

   public String pickPirateFaction() {
      WeightedRandomPicker<String> picker = new WeightedRandomPicker(this.random);
      Iterator var2 = Global.getSector().getAllFactions().iterator();

      while(var2.hasNext()) {
         FactionAPI faction = (FactionAPI)var2.next();
         if (faction.getCustomBoolean("makesHerdBases")) {
            picker.add(faction.getId(), 1.0F);
         }
      }

      return (String)picker.pick();
   }

   public float getUnadjustedDaysSinceStart() {
      float days = Global.getSector().getClock().getElapsedDaysSince(this.start);
      return days;
   }

   public float getDaysSinceStart() {
      float days = Global.getSector().getClock().getElapsedDaysSince(this.start) + this.extraDays;
      if (Misc.isFastStartExplorer()) {
         days += Tuning.FAST_START_EXTRA_DAYS - 30.0F;
      } else if (Misc.isFastStart()) {
         days += Tuning.FAST_START_EXTRA_DAYS + 60.0F;
      }

      return days;
   }

   public float getStandardTimeFactor() {
      float timeFactor = (getInstance().getDaysSinceStart() - Tuning.FAST_START_EXTRA_DAYS) / Tuning.DAYS_UNTIL_FULL_TIME_FACTOR;
      if (timeFactor < 0.0F) {
         timeFactor = 0.0F;
      }

      if (timeFactor > 1.0F) {
         timeFactor = 1.0F;
      }

      return timeFactor;
   }

   public float getExtraDays() {
      return this.extraDays;
   }

   public void setExtraDays(float extraDays) {
      this.extraDays = extraDays;
   }

   protected PirateBaseTier pickTier() {
      float days = this.getDaysSinceStart();
      days += (float)(this.numDestroyed * 200);
      WeightedRandomPicker<PirateBaseTier> picker = new WeightedRandomPicker();
      if (days < 360.0F) {
         picker.add(PirateBaseTier.TIER_1_1MODULE, 10.0F);
         picker.add(PirateBaseTier.TIER_2_1MODULE, 10.0F);
      } else if (days < 720.0F) {
         picker.add(PirateBaseTier.TIER_2_1MODULE, 10.0F);
         picker.add(PirateBaseTier.TIER_3_2MODULE, 10.0F);
      } else if (days < 1080.0F) {
         picker.add(PirateBaseTier.TIER_3_2MODULE, 10.0F);
         picker.add(PirateBaseTier.TIER_4_3MODULE, 10.0F);
      } else {
         picker.add(PirateBaseTier.TIER_3_2MODULE, 10.0F);
         picker.add(PirateBaseTier.TIER_4_3MODULE, 10.0F);
         picker.add(PirateBaseTier.TIER_5_3MODULE, 10.0F);
      }

      return (PirateBaseTier)picker.pick();
   }

   public static float genBaseUseTimeout() {
      return 120.0F + 60.0F * (float)Math.random();
   }

   public static void markRecentlyUsedForBase(StarSystemAPI system) {
      if (system != null && system.getCenter() != null) {
         system.getCenter().getMemoryWithoutUpdate().set(RECENTLY_USED_FOR_BASE, true, genBaseUseTimeout());
      }

   }

   protected StarSystemAPI pickSystemForHerdBase() {
      WeightedRandomPicker<StarSystemAPI> far = new WeightedRandomPicker(this.random);
      WeightedRandomPicker<StarSystemAPI> picker = new WeightedRandomPicker(this.random);
      Iterator var3 = Global.getSector().getStarSystems().iterator();

      while(var3.hasNext()) {
         StarSystemAPI system = (StarSystemAPI)var3.next();
         if (!system.hasPulsar()) {
            float days = Global.getSector().getClock().getElapsedDaysSince(system.getLastPlayerVisitTimestamp());
            if (!(days < 45.0F) && !system.getCenter().getMemoryWithoutUpdate().contains(RECENTLY_USED_FOR_BASE)) {
               float weight = 0.0F;
               if (system.hasTag("theme_misc_skip")) {
                  weight = 1.0F;
               } else if (system.hasTag("theme_misc")) {
                  weight = 3.0F;
               } else if (system.hasTag("theme_remnant_no_fleets")) {
                  weight = 3.0F;
               } else if (system.hasTag("theme_ruins")) {
                  weight = 5.0F;
               } else if (system.hasTag("theme_core_unpopulated")) {
                  weight = 1.0F;
               }

               if (!(weight <= 0.0F)) {
                  float usefulStuff = (float)(system.getCustomEntitiesWithTag("objective").size() + system.getCustomEntitiesWithTag("stable_location").size());
                  if (!(usefulStuff <= 0.0F) && !Misc.hasPulsar(system) && Misc.getMarketsInLocation(system).size() <= 0) {
                     float dist = system.getLocation().length();
                     float distMult = 1.0F;
                     if (dist > 36000.0F) {
                        far.add(system, weight * usefulStuff * distMult);
                     } else {
                        picker.add(system, weight * usefulStuff * distMult);
                     }
                  }
               }
            }
         }
      }

      if (picker.isEmpty()) {
         picker.addAll(far);
      }

      return (StarSystemAPI)picker.pick();
   }

   public int getNumDestroyed() {
      return this.numDestroyed;
   }

   public void setNumDestroyed(int numDestroyed) {
      this.numDestroyed = numDestroyed;
   }

   public void incrDestroyed() {
      ++this.numDestroyed;
      this.numSpawnChecksToSkip = Math.max(this.numSpawnChecksToSkip, (Tuning.PIRATE_BASE_MIN_TIMEOUT_MONTHS + Misc.random.nextInt(Tuning.PIRATE_BASE_MAX_TIMEOUT_MONTHS - Tuning.PIRATE_BASE_MIN_TIMEOUT_MONTHS + 1)) * 3);
   }
}
