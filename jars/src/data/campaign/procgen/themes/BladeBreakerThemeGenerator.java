package data.campaign.procgen.themes;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.JumpPointAPI.JumpDestination;
import com.fs.starfarer.api.campaign.ai.CampaignFleetAIAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.BattleCreationContext;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.FleetEncounterContext;
import com.fs.starfarer.api.impl.campaign.FleetInteractionDialogPluginImpl.BaseFIDDelegate;
import com.fs.starfarer.api.impl.campaign.FleetInteractionDialogPluginImpl.FIDConfig;
import com.fs.starfarer.api.impl.campaign.FleetInteractionDialogPluginImpl.FIDConfigGen;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.procgen.Constellation;
import com.fs.starfarer.api.impl.campaign.procgen.DefenderDataOverride;
import com.fs.starfarer.api.impl.campaign.procgen.NameAssigner;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.procgen.themes.ThemeGenContext;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.AddedEntity;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.EntityLocation;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.HabitationLevel;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.StarSystemData;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner.SpecialCreationContext;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.lwjgl.util.vector.Vector2f;

public class BladeBreakerThemeGenerator extends BaseThemeGenerator {
   public String getThemeId() {
      return "breakers";
   }

   public void generateForSector(ThemeGenContext context, float allowedUnusedFraction) {
      float total = (float)(context.constellations.size() - context.majorThemes.size()) * allowedUnusedFraction;
      if (!(total <= 0.0F)) {
         int MIN_CONSTELLATIONS_WITH_BREAKERS = 5;
         int MAX_CONSTELLATIONS_WITH_BREAKERS = 7;
         float CONSTELLATION_SKIP_PROB = 0.75F;
         if (Global.getSettings().getModManager().isModEnabled("Adjusted Sector")) {
            MIN_CONSTELLATIONS_WITH_BREAKERS = Global.getSettings().getInt("BladeBreakerThemed_ConstellationMin_AS");
            MAX_CONSTELLATIONS_WITH_BREAKERS = Global.getSettings().getInt("BladeBreakerThemed_ConstellationMax_AS");
            CONSTELLATION_SKIP_PROB = Global.getSettings().getFloat("BladeBreakerThemed_ConstellationSkipProb_AS");
         }

         int num = (int)StarSystemGenerator.getNormalRandom((float)MIN_CONSTELLATIONS_WITH_BREAKERS, (float)MAX_CONSTELLATIONS_WITH_BREAKERS);
         if ((float)num > total) {
            num = (int)total;
         }

         int numDestroyed = (int)((float)num * (0.23F + 0.1F * this.random.nextFloat()));
         if (numDestroyed < 1) {
            numDestroyed = 1;
         }

         int numSuppressed = (int)((float)num * (0.23F + 0.1F * this.random.nextFloat()));
         if (numSuppressed < 1) {
            numSuppressed = 1;
         }

         float suppressedStationMult = 0.5F;
         int suppressedStations = (int)Math.ceil((double)((float)numSuppressed * suppressedStationMult));
         WeightedRandomPicker<Boolean> addSuppressedStation = new WeightedRandomPicker(this.random);

         for(int i = 0; i < numSuppressed; ++i) {
            if (i < suppressedStations) {
               addSuppressedStation.add(true, 1.0F);
            } else {
               addSuppressedStation.add(false, 1.0F);
            }
         }

         List<Constellation> constellations = this.getSortedAvailableConstellations(context, false, new Vector2f(), (List)null);
         Collections.reverse(constellations);
         float skipProb = CONSTELLATION_SKIP_PROB;
         if (total < (float)num / (1.0F - CONSTELLATION_SKIP_PROB)) {
            skipProb = 1.0F - (float)num / total;
         }

         List<StarSystemData> breakerSystems = new ArrayList();
         if (DEBUG) {
            System.out.println("\n\n\n");
         }

         if (DEBUG) {
            System.out.println("Generating Blade Breaker systems");
         }

         int count = 0;
         int numUsed = 0;

         label181:
         for(int i = 0; i < num && i < constellations.size(); ++i) {
            Constellation c = (Constellation)constellations.get(i);
            if (this.random.nextFloat() < skipProb) {
               if (DEBUG) {
                  System.out.println("Skipping constellation " + c.getName());
               }
            } else {
               List<StarSystemData> systems = new ArrayList();
               Iterator var21 = c.getSystems().iterator();

               while(var21.hasNext()) {
                  StarSystemAPI system = (StarSystemAPI)var21.next();
                  StarSystemData data = computeSystemData(system);
                  systems.add(data);
               }

               List<StarSystemData> mainCandidates = this.getSortedSystemsSuitedToBePopulated(systems);
               int numMain = 1 + this.random.nextInt(2);
               if (numMain > mainCandidates.size()) {
                  numMain = mainCandidates.size();
               }

               if (numMain <= 0) {
                  if (DEBUG) {
                     System.out.println("Skipping constellation " + c.getName() + ", no suitable main candidates");
                  }
               } else {
                  BladeBreakerThemeGenerator.BladeBreakerSystemType type = BladeBreakerThemeGenerator.BladeBreakerSystemType.RESURGENT;
                  if (numUsed < numDestroyed) {
                     type = BladeBreakerThemeGenerator.BladeBreakerSystemType.DESTROYED;
                  } else if (numUsed < numDestroyed + numSuppressed) {
                     type = BladeBreakerThemeGenerator.BladeBreakerSystemType.SUPPRESSED;
                  }

                  context.majorThemes.put(c, "breakers");
                  ++numUsed;
                  if (DEBUG) {
                     System.out.println("Generating " + numMain + " main systems in " + c.getName());
                  }

                  StarSystemData data;
                  for(int j = 0; j < numMain; ++j) {
                     data = (StarSystemData)mainCandidates.get(j);
                     this.populateMain(data, type);
                     data.system.addTag("theme_interesting");
                     data.system.addTag("theme_breakers");
                     if (type != BladeBreakerThemeGenerator.BladeBreakerSystemType.DESTROYED) {
                        data.system.addTag("theme_unsafe");
                     }

                     data.system.addTag("theme_breakers_main");
                     data.system.addTag(type.getTag());
                     breakerSystems.add(data);
                     if (!NameAssigner.isNameSpecial(data.system)) {
                        NameAssigner.assignSpecialNames(data.system);
                     }

                     BladeBreakerSeededFleetManager fleets;
                     if (type == BladeBreakerThemeGenerator.BladeBreakerSystemType.DESTROYED) {
                        fleets = new BladeBreakerSeededFleetManager(data.system, 3, 8, 1, 2, 0.05F);
                        data.system.addScript(fleets);
                     } else if (type == BladeBreakerThemeGenerator.BladeBreakerSystemType.SUPPRESSED) {
                        fleets = new BladeBreakerSeededFleetManager(data.system, 7, 12, 4, 12, 0.25F);
                        data.system.addScript(fleets);
                        Boolean addStation = this.random.nextFloat() < suppressedStationMult;
                        if (j == 0 && !addSuppressedStation.isEmpty()) {
                           addSuppressedStation.pickAndRemove();
                        }

                        if (addStation) {
                           List<CampaignFleetAPI> stations = this.addBattlestations(data, 1.0F, 1, 1, this.createStringPicker(new Object[]{"istl_guardian_turret_dmg", 10.0F}));
                           Iterator var44 = stations.iterator();

                           while(var44.hasNext()) {
                              CampaignFleetAPI station = (CampaignFleetAPI)var44.next();
                              int maxFleets = 2 + this.random.nextInt(3);
                              BladeBreakerStationFleetManager activeFleets = new BladeBreakerStationFleetManager(station, 1.0F, 0, maxFleets, 20.0F, 6, 12);
                              data.system.addScript(activeFleets);
                           }
                        }
                     } else if (type == BladeBreakerThemeGenerator.BladeBreakerSystemType.RESURGENT) {
                        List<CampaignFleetAPI> stations = this.addBattlestations(data, 1.0F, 1, 1, this.createStringPicker(new Object[]{"istl_guardian_turret_std", 10.0F}));
                        Iterator var27 = stations.iterator();

                        while(var27.hasNext()) {
                           CampaignFleetAPI station = (CampaignFleetAPI)var27.next();
                           int maxFleets = 8 + this.random.nextInt(5);
                           BladeBreakerStationFleetManager activeFleets = new BladeBreakerStationFleetManager(station, 1.0F, 2, maxFleets, 10.0F, 8, 24);
                           data.system.addScript(activeFleets);
                        }
                     }
                  }

                  Iterator var38 = systems.iterator();

                  while(true) {
                     int index;
                     do {
                        if (!var38.hasNext()) {
                           ++count;
                           continue label181;
                        }

                        data = (StarSystemData)var38.next();
                        index = mainCandidates.indexOf(data);
                     } while(index >= 0 && index < numMain);

                     this.populateNonMain(data);
                     if (type == BladeBreakerThemeGenerator.BladeBreakerSystemType.DESTROYED) {
                        data.system.addTag("theme_interesting_minor");
                     } else {
                        data.system.addTag("theme_interesting");
                     }

                     data.system.addTag("theme_breakers");
                     data.system.addTag("theme_breakers_secondary");
                     data.system.addTag(type.getTag());
                     breakerSystems.add(data);
                     if (this.random.nextFloat() < 0.5F) {
                        BladeBreakerSeededFleetManager fleets = new BladeBreakerSeededFleetManager(data.system, 1, 3, 1, 2, 0.05F);
                        data.system.addScript(fleets);
                     } else {
                        data.system.addTag("theme_breakers_no_fleets");
                     }
                  }
               }
            }
         }

         SpecialCreationContext specialContext = new SpecialCreationContext();
         specialContext.themeId = this.getThemeId();
         SalvageSpecialAssigner.assignSpecials(breakerSystems, specialContext);
         this.addDefenders(breakerSystems);
         if (DEBUG) {
            System.out.println("Finished generating Blade Breaker systems\n\n\n\n\n");
         }

      }
   }

