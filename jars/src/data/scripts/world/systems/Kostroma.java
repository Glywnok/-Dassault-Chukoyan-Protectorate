package data.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.procgen.DefenderDataOverride;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.DerelictThemeGenerator;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.BaseSalvageSpecial;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin.AsteroidFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.BaseRingTerrain.RingParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldSource;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin.MagneticFieldParams;
import com.fs.starfarer.api.util.Misc;
import data.scripts.world.AddMarketplace;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class Kostroma {
   public void generate(SectorAPI sector) {
      StarSystemAPI system = sector.createStarSystem("Kostroma");
      LocationAPI hyper = Global.getSector().getHyperspace();
      system.setBackgroundTextureFilename("graphics/backgrounds/background6.jpg");
      PlanetAPI kostroma_star = system.initStar("kostroma", "star_orange_giant", 520.0F, 400.0F);
      kostroma_star.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "banded"));
      kostroma_star.getSpec().setGlowColor(new Color(255, 235, 50, 128));
      kostroma_star.getSpec().setAtmosphereThickness(0.5F);
      kostroma_star.applySpecChanges();
      system.setLightColor(new Color(255, 240, 220));
      system.addTag("theme_core");
      system.addTag("theme_core_populated");
      system.addTag("theme_dassault_system");
      system.addTag("theme_dassault_major");
      PlanetAPI cendre = system.addPlanet("istl_planet_cendre", kostroma_star, "Cendre", "barren", 210.0F, 35.0F, 920.0F, 30.0F);
      cendre.setCustomDescriptionId("planet_cendre");
      Misc.initConditionMarket(cendre);
      cendre.getMarket().addCondition("no_atmosphere");
      cendre.getMarket().addCondition("low_gravity");
      cendre.getMarket().addCondition("very_hot");
      SectorEntityToken kostroma_field1 = system.addTerrain("magnetic_field", new MagneticFieldParams(400.0F, 1050.0F, kostroma_star, 850.0F, 1250.0F, new Color(50, 30, 100, 45), 0.3F, new Color[]{new Color(50, 20, 110, 130), new Color(150, 30, 120, 150), new Color(200, 50, 130, 190), new Color(250, 70, 150, 240), new Color(200, 80, 130, 255), new Color(75, 0, 160), new Color(127, 0, 255)}));
      kostroma_field1.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 120.0F);
      PlanetAPI astalon = system.addPlanet("istl_planet_astalon", kostroma_star, "Astalon", "toxic", 30.0F, 75.0F, 1540.0F, 80.0F);
      astalon.getSpec().setPitch(-15.0F);
      astalon.getSpec().setTilt(12.0F);
      astalon.applySpecChanges();
      astalon.setInteractionImage("illustrations", "desert_moons_ruins");
      AddMarketplace.addMarketplace("independent", astalon, (ArrayList)null, "Astalon", 3, new ArrayList(Arrays.asList("frontier", "organics_plentiful", "very_hot", "population_3")), new ArrayList(Arrays.asList("mining", "spaceport", "population")), new ArrayList(Arrays.asList("black_market", "open_market", "storage")), 0.3F);
      astalon.setCustomDescriptionId("planet_astalon");
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 1760.0F, 98.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 2, Color.white, 256.0F, 1800.0F, 102.0F);
      system.addAsteroidBelt(kostroma_star, 120, 1780.0F, 300.0F, 200.0F, 300.0F, "asteroid_belt", "Astalon Necklace");
      SectorEntityToken kostroma_field2 = system.addTerrain("magnetic_field", new MagneticFieldParams(200.0F, 2020.0F, kostroma_star, 1920.0F, 2120.0F, new Color(50, 30, 100, 30), 0.6F, new Color[]{new Color(50, 20, 110, 130), new Color(150, 30, 120, 150), new Color(200, 50, 130, 190), new Color(250, 70, 150, 240), new Color(200, 80, 130, 255), new Color(75, 0, 160), new Color(127, 0, 255)}));
      kostroma_field2.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 180.0F);
      PlanetAPI mariegalante = system.addPlanet("istl_planet_mariegalante", kostroma_star, "Marie-Galante", "jungle", 270.0F, 150.0F, 2400.0F, 135.0F);
      mariegalante.setCustomDescriptionId("planet_mariegalante");
      mariegalante.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "asharu"));
      mariegalante.getSpec().setGlowColor(new Color(235, 235, 255, 255));
      mariegalante.getSpec().setUseReverseLightForGlow(true);
      mariegalante.getSpec().setCloudColor(new Color(245, 255, 235, 235));
      mariegalante.getSpec().setCloudRotation(12.0F);
      mariegalante.getSpec().setPitch(30.0F);
      mariegalante.getSpec().setTilt(35.0F);
      mariegalante.applySpecChanges();
      Misc.initConditionMarket(mariegalante);
      mariegalante.getMarket().addCondition("decivilized");
      mariegalante.getMarket().addCondition("ruins_vast");
      mariegalante.getMarket().getFirstCondition("ruins_vast").setSurveyed(true);
      mariegalante.getMarket().addCondition("inimical_biosphere");
      mariegalante.getMarket().addCondition("farmland_adequate");
      mariegalante.getMarket().addCondition("organics_plentiful");
      mariegalante.getMarket().addCondition("extreme_weather");
      SectorEntityToken derelictTerminal = system.addCustomEntity("mariegalante_station", "Orbital Terminal", "station_side03", "neutral");
      derelictTerminal.setCircularOrbitWithSpin(system.getEntityById("istl_planet_mariegalante"), 210.0F, 240.0F, 45.0F, 7.0F, 21.0F);
      derelictTerminal.setInteractionImage("illustrations", "space_wreckage");
      derelictTerminal.setCustomDescriptionId("station_mariegalante");
      PlanetAPI cayenne = system.addPlanet("istl_planet_cayenne", mariegalante, "Cayenne", "barren-bombarded", 30.0F, 50.0F, 300.0F, 45.0F);
      cayenne.setCustomDescriptionId("planet_cayenne");
      cayenne.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "asharu"));
      cayenne.getSpec().setGlowColor(new Color(255, 245, 235, 255));
      cayenne.getSpec().setUseReverseLightForGlow(true);
      cayenne.getSpec().setAtmosphereThicknessMin(25.0F);
      cayenne.getSpec().setAtmosphereThickness(0.2F);
      cayenne.getSpec().setAtmosphereColor(new Color(80, 90, 100, 120));
      cayenne.applySpecChanges();
      Misc.initConditionMarket(cayenne);
      cayenne.getMarket().addCondition("decivilized");
      cayenne.getMarket().addCondition("ruins_extensive");
      cayenne.getMarket().getFirstCondition("ruins_extensive").setSurveyed(true);
      cayenne.getMarket().addCondition("meteor_impacts");
      cayenne.getMarket().addCondition("pollution");
      cayenne.getMarket().addCondition("thin_atmosphere");
      JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("kostroma_inner_jump", "Kostroma Jump-point");
      jumpPoint1.setCircularOrbit(system.getEntityById("kostroma"), 330.0F, 2700.0F, 135.0F);
      jumpPoint1.setRelatedPlanet(mariegalante);
      system.addEntity(jumpPoint1);
      SectorEntityToken mariegalante_buoy = system.addCustomEntity("marie_galante_buoy", "Marie-Galante Nav Buoy", "nav_buoy", "neutral");
      mariegalante_buoy.setCircularOrbitPointingDown(system.getEntityById("kostroma"), 210.0F, 2400.0F, 135.0F);
      SectorEntityToken mariegalante_l3_loc = system.addCustomEntity((String)null, (String)null, "stable_location", "neutral");
      mariegalante_l3_loc.setCircularOrbitPointingDown(kostroma_star, 90.0F, 2400.0F, 135.0F);
      system.addAsteroidBelt(kostroma_star, 100, 3100.0F, 256.0F, 150.0F, 250.0F, "asteroid_belt", (String)null);
      system.addAsteroidBelt(kostroma_star, 100, 3500.0F, 256.0F, 150.0F, 250.0F, "asteroid_belt", (String)null);
      system.addAsteroidBelt(kostroma_star, 100, 3950.0F, 128.0F, 200.0F, 300.0F, "asteroid_belt", (String)null);
      system.addAsteroidBelt(kostroma_star, 100, 4250.0F, 188.0F, 200.0F, 300.0F, "asteroid_belt", (String)null);
      system.addAsteroidBelt(kostroma_star, 100, 4475.0F, 256.0F, 200.0F, 300.0F, "asteroid_belt", (String)null);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 0, Color.white, 256.0F, 3000.0F, 120.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 3200.0F, 150.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 2, Color.white, 256.0F, 3400.0F, 180.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 3600.0F, 120.0F);
      SectorEntityToken ring = system.addTerrain("ring", new RingParams(856.0F, 3300.0F, (SectorEntityToken)null, "Kostroma Meridian Disk"));
      ring.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 100.0F);
      SectorEntityToken scrap1 = DerelictThemeGenerator.addSalvageEntity(system, "equipment_cache_small", "derelict");
      scrap1.setId("kostroma_scrap1");
      scrap1.setCircularOrbit(kostroma_star, 0.0F, 3700.0F, 275.0F);
      Misc.setDefenderOverride(scrap1, new DefenderDataOverride("derelict", 0.0F, 0.0F, 0.0F));
      scrap1.setDiscoverable(Boolean.TRUE);
      SectorEntityToken scrap2 = DerelictThemeGenerator.addSalvageEntity(system, "weapons_cache_small", "derelict");
      scrap2.setId("kostroma_scrap2");
      scrap2.setCircularOrbit(kostroma_star, 120.0F, 3700.0F, 275.0F);
      Misc.setDefenderOverride(scrap2, new DefenderDataOverride("derelict", 0.0F, 0.0F, 0.0F));
      scrap2.setDiscoverable(Boolean.TRUE);
      SectorEntityToken scrap3 = DerelictThemeGenerator.addSalvageEntity(system, "weapons_cache", "derelict");
      scrap3.setId("kostroma_scrap3");
      scrap3.setCircularOrbit(kostroma_star, 240.0F, 3700.0F, 275.0F);
      Misc.setDefenderOverride(scrap3, new DefenderDataOverride("derelict", 2.0F, 8.0F, 0.0F));
      scrap3.setDiscoverable(Boolean.TRUE);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 0, Color.white, 256.0F, 3800.0F, 80.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 3900.0F, 120.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 2, Color.white, 256.0F, 4000.0F, 160.0F);
      ring = system.addTerrain("ring", new RingParams(456.0F, 3900.0F, (SectorEntityToken)null, "Kostroma Meridian Disk"));
      ring.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 100.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 3, Color.white, 256.0F, 4100.0F, 140.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 2, Color.white, 256.0F, 4200.0F, 180.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 4300.0F, 220.0F);
      ring = system.addTerrain("ring", new RingParams(456.0F, 4200.0F, (SectorEntityToken)null, "Kostroma Meridian Disk"));
      ring.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 100.0F);
      DebrisFieldParams params1 = new DebrisFieldParams(520.0F, 1.8F, 1.0E7F, 0.0F);
      params1.source = DebrisFieldSource.MIXED;
      params1.baseSalvageXP = 500L;
      SectorEntityToken debrisMeridian1 = Misc.addDebrisField(system, params1, StarSystemGenerator.random);
      debrisMeridian1.setSensorProfile(1000.0F);
      debrisMeridian1.setDiscoverable(true);
      debrisMeridian1.setCircularOrbit(kostroma_star, 135.0F, 4400.0F, 210.0F);
      debrisMeridian1.setId("kostroma_debrisMeridian1");
      DebrisFieldParams params2 = new DebrisFieldParams(360.0F, 1.2F, 1.0E7F, 0.0F);
      params2.source = DebrisFieldSource.MIXED;
      params2.baseSalvageXP = 500L;
      SectorEntityToken debrisMeridian2 = Misc.addDebrisField(system, params2, StarSystemGenerator.random);
      debrisMeridian2.setSensorProfile(1000.0F);
      debrisMeridian2.setDiscoverable(true);
      debrisMeridian2.setCircularOrbit(kostroma_star, 180.0F, 4000.0F, 210.0F);
      debrisMeridian2.setId("kostroma_debrisMeridian2");
      DebrisFieldParams params3 = new DebrisFieldParams(450.0F, 1.5F, 1.0E7F, 0.0F);
      params3.source = DebrisFieldSource.MIXED;
      params3.baseSalvageXP = 500L;
      SectorEntityToken debrisMeridian3 = Misc.addDebrisField(system, params3, StarSystemGenerator.random);
      debrisMeridian3.setSensorProfile(1000.0F);
      debrisMeridian3.setDiscoverable(true);
      debrisMeridian3.setCircularOrbit(kostroma_star, 270.0F, 4200.0F, 210.0F);
      debrisMeridian3.setId("kostroma_debrisMeridian3");
      DebrisFieldParams params4 = new DebrisFieldParams(360.0F, 1.2F, 1.0E7F, 0.0F);
      params4.source = DebrisFieldSource.MIXED;
      params4.baseSalvageXP = 500L;
      SectorEntityToken debrisMeridian4 = Misc.addDebrisField(system, params4, StarSystemGenerator.random);
      debrisMeridian4.setSensorProfile(1000.0F);
      debrisMeridian4.setDiscoverable(true);
      debrisMeridian4.setCircularOrbit(kostroma_star, 315.0F, 4200.0F, 210.0F);
      debrisMeridian4.setId("kostroma_debrisMeridian4");
      SectorEntityToken derelictPatrol = system.addCustomEntity("patrol_station", "Patrol Station", "station_side05", "neutral");
      derelictPatrol.setCircularOrbitWithSpin(system.getEntityById("kostroma"), 225.0F, 4400.0F, 210.0F, 9.0F, 27.0F);
      derelictPatrol.setInteractionImage("illustrations", "space_wreckage");
      derelictPatrol.setCustomDescriptionId("station_pirate1");
      SectorEntityToken derelictCustoms = system.addCustomEntity("customs_station", "Customs Port", "station_side05", "neutral");
      derelictCustoms.setCircularOrbitWithSpin(system.getEntityById("kostroma"), 345.0F, 4400.0F, 210.0F, 6.0F, 18.0F);
      derelictCustoms.setInteractionImage("illustrations", "space_wreckage");
      derelictCustoms.setCustomDescriptionId("station_pirate2");
      SectorEntityToken meridianStation = system.addCustomEntity("meridian_station", "Meridian Station", "station_side06", "pirates");
      meridianStation.setCircularOrbitPointingDown(system.getEntityById("kostroma"), 105.0F, 4400.0F, 210.0F);
      MarketAPI meridianStationMarket = AddMarketplace.addMarketplace("pirates", meridianStation, (ArrayList)null, "Meridian Station", 6, new ArrayList(Arrays.asList("free_market", "vice_demand", "stealth_minefields", "population_6")), new ArrayList(Arrays.asList("battlestation", "waystation", "spaceport", "population")), new ArrayList(Arrays.asList("black_market", "open_market", "storage")), 0.3F);
      meridianStationMarket.addIndustry("heavyindustry", new ArrayList(Arrays.asList("corrupted_nanoforge")));
      meridianStation.setCustomDescriptionId("station_meridian");
      system.addRingBand(kostroma_star, "misc", "rings_ice0", 256.0F, 0, Color.white, 256.0F, 4700.0F, 150.0F);
      system.addRingBand(kostroma_star, "misc", "rings_ice0", 256.0F, 2, Color.white, 256.0F, 4800.0F, 190.0F);
      system.addRingBand(kostroma_star, "misc", "rings_ice0", 256.0F, 1, Color.white, 256.0F, 4900.0F, 210.0F);
      system.addRingBand(kostroma_star, "misc", "rings_ice0", 256.0F, 2, Color.white, 256.0F, 5000.0F, 230.0F);
      ring = system.addTerrain("ring", new RingParams(556.0F, 4850.0F, (SectorEntityToken)null, "Kostroma Meridian Disk"));
      ring.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 100.0F);
      system.addRingBand(kostroma_star, "misc", "rings_ice0", 256.0F, 3, Color.white, 256.0F, 5350.0F, 150.0F);
      system.addRingBand(kostroma_star, "misc", "rings_ice0", 256.0F, 2, Color.white, 256.0F, 5575.0F, 190.0F);
      system.addRingBand(kostroma_star, "misc", "rings_ice0", 256.0F, 1, Color.white, 256.0F, 5600.0F, 210.0F);
      ring = system.addTerrain("ring", new RingParams(556.0F, 5575.0F, (SectorEntityToken)null, "Kostroma Meridian Disk"));
      ring.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 100.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 5800.0F, 235.0F);
      DebrisFieldParams params5 = new DebrisFieldParams(240.0F, 1.2F, 1.0E7F, 0.0F);
      params5.source = DebrisFieldSource.MIXED;
      params5.baseSalvageXP = 500L;
      SectorEntityToken debrisOuter1 = Misc.addDebrisField(system, params5, StarSystemGenerator.random);
      debrisOuter1.setSensorProfile(1000.0F);
      debrisOuter1.setDiscoverable(true);
      debrisOuter1.setCircularOrbit(kostroma_star, 60.0F, 5500.0F, 240.0F);
      debrisOuter1.setId("kostroma_debrisOuter1");
      DebrisFieldParams params6 = new DebrisFieldParams(300.0F, 1.2F, 1.0E7F, 0.0F);
      params6.source = DebrisFieldSource.MIXED;
      params6.baseSalvageXP = 500L;
      SectorEntityToken debrisOuter2 = Misc.addDebrisField(system, params6, StarSystemGenerator.random);
      debrisOuter2.setSensorProfile(1000.0F);
      debrisOuter2.setDiscoverable(true);
      debrisOuter2.setCircularOrbit(kostroma_star, 15.0F, 5500.0F, 240.0F);
      debrisOuter2.setId("kostroma_debrisOuter2");
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 0, Color.white, 256.0F, 5900.0F, 240.0F);
      PlanetAPI gironde = system.addPlanet("istl_planet_gironde", kostroma_star, "Gironde", "gas_giant", 300.0F, 360.0F, 8000.0F, 240.0F);
      gironde.getSpec().setPitch(35.0F);
      gironde.getSpec().setPlanetColor(new Color(200, 235, 245, 255));
      gironde.getSpec().setAtmosphereColor(new Color(210, 240, 250, 250));
      gironde.getSpec().setCloudColor(new Color(220, 250, 240, 200));
      gironde.getSpec().setIconColor(new Color(100, 130, 120, 255));
      gironde.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
      gironde.getSpec().setGlowColor(new Color(225, 45, 225, 145));
      gironde.getSpec().setUseReverseLightForGlow(true);
      gironde.getSpec().setAtmosphereThickness(0.5F);
      gironde.applySpecChanges();
      system.addRingBand(gironde, "misc", "rings_dust0", 256.0F, 3, Color.white, 256.0F, 520.0F, 45.0F, "ring", "Gironde Rings");
      system.addRingBand(gironde, "misc", "rings_dust0", 256.0F, 2, Color.white, 256.0F, 540.0F, 60.0F, (String)null, (String)null);
      gironde.setCustomDescriptionId("planet_gironde");
      SectorEntityToken gironde_field1 = system.addTerrain("magnetic_field", new MagneticFieldParams(200.0F, 460.0F, gironde, 360.0F, 560.0F, new Color(50, 30, 100, 60), 1.0F, new Color[]{new Color(50, 20, 110, 130), new Color(150, 30, 120, 150), new Color(200, 50, 130, 190), new Color(250, 70, 150, 240), new Color(200, 80, 130, 255), new Color(75, 0, 160), new Color(127, 0, 255)}));
      gironde_field1.setCircularOrbit(gironde, 0.0F, 0.0F, 100.0F);
      SectorEntityToken gironde_field2 = system.addTerrain("magnetic_field", new MagneticFieldParams(120.0F, 680.0F, gironde, 640.0F, 760.0F, new Color(50, 30, 100, 30), 0.6F, new Color[]{new Color(50, 20, 110, 130), new Color(150, 30, 120, 150), new Color(200, 50, 130, 190), new Color(250, 70, 150, 240), new Color(200, 80, 130, 255), new Color(75, 0, 160), new Color(127, 0, 255)}));
      gironde_field2.setCircularOrbit(gironde, 0.0F, 0.0F, 100.0F);
      SectorEntityToken girondeL4 = system.addTerrain("asteroid_field", new AsteroidFieldParams(600.0F, 900.0F, 28, 52, 7.0F, 18.0F, "Gironde L4 Trojans"));
      SectorEntityToken girondeL5 = system.addTerrain("asteroid_field", new AsteroidFieldParams(600.0F, 900.0F, 28, 52, 7.0F, 18.0F, "Gironde L5 Trojans"));
      girondeL4.setCircularOrbit(kostroma_star, 360.0F, 8000.0F, 240.0F);
      girondeL5.setCircularOrbit(kostroma_star, 240.0F, 8000.0F, 240.0F);
      PlanetAPI pyla = system.addPlanet("istl_planet_pyla", gironde, "Pyla", "arid", 150.0F, 40.0F, 760.0F, 60.0F);
      pyla.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "aurorae"));
      pyla.getSpec().setGlowColor(new Color(153, 50, 204, 225));
      pyla.getSpec().setUseReverseLightForGlow(true);
      pyla.applySpecChanges();
      pyla.setCustomDescriptionId("planet_pyla");
      Misc.initConditionMarket(pyla);
      pyla.getMarket().addCondition("thin_atmosphere");
      pyla.getMarket().addCondition("low_gravity");
      pyla.getMarket().addCondition("organics_common");
      pyla.getMarket().getFirstCondition("organics_common").setSurveyed(true);
      SectorEntityToken pyla_field = system.addTerrain("magnetic_field", new MagneticFieldParams(80.0F, 80.0F, pyla, 40.0F, 120.0F, new Color(50, 30, 100, 30), 0.8F, new Color[]{new Color(50, 20, 110, 130), new Color(150, 30, 120, 150), new Color(200, 50, 130, 190), new Color(250, 70, 150, 240), new Color(200, 80, 130, 255), new Color(75, 0, 160), new Color(127, 0, 255)}));
      pyla_field.setCircularOrbit(pyla, 0.0F, 0.0F, 100.0F);
      system.addRingBand(gironde, "misc", "rings_dust0", 256.0F, 0, Color.gray, 128.0F, 850.0F, 60.0F);
      PlanetAPI lareole = system.addPlanet("istl_planet_lareole", gironde, "La Réole", "water", 40.0F, 120.0F, 1280.0F, 105.0F);
      lareole.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
      lareole.getSpec().setGlowColor(new Color(255, 255, 255, 255));
      lareole.getSpec().setUseReverseLightForGlow(true);
      lareole.getSpec().setPitch(35.0F);
      lareole.applySpecChanges();
      SectorEntityToken lareolePolis = system.addCustomEntity("kostroma_port", "La Réole Polis", "station_dme_outpost", "dassault_mikoyan");
      lareolePolis.setCircularOrbitPointingDown(system.getEntityById("istl_planet_lareole"), 90.0F, 250.0F, 75.0F);
      lareolePolis.setInteractionImage("illustrations", "space_bar");
      SectorEntityToken lareole_loc = system.addCustomEntity((String)null, (String)null, "stable_location", "neutral");
      lareole_loc.setCircularOrbitPointingDown(gironde, 220.0F, 1280.0F, 105.0F);
      AddMarketplace.addMarketplace("dassault_mikoyan", lareole, new ArrayList(Arrays.asList(lareolePolis)), "La Réole", 5, new ArrayList(Arrays.asList("habitable", "water_surface", "free_market", "volturnian_lobster_pens", "organics_common", "ore_abundant", "rare_ore_sparse", "large_refugee_population", "urbanized_polity", "population_5")), new ArrayList(Arrays.asList("battlestation_high", "militarybase", "aquaculture", "mining", "spaceport", "heavybatteries", "6emebureau", "population")), new ArrayList(Arrays.asList("generic_military", "black_market", "open_market", "storage")), 0.2F);
      lareole.setCustomDescriptionId("planet_lareole");
      lareolePolis.setCustomDescriptionId("station_lareole_polis");
      system.addRingBand(gironde, "misc", "rings_dust0", 256.0F, 0, Color.white, 256.0F, 1720.0F, 120.0F);
      system.addAsteroidBelt(gironde, 120, 1720.0F, 135.0F, 200.0F, 300.0F, "asteroid_belt", "Gironde Belt");
      PlanetAPI chantilly = system.addPlanet("istl_planet_chantilly", gironde, "Chantilly", "barren", 300.0F, 50.0F, 1920.0F, 135.0F);
      chantilly.setCustomDescriptionId("planet_chantilly");
      Misc.initConditionMarket(chantilly);
      chantilly.getMarket().addCondition("thin_atmosphere");
      chantilly.getMarket().addCondition("low_gravity");
      chantilly.getMarket().addCondition("volatiles_trace");
      chantilly.getMarket().getFirstCondition("volatiles_trace").setSurveyed(true);
      JumpPointAPI jumpPoint2 = Global.getFactory().createJumpPoint("kostroma_outer_jump", "Gironde Bridge");
      jumpPoint2.setCircularOrbit(system.getEntityById("kostroma"), 240.0F, 8000.0F, 240.0F);
      jumpPoint2.setRelatedPlanet(lareole);
      system.addEntity(jumpPoint2);
      DebrisFieldParams params7 = new DebrisFieldParams(600.0F, 1.8F, 1.0E7F, 0.0F);
      params7.source = DebrisFieldSource.MIXED;
      params7.baseSalvageXP = 500L;
      SectorEntityToken debrisL4 = Misc.addDebrisField(system, params7, StarSystemGenerator.random);
      debrisL4.setSensorProfile(1000.0F);
      debrisL4.setDiscoverable(true);
      debrisL4.setCircularOrbit(kostroma_star, 360.0F, 8000.0F, 240.0F);
      debrisL4.setId("kostroma_debrisL4");
      SectorEntityToken kostroma_relay = system.addCustomEntity("kostroma_relay", "Kostroma Relay", "comm_relay", "dassault_mikoyan");
      kostroma_relay.setCircularOrbitPointingDown(system.getEntityById("kostroma"), 120.0F, 8000.0F, 240.0F);
      system.addAsteroidBelt(kostroma_star, 100, 10350.0F, 188.0F, 200.0F, 300.0F, "asteroid_belt", (String)null);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 3, Color.white, 256.0F, 10400.0F, 220.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 2, Color.white, 256.0F, 10500.0F, 260.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 10600.0F, 300.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 3, Color.white, 256.0F, 10750.0F, 340.0F);
      ring = system.addTerrain("ring", new RingParams(456.0F, 10575.0F, (SectorEntityToken)null, "Kostroma's Tears"));
      ring.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 120.0F);
      PlanetAPI tenacity = system.addPlanet("istl_planet_tenacity", kostroma_star, "Tenacity", "tundra", 150.0F, 135.0F, 11350.0F, 300.0F);
      tenacity.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "asharu"));
      tenacity.getSpec().setGlowColor(new Color(255, 160, 30, 255));
      tenacity.getSpec().setUseReverseLightForGlow(true);
      tenacity.getSpec().setPitch(-15.0F);
      tenacity.getSpec().setTilt(20.0F);
      tenacity.applySpecChanges();
      SectorEntityToken tenacity_mirror1 = system.addCustomEntity("tenacity_mirror1", "Tenacity Stellar Mirror Alpha", "stellar_mirror", "independent");
      SectorEntityToken tenacity_mirror2 = system.addCustomEntity("tenacity_mirror2", "Tenacity Stellar Mirror Beta", "stellar_mirror", "independent");
      SectorEntityToken tenacity_mirror3 = system.addCustomEntity("tenacity_mirror3", "Tenacity Stellar Mirror Gamma", "stellar_mirror", "independent");
      tenacity_mirror1.setCircularOrbitPointingDown(system.getEntityById("istl_planet_tenacity"), 113.0F, 360.0F, 300.0F);
      tenacity_mirror2.setCircularOrbitPointingDown(system.getEntityById("istl_planet_tenacity"), 150.0F, 360.0F, 300.0F);
      tenacity_mirror3.setCircularOrbitPointingDown(system.getEntityById("istl_planet_tenacity"), 187.0F, 360.0F, 300.0F);
      tenacity_mirror1.setCustomDescriptionId("stellar_mirror");
      tenacity_mirror2.setCustomDescriptionId("stellar_mirror");
      tenacity_mirror3.setCustomDescriptionId("stellar_mirror");
      AddMarketplace.addMarketplace("luddic_church", tenacity, new ArrayList(Arrays.asList(tenacity_mirror1, tenacity_mirror2, tenacity_mirror3)), "Tenacity", 5, new ArrayList(Arrays.asList("farmland_adequate", "large_refugee_population", "habitable", "solar_array", "luddic_majority", "population_5")), new ArrayList(Arrays.asList("farming", "lightindustry", "spaceport", "heavybatteries", "population")), new ArrayList(Arrays.asList("black_market", "open_market", "storage")), 0.3F);
      tenacity.setCustomDescriptionId("planet_tenacity");
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 0, Color.white, 256.0F, 11800.0F, 360.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 12000.0F, 360.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 0, Color.white, 256.0F, 12200.0F, 360.0F);
      ring = system.addTerrain("ring", new RingParams(456.0F, 12000.0F, (SectorEntityToken)null, "Kostroma's Tears"));
      ring.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 360.0F);
      SectorEntityToken gate = system.addCustomEntity("kostroma_gate", "Kostroma Gate", "inactive_gate", (String)null);
      gate.setCircularOrbit(system.getEntityById("kostroma"), 240.0F, 12800.0F, 400.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 0, Color.white, 256.0F, 13000.0F, 390.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 13200.0F, 420.0F);
      system.addRingBand(kostroma_star, "misc", "rings_dust0", 256.0F, 0, Color.white, 256.0F, 13400.0F, 400.0F);
      ring = system.addTerrain("ring", new RingParams(456.0F, 13200.0F, (SectorEntityToken)null, "Kostroma Fringe Belt"));
      ring.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 400.0F);
      DebrisFieldParams params8 = new DebrisFieldParams(360.0F, 1.2F, 1.0E7F, 0.0F);
      params8.source = DebrisFieldSource.MIXED;
      params8.baseSalvageXP = 500L;
      SectorEntityToken debrisOuter3 = Misc.addDebrisField(system, params8, StarSystemGenerator.random);
      debrisOuter3.setSensorProfile(1000.0F);
      debrisOuter3.setDiscoverable(true);
      debrisOuter3.setCircularOrbit(kostroma_star, 360.0F * (float)Math.random(), 13200.0F, 400.0F);
      debrisOuter3.setId("kostroma_debrisOuter3");
      DebrisFieldParams params9 = new DebrisFieldParams(450.0F, 1.5F, 1.0E7F, 0.0F);
      params9.source = DebrisFieldSource.MIXED;
      params9.baseSalvageXP = 500L;
      SectorEntityToken debrisOuter4 = Misc.addDebrisField(system, params9, StarSystemGenerator.random);
      debrisOuter4.setSensorProfile(1000.0F);
      debrisOuter4.setDiscoverable(true);
      debrisOuter4.setCircularOrbit(kostroma_star, 360.0F * (float)Math.random(), 13200.0F, 400.0F);
      debrisOuter4.setId("kostroma_debrisOuter4");
      PlanetAPI davout = system.addPlanet("istl_planet_davout", kostroma_star, "Davout", "frozen", 180.0F, 90.0F, 14000.0F, 360.0F);
      davout.setCustomDescriptionId("planet_davout");
      Misc.initConditionMarket(davout);
      davout.getMarket().addCondition("thin_atmosphere");
      davout.getMarket().addCondition("very_cold");
      davout.getMarket().addCondition("meteor_impacts");
      davout.getMarket().addCondition("volatiles_trace");
      davout.getMarket().getFirstCondition("volatiles_trace").setSurveyed(true);
      system.addAsteroidBelt(kostroma_star, 100, 14800.0F, 188.0F, 200.0F, 300.0F, "asteroid_belt", (String)null);
      system.addRingBand(kostroma_star, "misc", "rings_ice0", 256.0F, 3, Color.white, 256.0F, 14750.0F, 320.0F);
      system.addRingBand(kostroma_star, "misc", "rings_ice0", 256.0F, 1, Color.white, 256.0F, 14850.0F, 360.0F);
      ring = system.addTerrain("ring", new RingParams(456.0F, 14800.0F, (SectorEntityToken)null, "Kostroma Fringe Belt"));
      ring.setCircularOrbit(kostroma_star, 0.0F, 0.0F, 150.0F);
      SectorEntityToken stationDerelict = DerelictThemeGenerator.addSalvageEntity(system, "station_mining_remnant", "derelict");
      stationDerelict.setId("kostroma_derelict");
      stationDerelict.setCircularOrbit(kostroma_star, 360.0F * (float)Math.random(), 14800.0F, 400.0F);
      Misc.setDefenderOverride(stationDerelict, new DefenderDataOverride("blade_breakers", 1.0F, 2.0F, 8.0F));
      CargoAPI extraStationSalvage = Global.getFactory().createCargo(true);
      extraStationSalvage.addCommodity("beta_core", 1.0F);
      BaseSalvageSpecial.addExtraSalvage(extraStationSalvage, stationDerelict.getMemoryWithoutUpdate(), -1.0F);
      StarSystemGenerator.addOrbitingEntities(system, kostroma_star, StarAge.AVERAGE, 1, 3, 16000.0F, 6, true);
      StarSystemGenerator.addSystemwideNebula(system, StarAge.OLD);
      system.autogenerateHyperspaceJumpPoints(true, true);
      HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin)Misc.getHyperspaceTerrain().getPlugin();
      NebulaEditor editor = new NebulaEditor(plugin);
      float minRadius = plugin.getTileSize() * 2.0F;
      float radius = system.getMaxRadiusInHyperspace();
      editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0F, radius + minRadius, 0.0F, 360.0F);
      editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0F, radius + minRadius, 0.0F, 360.0F, 0.25F);
   }
}
