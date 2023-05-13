package data.scripts.world.systems;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.Constellation;
import com.fs.starfarer.api.impl.campaign.procgen.NameGenData;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.ProcgenUsedNames;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;
import com.fs.starfarer.api.impl.campaign.submarkets.StoragePlugin;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import data.scripts.world.AddMarketplace;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class KhamnConstellation {

    // used to keep the random location consistent between seeds, helpful for save transfer
    Random characterSaveSeed = StarSystemGenerator.random;
    Random random = new Random(characterSaveSeed.nextLong());
    // to be honest the double random should be overkill, yet it seems to make the distribution more "random"  
    // I would blame that normal generation seems to use about 10% of the possible seeds
    float selector = random.nextFloat();

    // generates an elliptical area, from where a random coordinate is selected for system generation
    // in this case the area is set to the sector's northwestern quadrant, instead of the whole sector
    // this is in order to avoid finding this sytem becoming a "find a needle in a haystack" task, as the star's name will also be random
    float spawnXradius = 3600f; // horizontal ellipse radius
    float spawnYradius = 2000f; // vertical ellipse radius
    float spawnXoffset = -4800f; // circular area X axis origin
    float spawnYoffset = 32000f; // circular area Y axis origin

    //values used to select a coordinate inside the ellipse
    //use squared radii, otherwise the distribution will be clustered in the origin
    float selectionXradiusSq = selector * spawnXradius * spawnXradius;
    float selectionYradiusSq = selector * spawnYradius * spawnYradius;
    float selectionAngle = selector * 360f;

    public float hsLocationX = (float) (sqrt(selectionXradiusSq) * cos(selectionAngle));
    public float hsLocationY = (float) (sqrt(selectionYradiusSq) * sin(selectionAngle));

    float A1Xoffset = (float) (-500 + Math.random() * 1000f);
    float A1Yoffset = (float) (1000 + Math.random() * -500f);

    float A2Xoffset = (float) (-2000 + Math.random() * -1000f);
    float A2Yoffset = (float) (-1000 + Math.random() * -2000f);

    float A3Xoffset = (float) (1500 + Math.random() * 1500f);
    float A3Yoffset = (float) (1500 + Math.random() * 1500f);

    float A4Xoffset = (float) (2000 + Math.random() * 1000f);
    float A4Yoffset = (float) (-1000 + Math.random() * -1000f);

    // name list for random renaming
    String[] strings = {"Estar", "Voscune", "Pailsen", "Apone"};
    int nameSelector = random.nextInt(strings.length);      // random name selector
    public String StarName = strings[nameSelector];
        
    //Random derelict radius 
    public static float radius_star = 320f;
    public static float radius_station = 2600f;
    public static float radius_acolyte_three = 1200f;
    public static float radius_variation = 400f;
        
        public void generate(SectorAPI sector) {

        LocationAPI hyper = Global.getSector().getHyperspace();

        StarAge magellan_constellation_Age = StarAge.ANY;

        if (selector < 0.33f) {
            magellan_constellation_Age = StarAge.YOUNG;
        }
        if (selector >= 0.33f && selector < 0.66f) {
            magellan_constellation_Age = StarAge.AVERAGE;
        }
        if (selector >= 0.66f) {
            magellan_constellation_Age = StarAge.OLD;
        }

        // create the constellation nebula       
        Constellation magellan_constellation_Khamn = new Constellation(
                Constellation.ConstellationType.NORMAL, magellan_constellation_Age
        );

        NameGenData data = new NameGenData("null", "null");
        ProcgenUsedNames.NamePick constname = new ProcgenUsedNames.NamePick(data, strings[nameSelector], "null");
        magellan_constellation_Khamn.setNamePick(constname);      // sets the new star name
        
        StarSystemAPI system_khamn = sector.createStarSystem("Khamn");
        StarSystemAPI system_two = sector.createStarSystem("Beta " + StarName);
        StarSystemAPI system_three = sector.createStarSystem("Gamma " + StarName);
        StarSystemAPI system_karic = sector.createStarSystem("Karic");
        //StarSystemAPI system_tet = sector.createStarSystem("prefix " + StarName);
        
        magellan_constellation_Khamn.getSystems().add(sector.getStarSystem("Khamn"));
        magellan_constellation_Khamn.getSystems().add(sector.getStarSystem("Beta " + StarName));
        magellan_constellation_Khamn.getSystems().add(sector.getStarSystem("Gamma " + StarName));
        magellan_constellation_Khamn.getSystems().add(sector.getStarSystem("Karic"));
        //magellan_constellation_Khamn.getSystems().add(sector.getStarSystem("prefix " + StarName));

        sector.getStarSystem("Khamn").setConstellation(magellan_constellation_Khamn);
        sector.getStarSystem("Beta " + StarName).setConstellation(magellan_constellation_Khamn);
        sector.getStarSystem("Gamma " + StarName).setConstellation(magellan_constellation_Khamn);
        sector.getStarSystem("Karic").setConstellation(magellan_constellation_Khamn);
        //sector.getStarSystem("prefix " + StarName).setConstellation(magellan_constellation_Khamn);
        
        /////////////////
        //KHAMN SYSTEM//
        /////////////////
        system_khamn.setBackgroundTextureFilename("graphics/backgrounds/background1.jpg");
		
		// create the star and generate the hyperspace anchor for this system
		// Khamn, an enormous blue giant
		PlanetAPI khamn_star = system_khamn.initStar("khamn", // unique id for this star 
			"star_red_supergiant",  // id in planets.json
			1000f, // radius (in pixels at default zoom)
                        (hsLocationX + spawnXoffset + A1Xoffset), // hyper space location x axis
                        (hsLocationY + spawnYoffset + A1Yoffset), // hyper space location y axis
                        600); // corona radius, from star edge
                                                                                    
		system_khamn.setLightColor(new Color(255, 200, 200)); // light color in entire system_khamn, affects all entities
                
                //Add some crap in here - a toxic mini-Venusoid and a rockbelt or two. Nothing past about 2400.
                PlanetAPI baphain = system_khamn.addPlanet("magellan_planet_baphain", khamn_star, "Baphain", "toxic", 270f, 75, 1800, 90f);
                
                    // Add fixed conditions to Baphain.
                    Misc.initConditionMarket(baphain);
                    baphain.getMarket().addCondition(Conditions.VERY_HOT);
                    baphain.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    baphain.getMarket().addCondition(Conditions.ORGANICS_TRACE);
                    
                SectorEntityToken khamn_buoy = system_khamn.addCustomEntity("khamn_nav_buoy", // unique id
                "Khamn Nav Buoy", // name - if null, defaultName from custom_entities.json will be used
                "nav_buoy", // type of object, defined in custom_entities.json
                "magellan_protectorate"); // faction
                khamn_buoy.setCircularOrbitPointingDown(system_khamn.getEntityById("khamn"), 90f, 1850, 90f);
                    
                // add first asteroid belt of the disk ---------------
                system_khamn.addRingBand(khamn_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 2110, 98f);
                system_khamn.addRingBand(khamn_star, "misc", "rings_dust0", 256f, 2, Color.white, 256f, 2150, 102f);
		system_khamn.addAsteroidBelt(khamn_star, 120, 2130, 300, 200, 300, Terrain.ASTEROID_BELT, "Baphain's Cry");
                
                //Add a small fuelling moon.
                PlanetAPI pariya = system_khamn.addPlanet("magellan_planet_pariya", khamn_star, "Pariya", "barren", 150f, 60, 2550, 150f);
                
                    //Add the marketplace to Pariya
                    MarketAPI pariyaMarket = AddMarketplace.addMarketplace("magellan_protectorate", pariya, null,
                        "Pariya", // name of the market
                        3, // size of the market (from the JSON)
                        new ArrayList<>(
                                Arrays.asList( // list of market conditions from martinique.json
                                        Conditions.HOT,
                                        Conditions.LOW_GRAVITY,
                                        Conditions.NO_ATMOSPHERE,
                                        Conditions.OUTPOST,
                                        Conditions.POPULATION_3)),
                        new ArrayList<>(
                                Arrays.asList( // list of industries
                                        Industries.SPACEPORT,
                                        Industries.FUELPROD,
                                        Industries.GROUNDDEFENSES,                                        
                                        Industries.POPULATION)),
                        new ArrayList<>(
                                Arrays.asList( // which submarkets to generate
                                        Submarkets.SUBMARKET_OPEN,
                                        Submarkets.SUBMARKET_STORAGE)),
                        0.3f); // tariff amount
                        pariyaMarket.addIndustry(Industries.FUELPROD, new ArrayList<>(Arrays.asList(Items.SYNCHROTRON)));
                
                        pariya.setCustomDescriptionId("planet_pariya");

                
                // Magella, a hot gas giant with several moons.
                PlanetAPI magella = system_khamn.addPlanet("magellan_planet_magella", khamn_star, "Magella", "gas_giant", 150, 750, 6400, 270f);
                    magella.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "banded"));
                    magella.getSpec().setGlowColor(new Color(235,38,8,145));
                    magella.getSpec().setUseReverseLightForGlow(true);
                    magella.getSpec().setAtmosphereThickness(0.5f);
                    magella.getSpec().setCloudRotation(15f);
                    magella.getSpec().setAtmosphereColor(new Color(138,118,255,145));
                    magella.getSpec().setPitch(30f);
                    magella.getSpec().setTilt(15f);
                    magella.applySpecChanges();             

                    // Add fixed conditions to Magella.
                    Misc.initConditionMarket(magella);
                        magella.getMarket().addCondition(Conditions.HOT);
                        magella.getMarket().addCondition(Conditions.EXTREME_WEATHER);
                        magella.getMarket().addCondition(Conditions.DENSE_ATMOSPHERE);
                        magella.getMarket().addCondition(Conditions.HIGH_GRAVITY);
                        magella.getMarket().addCondition(Conditions.VOLATILES_ABUNDANT);
                        magella.getMarket().addCondition(Conditions.ORGANICS_TRACE);
                        
                    magella.setCustomDescriptionId("planet_magella");
                        
                    //A wretched inner lava moon.
                    PlanetAPI innermoon = system_khamn.addPlanet("magellan_planet_eran", magella, "Eran", "lava", 210, 50, 1000, 90f);
                    //innermoon.setCustomDescriptionId("planet_eran");
        
                        // Add fixed conditions.
                        Misc.initConditionMarket(innermoon);
                        innermoon.getMarket().addCondition(Conditions.EXTREME_TECTONIC_ACTIVITY);
                        innermoon.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
                        innermoon.getMarket().addCondition(Conditions.VERY_HOT);
                        innermoon.getMarket().addCondition(Conditions.ORE_ABUNDANT);
                        innermoon.getMarket().addCondition(Conditions.RARE_ORE_SPARSE);
                    
                    //Jeshad, a huge, arid hive-world with a restive and violent underclass.
                        PlanetAPI jeshad = system_khamn.addPlanet("magellan_planet_jeshad", magella, "Jeshad", "arid", 30, 120, 1360, 90f);
                        jeshad.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "asharu"));
                        jeshad.getSpec().setGlowColor( new Color(255,160,30,255) );
                        jeshad.getSpec().setUseReverseLightForGlow(true);
                        jeshad.getSpec().setPitch(-15f);
                        jeshad.getSpec().setTilt(20f);
                        jeshad.applySpecChanges();
                        jeshad.setInteractionImage("illustrations", "desert_moons_ruins");
                
                    //Add the marketplace to Jeshad
                    MarketAPI jeshadMarket = AddMarketplace.addMarketplace("magellan_protectorate", jeshad, null,
                        "Jeshad", // name of the market
                        8, // size of the market (from the JSON)
                        new ArrayList<>(
                                Arrays.asList( // list of market conditions from martinique.json
                                        Conditions.HABITABLE,
                                        Conditions.EXTREME_WEATHER,
                                        Conditions.FARMLAND_ADEQUATE,
                                        Conditions.ORE_ABUNDANT,
                                        Conditions.RARE_ORE_SPARSE,
                                        Conditions.ORGANICS_COMMON,
                                        Conditions.URBANIZED_POLITY,
                                        Conditions.DISSIDENT,
                                        //Conditions.SPACEPORT,
                                        Conditions.POPULATION_8)),
                        new ArrayList<>(
                                Arrays.asList( // list of industries
                                        Industries.STARFORTRESS,
                                        Industries.MEGAPORT,
                                        Industries.FARMING,
                                        Industries.MINING,
                                        Industries.MILITARYBASE,
                                        Industries.HEAVYBATTERIES,
                                        Industries.POPULATION)),
                        new ArrayList<>(
                                Arrays.asList( // which submarkets to generate
                                        Submarkets.GENERIC_MILITARY,
                                        Submarkets.SUBMARKET_BLACK,
                                        Submarkets.SUBMARKET_OPEN,
                                        Submarkets.SUBMARKET_STORAGE)),
                        0.3f); // tariff amount
                        jeshadMarket.addIndustry(Industries.HEAVYINDUSTRY, new ArrayList<>(Arrays.asList(Items.CORRUPTED_NANOFORGE)));

                        jeshad.setCustomDescriptionId("planet_jeshad");
                        
                    //Annore, a gorgeous water moon where the aristocratic elite rule.
                    PlanetAPI annore = system_khamn.addPlanet("magellan_planet_annore", magella, "Annore", "water", 360*(float)Math.random(), 75, 2100, 105f);
                        annore.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
                        annore.getSpec().setGlowColor( new Color(215,235,255,225) );
                        annore.getSpec().setUseReverseLightForGlow(true);
                        annore.getSpec().setPitch(20f);
                        annore.applySpecChanges();      
                        annore.setInteractionImage("illustrations", "urban00");

                        //Annore Orbital, the cream of the cream.
                        SectorEntityToken annoreOrbital = system_khamn.addCustomEntity("magellan_annore_orbital", "Annore Orbital", "station_side04", "magellan_protectorate");
                        annoreOrbital.setCircularOrbitPointingDown(system_khamn.getEntityById("magellan_planet_annore"), 360*(float)Math.random(), 150, 60f);
                        annoreOrbital.setInteractionImage("illustrations", "orbital");
                        
                    // add the marketplace to Annore/Annore Orbital ---------------
                    MarketAPI annoreMarket = AddMarketplace.addMarketplace("magellan_protectorate", annore, new ArrayList<>(Arrays.asList(annoreOrbital)),
                        "Annore", // name of the market
                        5, // size of the market (from the JSON)
                        new ArrayList<>(
                                Arrays.asList( // list of market conditions from martinique.json
                                        Conditions.HABITABLE,
                                        Conditions.WATER_SURFACE,
                                        Conditions.FREE_PORT,
                                        Conditions.REGIONAL_CAPITAL,
                                        Conditions.CLOSED_IMMIGRATION,
                                        Conditions.POPULATION_5)),
                        new ArrayList<>
                                (Arrays.asList( // list of industries
                                        Industries.ORBITALSTATION,
                                        Industries.AQUACULTURE,
                                        Industries.SPACEPORT,
                                        Industries.WAYSTATION,
                                        Industries.LIGHTINDUSTRY,
                                        Industries.REFINING,
                                        Industries.HEAVYBATTERIES,
                                        Industries.POPULATION)),
                        new ArrayList<>(
                                Arrays.asList( // which submarkets to generate
                                        Submarkets.SUBMARKET_BLACK,
                                        Submarkets.SUBMARKET_OPEN,
                                        Submarkets.SUBMARKET_STORAGE)),
                        0.3f); // tariff amount
                        
                        annore.setCustomDescriptionId("planet_annore");
                        annoreOrbital.setCustomDescriptionId("station_annoreorbital");
                        
                        //add custom admin to Annore.      