   public void addDefenders(List<StarSystemData> systemData) {
      Iterator var2 = systemData.iterator();

      while(var2.hasNext()) {
         StarSystemData data = (StarSystemData)var2.next();
         float mult = 1.0F;
         if (data.system.hasTag("theme_breakers_secondary")) {
            mult = 0.5F;
         }

         Iterator var5 = data.generated.iterator();

         while(var5.hasNext()) {
            AddedEntity added = (AddedEntity)var5.next();
            if (added.entityType != null && !"wreck".equals(added.entityType)) {
               float prob = 0.0F;
               float min = 1.0F;
               float max = 1.0F;
               if ("istl_bladebreaker_mining".equals(added.entityType)) {
                  prob = 0.25F;
                  min = 8.0F;
                  max = 15.0F;
               } else if ("istl_bladebreaker_habitat".equals(added.entityType)) {
                  prob = 0.25F;
                  min = 8.0F;
                  max = 15.0F;
               } else if ("istl_bladebreaker_research".equals(added.entityType)) {
                  prob = 0.25F;
                  min = 10.0F;
                  max = 20.0F;
               }

               min *= 3.0F;
               max *= 3.0F;
               prob *= mult;
               min *= mult;
               max *= mult;
               if (min < 1.0F) {
                  min = 1.0F;
               }

               if (max < 1.0F) {
                  max = 1.0F;
               }

               if (this.random.nextFloat() < prob) {
                  Misc.setDefenderOverride(added.entity, new DefenderDataOverride("blade_breakers", 1.0F, min, max, 4));
               }
            }
         }
      }

   }

