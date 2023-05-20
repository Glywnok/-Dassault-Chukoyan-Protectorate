package data.campaign.procgen.themes;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.FleetEncounterContextPlugin.DataForEncounterSide;
import com.fs.starfarer.api.campaign.FleetEncounterContextPlugin.FleetMemberData;
import com.fs.starfarer.api.campaign.ai.CampaignFleetAIAPI;
import com.fs.starfarer.api.combat.BattleCreationContext;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.FleetEncounterContext;
import com.fs.starfarer.api.impl.campaign.FleetInteractionDialogPluginImpl.FIDConfig;
import com.fs.starfarer.api.impl.campaign.FleetInteractionDialogPluginImpl.FIDConfigGen;
import com.fs.starfarer.api.impl.campaign.FleetInteractionDialogPluginImpl.FIDDelegate;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.fleets.SeededFleetManager;
import com.fs.starfarer.api.impl.campaign.fleets.SeededFleetManager.SeededFleet;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.SalvageEntityGenDataSpec.DropData;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.SalvageEntity;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.lwjgl.util.vector.Vector2f;

public class BladeBreakerSeededFleetManager extends SeededFleetManager {
   protected int minPts;
   protected int maxPts;
   protected float activeChance;

   public BladeBreakerSeededFleetManager(StarSystemAPI system, int minFleets, int maxFleets, int minPts, int maxPts, float activeChance) {
      super(system, 1.0F);
      this.minPts = minPts;
      this.maxPts = maxPts;
      this.activeChance = activeChance;
      int num = minFleets + StarSystemGenerator.random.nextInt(maxFleets - minFleets + 1);

      for(int i = 0; i < num; ++i) {
         long seed = StarSystemGenerator.random.nextLong();
         this.addSeed(seed);
      }

   }