//                        annoreMarket =  Global.getSector().getEconomy().getMarket("magellan_planet_annore");
//                        if (annoreMarket != null) {
//                                PersonAPI person = Global.getFactory().createPerson();
//                                person.setFaction("magellan_protectorate");
//                                person.setGender(FullName.Gender.FEMALE);
//                                person.setRankId(Ranks.FACTION_LEADER);
//                                person.setPostId(Ranks.POST_FACTION_LEADER);
//                                person.getName().setFirst("Emilie");
//                                person.getName().setLast("Kailen-Sxown");
//                                person.setPortraitSprite(Global.getSettings().getSpriteName("characters", "magellan_ladyregent"));
//                                person.getStats().setSkillLevel(Skills.SPACE_OPERATIONS, 3);
//                                person.getStats().setSkillLevel(Skills.INDUSTRIAL_PLANNING, 2);
//			
//                                annoreMarket.setAdmin(person);
//                                annoreMarket.getCommDirectory().addPerson(person, 0);
//                                annoreMarket.addPerson(person);
//                        }
                        
                        //Add a small ring.
                        system_khamn.addRingBand(magella, "misc", "rings_ice0", 256f, 1, Color.white, 256f, 3000, 90f, Terrain.RING, "Magella Ring");

            //Magella's Trojans and an abandoned sporeship opposite Magella at L3.
            
                //Trojan asteroids.
                SectorEntityToken magellaL4 = system_khamn.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                        840f, // min radius
                        1080f, // max radius
                        35, // min asteroid count
                        64, // max asteroid count
                        7f, // min asteroid radius 
                        21f, // max asteroid radius
                        "Magella L4 Trojans")); // null for default name
                
                // Jump point in L4.
                JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("khamn_inner_jump", "Khamn Bridge");
		jumpPoint1.setCircularOrbit(system_khamn.getEntityById("khamn"), 210f, 6400, 270f);
		jumpPoint1.setRelatedPlanet(annore);
		system_khamn.addEntity(jumpPoint1);
                
                SectorEntityToken magellaL5 = system_khamn.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldTerrainPlugin.AsteroidFieldParams(
                        840f, // min radius
                        1080f, // max radius
                        35, // min asteroid count
                        64, // max asteroid count
                        7f, // min asteroid radius 
                        21f, // max asteroid radius
                        "Magella L5 Trojans")); // null for default name
                
                //Stable location in L5.
                SectorEntityToken magella_l5_loc = system_khamn.addCustomEntity(null,null, "stable_location",Factions.NEUTRAL); 
		magella_l5_loc.setCircularOrbitPointingDown(khamn_star, 90f, 6400, 270f);
                
                magellaL4.setCircularOrbit(khamn_star, 210f, 6400, 270f);
                magellaL5.setCircularOrbit(khamn_star, 90f, 6400, 270f);     
                
                //Abandoned sporeship.
                SectorEntityToken sporeStation = system_khamn.addCustomEntity("magellan_sporeship", "Abandoned Fleet Sporeship", "station_sporeship_derelict", "neutral");
                sporeStation.setCircularOrbitWithSpin(system_khamn.getEntityById("khamn"), 330, 6400, 270f, 4, 14);
                sporeStation.setDiscoverable(true);
                sporeStation.setDiscoveryXP(1500f);
                sporeStation.setSensorProfile(0.25f);
                sporeStation.setCustomDescriptionId("magellan_fleet_sporeship");
                sporeStation.setInteractionImage("illustrations", "abandoned_station");
                
                sporeStation.getMemoryWithoutUpdate().set("$abandonedStation", true);
                MarketAPI market = Global.getFactory().createMarket("magellan_fleet_sporeship_market", sporeStation.getName(), 0);
                market.setPrimaryEntity(sporeStation);
                market.setFactionId(sporeStation.getFaction().getId());
                //market.addCondition(Conditions.ABANDONED_STATION);
                market.addSubmarket(Submarkets.SUBMARKET_STORAGE);
                ((StoragePlugin) market.getSubmarket(Submarkets.SUBMARKET_STORAGE).getPlugin()).setPlayerPaidToUnlock(true);
                sporeStation.setMarket(market);
               
            //Add a sensor array at Magella L2.
            SectorEntityToken magella_array = system_khamn.addCustomEntity("magella_sensor_array", // unique id
            "Magella Array", // name - if null, defaultName from custom_entities.json will be used
            "sensor_array", // type of object, defined in custom_entities.json
            "magellan_protectorate"); // faction
            magella_array.setCircularOrbitPointingDown(system_khamn.getEntityById("khamn"), 150, 9600, 270f);
                
            //Outer system should have sparse asteroids, a Neptune, pirate base.
            
                //Asteroid belt.
                system_khamn.addAsteroidBelt(khamn_star, 100, 10750, 500, 290, 310, Terrain.ASTEROID_BELT,  "Khamn Belt");
		system_khamn.addRingBand(khamn_star, "misc", "rings_dust0", 256f, 3, Color.white, 256f, 10700, 275f, null, null);
		system_khamn.addRingBand(khamn_star, "misc", "rings_dust0", 256f, 1, Color.white, 256f, 10800, 245f, null, null);
                
                //An unremarkable Neptunoid, Obilot.
                SectorEntityToken obilot = system_khamn.addPlanet("magellan_planet_obilot", khamn_star, "Obilot", "ice_giant", 360*(float)Math.random(), 250, 12800, 400f);
                
                //And throw in another little rock of a moon here.
                PlanetAPI outermoon = system_khamn.addPlanet("magellan_planet_spera", obilot, "Spera", "cryovolcanic", 180, 75, 900, 90f);
                outermoon.setCustomDescriptionId("planet_spera");
                
                    // Add fixed conditions.
                    Misc.initConditionMarket(outermoon);
                    outermoon.getMarket().addCondition(Conditions.LOW_GRAVITY);
                    outermoon.getMarket().addCondition(Conditions.NO_ATMOSPHERE);
                    outermoon.getMarket().addCondition(Conditions.VOLATILES_ABUNDANT);
                    outermoon.getMarket().addCondition(Conditions.ORE_MODERATE);
                    outermoon.getMarket().addCondition(Conditions.RARE_ORE_SPARSE);
                    outermoon.getMarket().addCondition(Conditions.RUINS_SCATTERED);

                
                //Pirate base around Obilot.
                SectorEntityToken pirStation = system_khamn.addCustomEntity("station_obilotbase", "Port Obilo", "station_side05", "pirates");
                pirStation.setCircularOrbitWithSpin(system_khamn.getEntityById("magellan_planet_obilot"), 0f, 900, 90f, 7, 21);

                    // add the marketplace to Brightheaven ---------------
                    MarketAPI pirbaseMarket = AddMarketplace.addMarketplace("pirates", pirStation, null,
                         "Port Obilo", // name of the market
                        3, // size of the market (from the JSON)
                        new ArrayList<>(
                            Arrays.asList( // list of market conditions from json
                                Conditions.FREE_PORT,
                                Conditions.STEALTH_MINEFIELDS,
                                Conditions.ORGANIZED_CRIME,
                                //Conditions.ORBITAL_STATION,
                                Conditions.POPULATION_3)),
                        new ArrayList<>
                            (Arrays.asList( // list of industries
                                Industries.ORBITALSTATION,
                                Industries.HEAVYBATTERIES,
                                Industries.SPACEPORT,
                                Industries.POPULATION)),
                        new ArrayList<>(
                            Arrays.asList( // which submarkets to generate
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE)),
                        0.12f); // tariff amount

                    pirStation.setCustomDescriptionId("station_obilotbase");

                        
                // Procgen makes you strong.
                float radiusAfter = StarSystemGenerator.addOrbitingEntities(system_khamn, khamn_star, StarAge.AVERAGE,
                        3, 6, // min/max entities to add
                        14400, // radius to start adding at 
                        4, // name offset - next planet will be <system_khamn name> <roman numeral of this parameter + 1>
                        true); // whether to use custom or system_khamn-name based names
                
                // Add a nebula to the system_khamn.
		//StarSystemGenerator.addSystemwideNebula(system_khamn, StarAge.OLD);
                
                // generates hyperspace destinations for in-system_khamn jump points
		system_khamn.autogenerateHyperspaceJumpPoints(true, true);

                cleanup(system_khamn);
                
     
        /////////////////
        //KARIC SYSTEM //
        /////////////////
        system_karic.setBackgroundTextureFilename("graphics/backgrounds/background1.jpg");

        // create the star and generate the hyperspace anchor for this system
        PlanetAPI karic_star = system_karic.initStar(
                "karic", // unique id for this star
                StarTypes.WHITE_DWARF, // id in planets.json
                350f, // radius (in pixels at default zoom)
                (hsLocationX + spawnXoffset + A4Xoffset), // hyper space location x axis
                (hsLocationY + spawnYoffset + A4Yoffset), // hyper space location y axis
                255 // corona radius, from star edge
        );

        system_karic.setLightColor(new Color(225, 225, 245)); // light color in entire system, affects all entities
        
        StarSystemGenerator.addSystemwideNebula(system_karic, magellan_constellation_Age);
        
        // an inner asteroid ring.
        system_karic.addRingBand(karic_star, "misc", "rings_ice0", 256f, 1, Color.gray, 256f, 3600, 90f, Terrain.RING, "Karic Ring");
        
        //Valca, a hellish volatile-mining world with a large dissident population.
        PlanetAPI valca = system_karic.addPlanet("magellan_planet_valca", karic_star, "Valca", "cryovolcanic", 210, 120, 4800, 180);
                
                    // Add market with conditions and industries to Valca.
                    MarketAPI valcaMarket = AddMarketplace.addMarketplace("independent", valca, null,
                        "Valca", // name of the market
                        5, // size of the market (from the JSON)
                        new ArrayList<>(
                                Arrays.asList( // list of market conditions from martinique.json
                                        Conditions.THIN_ATMOSPHERE,
                                        Conditions.COLD,
                                        Conditions.VOLATILES_PLENTIFUL,
                                        Conditions.FREE_PORT,
                                        Conditions.VICE_DEMAND,
                                        Conditions.DISSIDENT,
                                        Conditions.ORGANIZED_CRIME,
                                        Conditions.POPULATION_5)),
                        new ArrayList<>
                                (Arrays.asList( // list of industries
                                        Industries.SPACEPORT,
                                        Industries.LIGHTINDUSTRY,
                                        Industries.MINING,
                                        Industries.POPULATION)),
                        new ArrayList<>(
                                Arrays.asList( // which submarkets to generate
                                        Submarkets.SUBMARKET_BLACK,
                                        Submarkets.SUBMARKET_OPEN,
                                        Submarkets.SUBMARKET_STORAGE)),
                        0.3f); // tariff amount
                        
                        valca.setCustomDescriptionId("planet_valca");
                        
        //Valca Bastion, a Magellan military outpost to keep an eye on the troublemakers.
        SectorEntityToken valcaOrbital = system_karic.addCustomEntity("magellan_valca_orbital", "Valca Bastion", "station_side02", "magellan_protectorate");
        valcaOrbital.setCircularOrbitPointingDown(system_karic.getEntityById("magellan_planet_valca"), 360*(float)Math.random(), 360, 105f);
        valcaOrbital.setInteractionImage("illustrations", "orbital");
        
                    // Add market with conditions and industries to Valca Bastion.
                    MarketAPI valcaBastionMarket = AddMarketplace.addMarketplace("magellan_protectorate", valcaOrbital, null,
                        "Valca Bastion", // name of the market
                        3, // size of the market (from the JSON)
                        new ArrayList<>(
                                Arrays.asList( // list of market conditions
                                        Conditions.OUTPOST,
                                        Conditions.VICE_DEMAND,
                                        Conditions.POPULATION_3)),
                        new ArrayList<>
                                (Arrays.asList( // list of industries
                                        Industries.BATTLESTATION,
                                        Industries.MILITARYBASE,
                                        Industries.SPACEPORT,
                                        Industries.HEAVYBATTERIES,
                                        Industries.POPULATION)),
                        new ArrayList<>(
                                Arrays.asList( // which submarkets to generate
                                        Submarkets.SUBMARKET_BLACK,
                                        Submarkets.SUBMARKET_OPEN,
                                        Submarkets.SUBMARKET_STORAGE)),
                        0.3f); // tariff amount
                    
                        valcaOrbital.setCustomDescriptionId("station_valcabastion");
                        
        //Comm relay opposite Valca.
        SectorEntityToken valca_l3_loc = system_karic.addCustomEntity(null,null, "stable_location",Factions.NEUTRAL); 
        valca_l3_loc.setCircularOrbitPointingDown(karic_star, 30f, 4800, 180f);
        
        // Jump point in L5.
        JumpPointAPI jumpPoint2 = Global.getFactory().createJumpPoint("karic_inner_jump", "Karic Bridge");
	jumpPoint2.setCircularOrbit(system_karic.getEntityById("karic"), 150f, 4800, 180f);
	jumpPoint2.setRelatedPlanet(valca);
	system_karic.addEntity(jumpPoint2);
        
        // Stable location in L4
        SectorEntityToken valca_l4_loc = system_karic.addCustomEntity(null,null, "stable_location",Factions.NEUTRAL); 
        valca_l4_loc.setCircularOrbitPointingDown(karic_star, 270f, 4800, 180f);

        //Station assigner for a Leveller base in the outer system. Should pick a pretty location.
        
        
        float karic_Outer = StarSystemGenerator.addOrbitingEntities(
                system_karic,
                karic_star,
                magellan_constellation_Age,
                3, 5, // min/max entities to add
                6000, // radius to start adding at 
                0, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                true // whether to use custom or system-name based names  
        );

        system_karic.autogenerateHyperspaceJumpPoints(true, true);

        cleanup(system_karic);           
        
        /////////////////
        //TWO SYSTEM   //
        /////////////////
        system_two.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg");

        // create the star and generate the hyperspace anchor for this system
        PlanetAPI two_star = system_two.initStar(
                "Beta " + StarName, // unique id for this star
                StarTypes.ORANGE, // id in planets.json
                400f, // radius (in pixels at default zoom)
                (hsLocationX + spawnXoffset + A2Xoffset), // hyper space location x axis
                (hsLocationY + spawnYoffset + A2Yoffset), // hyper space location y axis
                255 // corona radius, from star edge
        );

        system_two.setLightColor(new Color(255, 225, 205)); // light color in entire system, affects all entities
        
        two_star.setName("Beta " + StarName);
        system_two.setName("Beta " + StarName + " Star System");
        
        //Huge debris field.
        

        float two_Outer = StarSystemGenerator.addOrbitingEntities(
                system_two,
                two_star,
                magellan_constellation_Age,
                3, 5, // min/max entities to add
                2000, // radius to start adding at 
                0, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                true // whether to use custom or system-name based names  
        );

        system_two.autogenerateHyperspaceJumpPoints(true, true);

        cleanup(system_two);        
        
        /////////////////
        //THREE SYSTEM //
        /////////////////
        system_three.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg");

        // create the star and generate the hyperspace anchor for this system
        PlanetAPI three_star = system_three.initStar(
                "Gamma " + StarName, // unique id for this star
                StarTypes.RED_DWARF, // id in planets.json
                300f, // radius (in pixels at default zoom)
                (hsLocationX + spawnXoffset + A3Xoffset), // hyper space location x axis
                (hsLocationY + spawnYoffset + A3Yoffset), // hyper space location y axis
                255 // corona radius, from star edge
        );

        system_three.setLightColor(new Color(255, 225, 205)); // light color in entire system, affects all entities
        
        system_three.setName("Gamma " + StarName + " Star System");

        StarSystemGenerator.addSystemwideNebula(system_three, magellan_constellation_Age);
                
        float three_Outer = StarSystemGenerator.addOrbitingEntities(
                system_three,
                three_star,
                magellan_constellation_Age,
                4, 6, // min/max entities to add
                700, // radius to start adding at 
                0, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                true // whether to use custom or system-name based names  
        );

        system_three.autogenerateHyperspaceJumpPoints(true, true);

        cleanup(system_three);
        
        }
        
    void cleanup(StarSystemAPI system) {
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius * 0.5f, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }

    private void addDerelict(StarSystemAPI system_khamn, 
            SectorEntityToken focus, 
            String variantId, 
            ShipRecoverySpecial.ShipCondition condition, 
            float orbitRadius, 
            boolean recoverable) {
        DerelictShipEntityPlugin.DerelictShipData params = new DerelictShipEntityPlugin.DerelictShipData(new ShipRecoverySpecial.PerShipData(variantId, condition), false);
        SectorEntityToken ship = BaseThemeGenerator.addSalvageEntity(system_khamn, Entities.WRECK, Factions.NEUTRAL, params);
        ship.setDiscoverable(true);

        float orbitDays = orbitRadius / (10f + (float) Math.random() * 5f);
        ship.setCircularOrbit(focus, (float) Math.random() * 360f, orbitRadius, orbitDays);

        if (recoverable) {
            SalvageSpecialAssigner.ShipRecoverySpecialCreator creator = new SalvageSpecialAssigner.ShipRecoverySpecialCreator(null, 0, 0, false, null, null);
            Misc.setSalvageSpecial(ship, creator.createSpecial(ship, null));
        }
    }
}