   public void populateNonMain(StarSystemData data) {
      if (DEBUG) {
         System.out.println(" Generating secondary breaker system in " + data.system.getName());
      }

      boolean special = data.isBlackHole() || data.isNebula() || data.isPulsar();
      if (special) {
         this.addResearchStations(data, 0.75F, 1, 1, this.createStringPicker(new Object[]{"istl_bladebreaker_research", 10.0F}));
      }

      if (!(this.random.nextFloat() < 0.5F)) {
         if (!data.resourceRich.isEmpty()) {
            this.addMiningStations(data, 0.5F, 1, 1, this.createStringPicker(new Object[]{"istl_bladebreaker_mining", 10.0F}));
         }

         if (!special && !data.habitable.isEmpty()) {
            this.addHabCenters(data, 0.25F, 1, 1, this.createStringPicker(new Object[]{"istl_bladebreaker_habitat", 10.0F}));
         }

         this.addShipGraveyard(data, 0.05F, 1, 1, this.createStringPicker(new Object[]{"independent", 10.0F, "scavengers", 8.0F, "dassault_mikoyan", 6.0F, "pirates", 3.0F}));
         this.addDebrisFields(data, 0.25F, 1, 2);
         this.addDerelictShips(data, 0.5F, 0, 3, this.createStringPicker(new Object[]{"independent", 10.0F, "scavengers", 8.0F, "dassault_mikoyan", 6.0F, "pirates", 3.0F}));
         this.addCaches(data, 0.25F, 0, 2, this.createStringPicker(new Object[]{"weapons_cache_breakers", 2.0F, "weapons_cache_small_breakers", 5.0F, "supply_cache", 4.0F, "supply_cache_small", 10.0F, "equipment_cache", 4.0F, "equipment_cache_small", 10.0F}));
      }
   }

