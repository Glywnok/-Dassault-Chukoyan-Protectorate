package data.missions.dcp_magellan_paythepiper;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "FSS", FleetGoal.ATTACK, false, 5);
      api.initFleet(FleetSide.ENEMY, "HSS", FleetGoal.ATTACK, true);
      api.setFleetTagline(FleetSide.PLAYER, "Task Force Dalganos");
      api.setFleetTagline(FleetSide.ENEMY, "Hegemony Detachment");
      api.addBriefingItem("Destroy the Hegemony detachment.");
      api.addBriefingItem("FSS Dalganos must survive.");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_supportdestroyer_elite", FleetMemberType.SHIP, "FSS Knifepoint", true);
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_battleship_startiger_elite", FleetMemberType.SHIP, "FSS Dalganos", false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_battleship_line", FleetMemberType.SHIP, "FSS Riva", false).getCaptain().setPersonality("reckless");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_carrier_startiger_std", FleetMemberType.SHIP, false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_carrier_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_linedestroyer_std", FleetMemberType.SHIP, false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_linefrigate_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_linefrigate_std", FleetMemberType.SHIP, false);
      api.defeatOnShipLoss("FSS Dalganos");
      api.addToFleet(FleetSide.ENEMY, "onslaught_Elite", FleetMemberType.SHIP, "HSS Implacable", false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.ENEMY, "onslaught_Outdated", FleetMemberType.SHIP, false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.ENEMY, "onslaught_Outdated", FleetMemberType.SHIP, false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.ENEMY, "mora_Assault", FleetMemberType.SHIP, false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.ENEMY, "enforcer_Elite", FleetMemberType.SHIP, false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.ENEMY, "lasher_Standard", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "lasher_Standard", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "lasher_Standard", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "hound_hegemony_Standard", FleetMemberType.SHIP, false).getCaptain().setPersonality("cautious");
      float width = 24000.0F;
      float height = 18000.0F;
      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;
      api.addObjective(minX + width * 0.15F + 3000.0F, minY + height * 0.3F + 1000.0F, "nav_buoy");
      api.addObjective(minX + width * 0.8F - 2000.0F, minY + height * 0.3F + 1000.0F, "comm_relay");
      api.addObjective(minX + width * 0.85F - 3000.0F, minY + height * 0.7F - 1000.0F, "nav_buoy");
      api.addObjective(minX + width * 0.6F - 1000.0F, minY + height * 0.6F, "sensor_array");
      api.addAsteroidField(minX, minY + height / 2.0F, 0.0F, 8000.0F, 20.0F, 70.0F, 100);
      api.addPlanet(0.0F, 0.0F, 240.0F, "star_browndwarf", 180.0F, true);
   }
}
