package data.missions.dcp_DME_mothershipconnection;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "DMS", FleetGoal.ATTACK, false);
      api.initFleet(FleetSide.ENEMY, "", FleetGoal.ATTACK, true);
      api.setFleetTagline(FleetSide.PLAYER, "Tech-mining Group 'Star Child'");
      api.setFleetTagline(FleetSide.ENEMY, "Funkless Raiders");
      api.addBriefingItem("Destroy the enemy carriers.");
      api.addBriefingItem("Don't lose your own carriers and command ship.");
      api.addBriefingItem("Stay frosty, stay funky.");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_mindanao_std", FleetMemberType.SHIP, "Tear The Roof Off The Sucker", true);
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_tereshkova_std", FleetMemberType.SHIP, "The Electric Spanking of War Babies", false);
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_leyte_std", FleetMemberType.SHIP, "Night of the Thumpasorus Peoples", false);
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_samoyed_elite", FleetMemberType.SHIP, "Bop Gunner", false);
      api.addToFleet(FleetSide.ENEMY, "heron_Strike", FleetMemberType.SHIP, "The Man", false);
      api.addToFleet(FleetSide.ENEMY, "drover_Strike", FleetMemberType.SHIP, "Tin God", false);
      api.addToFleet(FleetSide.ENEMY, "drover_Strike", FleetMemberType.SHIP, "Unfunky UFO", false);
      api.addToFleet(FleetSide.ENEMY, "mule_Standard", FleetMemberType.SHIP, "Volume Control", false);
      api.addToFleet(FleetSide.ENEMY, "centurion_Assault", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "centurion_Assault", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "vigilance_Standard", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "vigilance_Standard", FleetMemberType.SHIP, false);
      api.defeatOnShipLoss("Tear The Roof Off The Sucker");
      float width = 12000.0F;
      float height = 12000.0F;
      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;
      api.addAsteroidField(minX, minY + height / 2.0F, 0.0F, 8000.0F, 15.0F, 60.0F, 90);
      api.addPlanet(0.0F, 0.0F, 50.0F, "star_red_dwarf", 250.0F, true);
   }
}