   public void populateMain(StarSystemData data, BladeBreakerThemeGenerator.BladeBreakerSystemType type) {
      if (DEBUG) {
         System.out.println(" Generating Blade Breaker center in " + data.system.getName());
      }

      StarSystemAPI system = data.system;
      addBeacon(system, type);
      if (DEBUG) {
         System.out.println("    Added hardened warning beacon");
      }

      int maxHabCenters = 1 + this.random.nextInt(3);
      HabitationLevel level = HabitationLevel.LOW;
      if (maxHabCenters == 2) {
         level = HabitationLevel.MEDIUM;
      }

      if (maxHabCenters >= 3) {
         level = HabitationLevel.HIGH;
      }

      this.addHabCenters(data, 1.0F, maxHabCenters, maxHabCenters, this.createStringPicker(new Object[]{"istl_bladebreaker_habitat", 10.0F}));
      float probGate = 1.0F;
      float probRelay = 1.0F;
      float probMining = 0.5F;
      float probResearch = 0.25F;
      switch(level) {
      case HIGH:
         probGate = 0.75F;
         probRelay = 1.0F;
         break;
      case MEDIUM:
         probGate = 0.5F;
         probRelay = 0.75F;
         break;
      case LOW:
         probGate = 0.25F;
         probRelay = 0.5F;
      }

      this.addObjectives(data, probRelay);
      this.addInactiveGate(data, probGate, 0.5F, 0.5F, this.createStringPicker(new Object[]{"tritachyon", 10.0F, "hegemony", 7.0F, "independent", 3.0F}));
      this.addShipGraveyard(data, 0.25F, 1, 1, this.createStringPicker(new Object[]{"tritachyon", 10.0F, "hegemony", 7.0F, "independent", 3.0F}));
      this.addMiningStations(data, probMining, 1, 1, this.createStringPicker(new Object[]{"istl_bladebreaker_mining", 10.0F}));
      this.addResearchStations(data, probResearch, 1, 1, this.createStringPicker(new Object[]{"istl_bladebreaker_research", 10.0F}));
      this.addDebrisFields(data, 0.75F, 1, 5);
      this.addDerelictShips(data, 0.75F, 0, 7, this.createStringPicker(new Object[]{"tritachyon", 10.0F, "hegemony", 7.0F, "independent", 3.0F}));
      this.addCaches(data, 0.75F, 0, 3, this.createStringPicker(new Object[]{"weapons_cache_breakers", 5.0F, "weapons_cache_small_breakers", 5.0F, "supply_cache", 10.0F, "supply_cache_small", 10.0F, "equipment_cache", 10.0F, "equipment_cache_small", 10.0F}));
   }

   public List<StarSystemData> getSortedSystemsSuitedToBePopulated(List<StarSystemData> systems) {
      List<StarSystemData> result = new ArrayList();
      Iterator var3 = systems.iterator();

      while(true) {
         StarSystemData data;
         do {
            do {
               do {
                  do {
                     if (!var3.hasNext()) {
                        Collections.sort(systems, new Comparator<StarSystemData>() {
                           public int compare(StarSystemData o1, StarSystemData o2) {
                              float s1 = BladeBreakerThemeGenerator.this.getMainCenterScore(o1);
                              float s2 = BladeBreakerThemeGenerator.this.getMainCenterScore(o2);
                              return (int)Math.signum(s2 - s1);
                           }
                        });
                        return result;
                     }

                     data = (StarSystemData)var3.next();
                  } while(data.isBlackHole());
               } while(data.isNebula());
            } while(data.isPulsar());
         } while(data.planets.size() < 4 && data.habitable.size() < 1);

         result.add(data);
      }
   }

   public float getMainCenterScore(StarSystemData data) {
      float total = 0.0F;
      total += (float)data.planets.size() * 1.0F;
      total += (float)data.habitable.size() * 2.0F;
      total += (float)data.resourceRich.size() * 0.25F;
      return total;
   }