   protected CampaignFleetAPI spawnFleet(long seed) {
      Random random = new Random(seed);
      int combatPoints = this.minPts + random.nextInt(this.maxPts - this.minPts + 1);
      String type = "patrolSmall";
      if (combatPoints > 8) {
         type = "patrolMedium";
      }

      if (combatPoints > 16) {
         type = "patrolLarge";
      }

      combatPoints = (int)((float)combatPoints * 8.0F);
      FleetParamsV3 params = new FleetParamsV3(this.system.getLocation(), "blade_breakers", 1.0F, type, (float)combatPoints, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      params.withOfficers = false;
      params.random = random;
      CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
      if (fleet == null) {
         return null;
      } else {
         this.system.addEntity(fleet);
         fleet.setFacing(random.nextFloat() * 360.0F);
         boolean dormant = random.nextFloat() >= this.activeChance;
         int numActive = 0;
         Iterator var10 = this.fleets.iterator();

         while(var10.hasNext()) {
            SeededFleet f = (SeededFleet)var10.next();
            if (f.fleet != null) {
               ++numActive;
            }
         }

         if (numActive == 0 && this.activeChance > 0.0F) {
            dormant = false;
         }

         initBladeBreakerFleetProperties(random, fleet, dormant);
         if (dormant) {
            SectorEntityToken target = pickEntityToGuard(random, this.system, fleet);
            if (target != null) {
               fleet.setCircularOrbit(target, random.nextFloat() * 360.0F, fleet.getRadius() + target.getRadius() + 100.0F + 100.0F * random.nextFloat(), 25.0F + 5.0F * random.nextFloat());
            } else {
               Vector2f loc = Misc.getPointAtRadius(new Vector2f(), 4000.0F, random);
               fleet.setLocation(loc.x, loc.y);
            }
         } else {
            fleet.addScript(new BladeBreakerAssignmentAI(fleet, this.system, (SectorEntityToken)null));
         }

         return fleet;
      }
   }

   public static SectorEntityToken pickEntityToGuard(Random random, StarSystemAPI system, CampaignFleetAPI fleet) {
      WeightedRandomPicker<SectorEntityToken> picker = new WeightedRandomPicker(random);

      Iterator var4;
      SectorEntityToken entity;
      float w;
      for(var4 = system.getEntitiesWithTag("salvageable").iterator(); var4.hasNext(); picker.add(entity, w)) {
         entity = (SectorEntityToken)var4.next();
         w = 1.0F;
         if (entity.hasTag("neutrino_high")) {
            w = 3.0F;
         }

         if (entity.hasTag("neutrino_low")) {
            w = 0.33F;
         }
      }

      var4 = system.getJumpPoints().iterator();

      while(var4.hasNext()) {
         entity = (SectorEntityToken)var4.next();
         picker.add(entity, 1.0F);
      }

      return (SectorEntityToken)picker.pick();
   }

   public static void initBladeBreakerFleetProperties(Random random, CampaignFleetAPI fleet, boolean dormant) {
      if (random == null) {
         new Random();
      }

      fleet.removeAbility("emergency_burn");
      fleet.removeAbility("sensor_burst");
      fleet.removeAbility("go_dark");
      fleet.getMemoryWithoutUpdate().set("$sawPlayerTransponderOn", true);
      fleet.getMemoryWithoutUpdate().set("$isPatrol", true);
      fleet.getMemoryWithoutUpdate().set("$cfai_longPursuit", true);
      if (dormant) {
      }

      fleet.getMemoryWithoutUpdate().set("$cfai_noJump", true);
      if (dormant) {
         fleet.setTransponderOn(false);
         fleet.getMemoryWithoutUpdate().set("$cfai_makeAllowDisengage", true);
         fleet.setAI((CampaignFleetAIAPI)null);
         fleet.setNullAIActionText("lurking");
      }

      addBladeBreakerInteractionConfig(fleet);
   }

   public static void addBladeBreakerInteractionConfig(CampaignFleetAPI fleet) {
      fleet.getMemoryWithoutUpdate().set("$fidConifgGen", new BladeBreakerSeededFleetManager.BladeBreakerFleetInteractionConfigGen());
   }

   public static void addBladeBreakerLootDrops(Random random, CampaignFleetAPI fleet, float mult) {
      long salvageSeed = random.nextLong();
      fleet.getMemoryWithoutUpdate().set("$salvageSeed", salvageSeed);
      int[] counts = new int[5];
      String[] groups = new String[]{"breaker_fighters", "dcp_DME_sigma_matter1", "breaker_weapons_small", "breaker_weapons_medium", "ai_cores3"};
      Iterator var7 = fleet.getFleetData().getMembersListCopy().iterator();

      while(var7.hasNext()) {
         FleetMemberAPI member = (FleetMemberAPI)var7.next();
         if (member.isCapital()) {
            counts[3] += 2;
         } else {
            int var10002;
            if (member.isCruiser()) {
               var10002 = counts[3]++;
            } else if (member.isDestroyer()) {
               var10002 = counts[2]++;
            } else if (member.isFrigate()) {
               var10002 = counts[0]++;
            }
         }
      }

      if (fleet.isStationMode()) {
         counts[4] += 10;
      }

      for(int i = 0; i < counts.length; ++i) {
         int count = counts[i];
         if (count > 0) {
            DropData d = new DropData();
            d.group = groups[i];
            d.chances = (int)Math.ceil((double)((float)count * mult));
            fleet.addDropRandom(d);
         }
      }

   }

   public static class BladeBreakerFleetInteractionConfigGen implements FIDConfigGen {
      public FIDConfig createConfig() {
         FIDConfig config = new FIDConfig();
         config.showTransponderStatus = false;
         config.delegate = new FIDDelegate() {
            public void postPlayerSalvageGeneration(InteractionDialogAPI dialog, FleetEncounterContext context, CargoAPI salvage) {
               if (dialog.getInteractionTarget() instanceof CampaignFleetAPI) {
                  CampaignFleetAPI fleet = (CampaignFleetAPI)dialog.getInteractionTarget();
                  DataForEncounterSide data = context.getDataFor(fleet);
                  List<FleetMemberAPI> losses = new ArrayList();
                  Iterator var7 = data.getOwnCasualties().iterator();

                  while(var7.hasNext()) {
                     FleetMemberData fmd = (FleetMemberData)var7.next();
                     losses.add(fmd.getMember());
                  }

                  List<DropData> dropRandom = new ArrayList();
                  int[] counts = new int[5];
                  String[] groups = new String[]{"dcp_DME_sigma_matter2", "breaker_weapons_small", "breaker_weapons_medium", "breaker_weapons_large", "ai_cores3"};
                  Iterator var10 = losses.iterator();

                  while(var10.hasNext()) {
                     FleetMemberAPI member = (FleetMemberAPI)var10.next();
                     if (member.isStation()) {
                        counts[4] += 10;
                     }

                     if (member.isCapital()) {
                        counts[3] += 2;
                     } else {
                        int var10002;
                        if (member.isCruiser()) {
                           var10002 = counts[3]++;
                        } else if (member.isDestroyer()) {
                           var10002 = counts[2]++;
                        } else if (member.isFrigate()) {
                           var10002 = counts[0]++;
                        }
                     }
                  }

                  for(int i = 0; i < counts.length; ++i) {
                     int count = counts[i];
                     if (count > 0) {
                        DropData d = new DropData();
                        d.group = groups[i];
                        d.chances = (int)Math.ceil((double)((float)count * 1.0F));
                        dropRandom.add(d);
                     }
                  }

                  Random salvageRandom = new Random(Misc.getSalvageSeed(fleet));
                  CargoAPI extra = SalvageEntity.generateSalvage(salvageRandom, 1.0F, 1.0F, 1.0F, 1.0F, (List)null, dropRandom);
                  Iterator var20 = extra.getStacksCopy().iterator();

                  while(var20.hasNext()) {
                     CargoStackAPI stack = (CargoStackAPI)var20.next();
                     salvage.addFromStack(stack);
                  }

               }
            }

            public void notifyLeave(InteractionDialogAPI dialog) {
            }

            public void battleContextCreated(InteractionDialogAPI dialog, BattleCreationContext bcc) {
               bcc.aiRetreatAllowed = false;
               bcc.objectivesAllowed = false;
            }
         };
         return config;
      }
   }
}
