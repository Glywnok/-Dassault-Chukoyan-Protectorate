package data.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin.DerelictShipData;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin.DerelictType;
import com.fs.starfarer.api.impl.campaign.procgen.DefenderDataOverride;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.DerelictThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner.ShipRecoverySpecialCreator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner.SpecialCreationContext;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.PerShipData;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.ShipCondition;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldSource;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import java.awt.Color;
import java.util.Random;

public class Hejaz {
   public void generate(SectorAPI sector) {
      StarSystemAPI system = sector.createStarSystem("Hejaz");
      LocationAPI hyper = Global.getSector().getHyperspace();
      system.setBackgroundTextureFilename("graphics/backgrounds/background1.jpg");
      PlanetAPI hejaz_star = system.initStar("hejaz", "star_red_dwarf", 300.0F, 150.0F, 3.0F, 0.2F, 1.0F);
      system.setLightColor(new Color(240, 225, 200));
      system.addTag("theme_core");
      system.addTag("theme_core_unpopulated");
      system.addTag("theme_hidden");
      system.addTag("theme_breakers_no_fleets");
      SectorEntityToken gate = system.addCustomEntity("hejaz_gate", "Hejaz Gate", "inactive_gate", (String)null);
      gate.setCircularOrbit(system.getEntityById("hejaz"), 360.0F * (float)Math.random(), 1500.0F, 150.0F);
      system.addAsteroidBelt(hejaz_star, 100, 2250.0F, 500.0F, 290.0F, 310.0F, "asteroid_belt", "Hejaz Inner Belt");
      system.addRingBand(hejaz_star, "misc", "rings_dust0", 256.0F, 3, Color.white, 256.0F, 2200.0F, 275.0F, (String)null, (String)null);
      system.addRingBand(hejaz_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 2300.0F, 245.0F, (String)null, (String)null);
      DebrisFieldParams params1 = new DebrisFieldParams(450.0F, 1.5F, 1.0E7F, 0.0F);
      params1.source = DebrisFieldSource.MIXED;
      params1.baseSalvageXP = 500L;
      SectorEntityToken debrisBeta1 = Misc.addDebrisField(system, params1, StarSystemGenerator.random);
      debrisBeta1.setSensorProfile(1000.0F);
      debrisBeta1.setDiscoverable(true);
      debrisBeta1.setCircularOrbit(hejaz_star, 360.0F * (float)Math.random(), 2250.0F, 150.0F);
      debrisBeta1.setId("hejaz_debris1");
      this.addDerelict(system, hejaz_star, "istl_tereshkova_export", ShipCondition.GOOD, 2250.0F, true);
      system.addAsteroidBelt(hejaz_star, 60, 3250.0F, 500.0F, 290.0F, 310.0F, "asteroid_belt", "Hejaz Middle Belt");
      system.addRingBand(hejaz_star, "misc", "rings_dust0", 256.0F, 1, Color.white, 256.0F, 3200.0F, 360.0F, (String)null, (String)null);
      system.addRingBand(hejaz_star, "misc", "rings_ice0", 256.0F, 2, Color.white, 256.0F, 3300.0F, 300.0F, (String)null, (String)null);
      DebrisFieldParams params2 = new DebrisFieldParams(300.0F, 1.0F, 1.0E7F, 0.0F);
      params2.source = DebrisFieldSource.MIXED;
      params2.baseSalvageXP = 500L;
      SectorEntityToken debrisBeta2 = Misc.addDebrisField(system, params2, StarSystemGenerator.random);
      debrisBeta2.setSensorProfile(1000.0F);
      debrisBeta2.setDiscoverable(true);
      debrisBeta2.setCircularOrbit(hejaz_star, 360.0F * (float)Math.random(), 3250.0F, 240.0F);
      debrisBeta2.setId("hejaz_debris2");
      PlanetAPI qarib = system.addPlanet("istl_planet_qarib", hejaz_star, "Qarib", "barren-desert", 240.0F, 75.0F, 4000.0F, 180.0F);
      qarib.getSpec().setCloudRotation(20.0F);
      qarib.applySpecChanges();
      qarib.setCustomDescriptionId("planet_qarib");
      Misc.initConditionMarket(qarib);
      qarib.getMarket().addCondition("thin_atmosphere");
      qarib.getMarket().addCondition("low_gravity");
      qarib.getMarket().addCondition("hot");
      qarib.getMarket().addCondition("ore_sparse");
      qarib.getMarket().getFirstCondition("ore_sparse").setSurveyed(true);
      JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("hejaz_jump", "Hejaz Bridge");
      jumpPoint1.setCircularOrbit(system.getEntityById("hejaz"), 180.0F, 4000.0F, 180.0F);
      jumpPoint1.setRelatedPlanet(qarib);
      system.addEntity(jumpPoint1);
      SectorEntityToken hejaz_loc = system.addCustomEntity((String)null, (String)null, "stable_location", "neutral");
      hejaz_loc.setCircularOrbitPointingDown(hejaz_star, 0.0F, 4000.0F, 180.0F);
      system.addRingBand(hejaz_star, "misc", "rings_dust0", 256.0F, 3, Color.white, 256.0F, 4800.0F, 300.0F, "ring", "Hejaz Dust Band");
      SectorEntityToken stationDerelict1 = DerelictThemeGenerator.addSalvageEntity(system, "istl_bladebreaker_habitat", "blade_breakers");
      stationDerelict1.setId("hejaz_derelict1");
      stationDerelict1.setCircularOrbit(hejaz_star, 360.0F * (float)Math.random(), 4800.0F, 270.0F);
      Misc.setDefenderOverride(stationDerelict1, new DefenderDataOverride("blade_breakers", 1.0F, 8.0F, 21.0F));
      StarSystemGenerator.addOrbitingEntities(system, hejaz_star, StarAge.OLD, 0, 1, 6000.0F, 1, true);
      system.autogenerateHyperspaceJumpPoints(true, true);
      HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin)Misc.getHyperspaceTerrain().getPlugin();
      NebulaEditor editor = new NebulaEditor(plugin);
      float minRadius = plugin.getTileSize() * 2.0F;
      float radius = system.getMaxRadiusInHyperspace();
      editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0F, radius + minRadius, 0.0F, 360.0F);
      editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0F, radius + minRadius, 0.0F, 360.0F, 0.25F);
   }

   private void addDerelict(StarSystemAPI system, SectorEntityToken focus, String variantId, ShipCondition condition, float orbitRadius, boolean recoverable) {
      DerelictShipData params = new DerelictShipData(new PerShipData(variantId, condition), false);
      SectorEntityToken ship = BaseThemeGenerator.addSalvageEntity(system, "wreck", "neutral", params);
      ship.setDiscoverable(true);
      float orbitDays = orbitRadius / (10.0F + (float)Math.random() * 5.0F);
      ship.setCircularOrbit(focus, (float)Math.random() * 360.0F, orbitRadius, orbitDays);
      if (recoverable) {
         ShipRecoverySpecialCreator creator = new ShipRecoverySpecialCreator((Random)null, 0, 0, false, (DerelictType)null, (WeightedRandomPicker)null);
         Misc.setSalvageSpecial(ship, creator.createSpecial(ship, (SpecialCreationContext)null));
      }

   }
}