   public static CustomCampaignEntityAPI addBeacon(StarSystemAPI system, BladeBreakerThemeGenerator.BladeBreakerSystemType type) {
      SectorEntityToken anchor = system.getHyperspaceAnchor();
      List<SectorEntityToken> points = Global.getSector().getHyperspace().getEntities(JumpPointAPI.class);
      float minRange = 600.0F;
      float closestRange = Float.MAX_VALUE;
      JumpPointAPI closestPoint = null;
      Iterator var7 = points.iterator();

      while(var7.hasNext()) {
         SectorEntityToken entity = (SectorEntityToken)var7.next();
         JumpPointAPI point = (JumpPointAPI)entity;
         if (!point.getDestinations().isEmpty()) {
            JumpDestination dest = (JumpDestination)point.getDestinations().get(0);
            if (dest.getDestination().getContainingLocation() == system) {
               float dist = Misc.getDistance(anchor.getLocation(), point.getLocation());
               if (!(dist < minRange + point.getRadius()) && dist < closestRange) {
                  closestPoint = point;
                  closestRange = dist;
               }
            }
         }
      }

      CustomCampaignEntityAPI beacon = Global.getSector().getHyperspace().addCustomEntity((String)null, (String)null, "istl_bladebreaker_beacon", "neutral");
      beacon.getMemoryWithoutUpdate().set(type.getBeaconFlag(), true);
      switch(type) {
      case DESTROYED:
         beacon.addTag("beacon_low");
         break;
      case SUPPRESSED:
         beacon.addTag("beacon_medium");
         break;
      case RESURGENT:
         beacon.addTag("beacon_high");
      }

      float orbitDays;
      if (closestPoint == null) {
         orbitDays = minRange / (10.0F + StarSystemGenerator.random.nextFloat() * 5.0F);
         beacon.setCircularOrbitPointingDown(anchor, StarSystemGenerator.random.nextFloat() * 360.0F, minRange, orbitDays);
      } else {
         orbitDays = 20.0F + StarSystemGenerator.random.nextFloat() * 20.0F;
         float angle = Misc.getAngleInDegrees(anchor.getLocation(), closestPoint.getLocation()) + orbitDays;
         if (closestPoint.getOrbit() != null) {
            OrbitAPI orbit = Global.getFactory().createCircularOrbitPointingDown(anchor, angle, closestRange, closestPoint.getOrbit().getOrbitalPeriod());
            beacon.setOrbit(orbit);
         } else {
            Vector2f beaconLoc = Misc.getUnitVectorAtDegreeAngle(angle);
            beaconLoc.scale(closestRange);
            Vector2f.add(beaconLoc, anchor.getLocation(), beaconLoc);
            beacon.getLocation().set(beaconLoc);
         }
      }

      Color glowColor = new Color(75, 255, 175, 255);
      Color pingColor = new Color(75, 255, 175, 255);
      if (type == BladeBreakerThemeGenerator.BladeBreakerSystemType.SUPPRESSED) {
         glowColor = new Color(175, 145, 0, 255);
         pingColor = new Color(175, 145, 0, 255);
      } else if (type == BladeBreakerThemeGenerator.BladeBreakerSystemType.RESURGENT) {
         glowColor = new Color(250, 55, 0, 255);
         pingColor = new Color(250, 55, 0, 255);
      }

      Misc.setWarningBeaconColors(beacon, glowColor, pingColor);
      return beacon;
   }

   protected List<Constellation> getSortedAvailableConstellations(ThemeGenContext context, boolean emptyOk, final Vector2f sortFrom, List<Constellation> exclude) {
      List<Constellation> constellations = new ArrayList();
      Iterator var6 = context.constellations.iterator();

      while(true) {
         Constellation c;
         do {
            do {
               if (!var6.hasNext()) {
                  if (exclude != null) {
                     constellations.removeAll(exclude);
                  }

                  Collections.sort(constellations, new Comparator<Constellation>() {
                     public int compare(Constellation o1, Constellation o2) {
                        float d1 = Misc.getDistance(o1.getLocation(), sortFrom);
                        float d2 = Misc.getDistance(o2.getLocation(), sortFrom);
                        return (int)Math.signum(d2 - d1);
                     }
                  });
                  return constellations;
               }

               c = (Constellation)var6.next();
            } while(context.majorThemes.containsKey(c));
         } while(!emptyOk && constellationIsEmpty(c));

         constellations.add(c);
      }
   }

   public static boolean constellationIsEmpty(Constellation c) {
      Iterator var1 = c.getSystems().iterator();

      StarSystemAPI s;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         s = (StarSystemAPI)var1.next();
      } while(systemIsEmpty(s));

