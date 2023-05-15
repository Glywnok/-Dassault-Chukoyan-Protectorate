package data.missions.dcp_DME_threecaballeros;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "", FleetGoal.ATTACK, false);
      api.initFleet(FleetSide.ENEMY, "", FleetGoal.ATTACK, true);
      api.setFleetTagline(FleetSide.PLAYER, "Three humble gentlemen, wandering the stars.");
      api.setFleetTagline(FleetSide.ENEMY, "A nasty bunch of rotten, weaselly scoundrels.");
      api.addBriefingItem("Destroy or drive off the attacking pirate swarm.");
      api.addBriefingItem("Don't lose 'Pup Tentacle' - it's the centerpiece of your fleet.");
      api.addBriefingItem("You've got the advantage in firepower and durability. Exploit it.");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_tunguska_fsup", FleetMemberType.SHIP, "Pup Tentacle", true);
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_wanderer_std", FleetMemberType.SHIP, "Peaches En Regalia", false);
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_tereshkova_tech", FleetMemberType.SHIP, "Roxy & Elsewhere", false);
      api.addToFleet(FleetSide.ENEMY, "hammerhead_Overdriven", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "centurion_Assault", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "dcp_DME_sevastopol_mk1_std", FleetMemberType.SHIP, "I'm The Slime", false);
      api.addToFleet(FleetSide.ENEMY, "dcp_DME_naja_mk1_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "dcp_DME_naja_mk1_brawl", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "dcp_DME_naja_mk1_brawl", FleetMemberType.SHIP, false);
      api.defeatOnShipLoss("Pup Tentacle");
      float width = 12000.0F;
      float height = 12000.0F;
      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;
      api.addAsteroidField(minX, minY + height / 2.0F, 0.0F, 8000.0F, 20.0F, 70.0F, 100);
      api.addPlanet(0.0F, 0.0F, 50.0F, "star_red_giant", 250.0F, true);
   }
}
