package data.missions.dcp_magellan_paythepiper;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Personalities;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "FSS", FleetGoal.ATTACK, false, 5);
		api.initFleet(FleetSide.ENEMY, "HSS", FleetGoal.ATTACK, true);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Task Force Dalganos");
		api.setFleetTagline(FleetSide.ENEMY, "Hegemony Detachment");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Destroy the Hegemony detachment.");
                api.addBriefingItem("FSS Dalganos must survive.");
		
		// Set up the player's fleet
		api.addToFleet(FleetSide.PLAYER, "magellan_supportdestroyer_elite", FleetMemberType.SHIP, "FSS Knifepoint", true);
		api.addToFleet(FleetSide.PLAYER, "magellan_battleship_elite", FleetMemberType.SHIP, "FSS Dalganos", false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		api.addToFleet(FleetSide.PLAYER, "magellan_battleship_line", FleetMemberType.SHIP, "FSS Riva", false).getCaptain().setPersonality(Personalities.RECKLESS);
		api.addToFleet(FleetSide.PLAYER, "magellan_carrier_std", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.STEADY);
		api.addToFleet(FleetSide.PLAYER, "magellan_carrier_std", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "magellan_linedestroyer_std", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.STEADY);
		api.addToFleet(FleetSide.PLAYER, "magellan_linefrigate_std", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.PLAYER, "magellan_linefrigate_std", FleetMemberType.SHIP, false);
		
		// Mark fleet flagship as essential
		api.defeatOnShipLoss("FSS Dalganos");
		
		// Set up the enemy fleet
		api.addToFleet(FleetSide.ENEMY, "onslaught_Elite", FleetMemberType.SHIP, "HSS Implacable", false).getCaptain().setPersonality(Personalities.STEADY);
		api.addToFleet(FleetSide.ENEMY, "onslaught_Outdated", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		api.addToFleet(FleetSide.ENEMY, "onslaught_Outdated", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		api.addToFleet(FleetSide.ENEMY, "mora_Assault", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.STEADY);
                api.addToFleet(FleetSide.ENEMY, "enforcer_Elite", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.STEADY);
		api.addToFleet(FleetSide.ENEMY, "lasher_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "lasher_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "hound_hegemony_Standard", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.CAUTIOUS);
                
                // Set up the map.
		float width = 24000f;
		float height = 18000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		api.addObjective(minX + width * 0.15f + 3000, minY + height * 0.3f + 1000, "nav_buoy");
		api.addObjective(minX + width * 0.8f - 2000, minY + height * 0.3f + 1000, "comm_relay");
		
		api.addObjective(minX + width * 0.85f - 3000, minY + height * 0.7f - 1000, "nav_buoy");
		api.addObjective(minX + width * 0.6f - 1000, minY + height * 0.6f, "sensor_array");
		
		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		
		api.addPlanet(0, 0, 240f, StarTypes.BROWN_DWARF, 180f, true);
		
	}

}