      return false;
   }

   public static boolean systemIsEmpty(StarSystemAPI system) {
      Iterator var1 = system.getPlanets().iterator();

      PlanetAPI p;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         p = (PlanetAPI)var1.next();
      } while(p.isStar());

      return false;
   }

   public List<CampaignFleetAPI> addBattlestations(StarSystemData data, float chanceToAddAny, int min, int max, WeightedRandomPicker<String> stationTypes) {
      List<CampaignFleetAPI> result = new ArrayList();
      if (this.random.nextFloat() >= chanceToAddAny) {
         return result;
      } else {
         int num = min + this.random.nextInt(max - min + 1);
         if (DEBUG) {
            System.out.println("    Adding " + num + " guardians");
         }

         for(int i = 0; i < num; ++i) {
            EntityLocation loc = pickCommonLocation(this.random, data.system, 200.0F, true, (Set)null);
            String type = (String)stationTypes.pick();
            if (loc != null) {
               CampaignFleetAPI fleet = FleetFactoryV3.createEmptyFleet("blade_breakers", "battlestation", (MarketAPI)null);
               FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, type);
               fleet.getFleetData().addFleetMember(member);
               fleet.getMemoryWithoutUpdate().set("$cfai_makeAggressive", true);
               fleet.getMemoryWithoutUpdate().set("$cfai_noJump", true);
               fleet.getMemoryWithoutUpdate().set("$cfai_makeAllowDisengage", true);
               fleet.addTag("neutrino_high");
               fleet.setStationMode(true);
               addBladeBreakerStationInteractionConfig(fleet);
               data.system.addEntity(fleet);
               fleet.clearAbilities();
               fleet.addAbility("transponder");
               fleet.getAbility("transponder").activate();
               fleet.getDetectedRangeMod().modifyFlat("gen", 1000.0F);
               fleet.setAI((CampaignFleetAIAPI)null);
               setEntityLocation(fleet, loc, (String)null);
               convertOrbitWithSpin(fleet, 5.0F);
               boolean damaged = type.toLowerCase().contains("damaged");
               float mult = 25.0F;
               int level = 20;
               if (damaged) {
                  mult = 10.0F;
                  level = 10;
                  fleet.getMemoryWithoutUpdate().set("$damagedStation", true);
               }

               PersonAPI commander = OfficerManagerEvent.createOfficer(Global.getSector().getFaction("blade_breakers"), level, true);
               if (!damaged) {
                  commander.getStats().setSkillLevel("gunnery_implants", 3.0F);
               }

               FleetFactoryV3.addCommanderSkills(commander, fleet, this.random);
               fleet.setCommander(commander);
               fleet.getFlagship().setCaptain(commander);
               member.getRepairTracker().setCR(member.getRepairTracker().getMaxCR());
               result.add(fleet);
            }
         }

         return result;
      }
   }

   public static void addBladeBreakerStationInteractionConfig(CampaignFleetAPI fleet) {
      fleet.getMemoryWithoutUpdate().set("$fidConifgGen", new BladeBreakerThemeGenerator.BladeBreakerStationInteractionConfigGen());
   }

   public int getOrder() {
      return 1500;
   }

   public static class BladeBreakerStationInteractionConfigGen implements FIDConfigGen {
      public FIDConfig createConfig() {
         FIDConfig config = new FIDConfig();
         config.alwaysAttackVsAttack = true;
         config.leaveAlwaysAvailable = true;
         config.showFleetAttitude = false;
         config.showTransponderStatus = false;
         config.showEngageText = false;
         config.delegate = new BaseFIDDelegate() {
            public void postPlayerSalvageGeneration(InteractionDialogAPI dialog, FleetEncounterContext context, CargoAPI salvage) {
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

   public static enum BladeBreakerSystemType {
      DESTROYED("theme_breakers_destroyed", "$breakerDestroyed"),
      SUPPRESSED("theme_breakers_suppressed", "$breakerSuppressed"),
      RESURGENT("theme_breakers_resurgent", "$breakerResurgent"),
      HOMEWORLD("theme_breakers_homeworld", "$breakerHomeworld");

      private String tag;
      private String beaconFlag;

      private BladeBreakerSystemType(String tag, String beaconFlag) {
         this.tag = tag;
         this.beaconFlag = beaconFlag;
      }

      public String getTag() {
         return this.tag;
      }

      public String getBeaconFlag() {
         return this.beaconFlag;
      }
   }
}
