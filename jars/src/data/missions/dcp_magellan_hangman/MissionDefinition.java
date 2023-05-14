package data.missions.dcp_magellan_hangman;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Personalities;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets
		api.initFleet(FleetSide.PLAYER, "FSS", FleetGoal.ATTACK, true, 3);
		api.initFleet(FleetSide.ENEMY, "SS", FleetGoal.ATTACK, false, 5);

		// Set a blurb for each fleet
		api.setFleetTagline(FleetSide.PLAYER, "Blackcollar Response Fleet");
		api.setFleetTagline(FleetSide.ENEMY, "Valca Herd and local traitors");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Destroy the Herd fleet and run down any survivors.");
                api.addBriefingItem("Kill Maxau Sxown, who's visiting the Herdmaster on SS Verdant Vision.");
		
		// Set up the player's fleet
		api.addToFleet(FleetSide.PLAYER, "dcp_magellan_fastdestroyer_blackcollar_elite", FleetMemberType.SHIP, "FSS Ring Of Swords", true);
                // Cruiser
		api.addToFleet(FleetSide.PLAYER, "dcp_magellan_lightcruiser_blackcollar_elite", FleetMemberType.SHIP, "FSS Three Hearts, Three Suns", false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
                // Destroyers
		api.addToFleet(FleetSide.PLAYER, "dcp_magellan_patroldestroyer_blackcollar_elite", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.STEADY);
		api.addToFleet(FleetSide.PLAYER, "dcp_magellan_supportdestroyer_blackcollar_elite", FleetMemberType.SHIP, "FSS Knight Of Cups", false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		api.addToFleet(FleetSide.PLAYER, "dcp_magellan_supportdestroyer_blackcollar_elite", FleetMemberType.SHIP, "FSS Queen Of Clubs", false).getCaptain().setPersonality(Personalities.STEADY);
		api.addToFleet(FleetSide.PLAYER, "dcp_magellan_supportdestroyer_blackcollar_elite", FleetMemberType.SHIP, "FSS Knave Of Wands", false).getCaptain().setPersonality(Personalities.STEADY);
                // Frigates
		api.addToFleet(FleetSide.PLAYER, "dcp_magellan_phasefrig_blackcollar_attack", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		
		// Set up the enemy fleet
		api.addToFleet(FleetSide.ENEMY, "dcp_magellan_carrier_theherd_std", FleetMemberType.SHIP, "SS Verdant Vision", false).getCaptain().setPersonality(Personalities.CAUTIOUS);
		// Herd ships
                api.addToFleet(FleetSide.ENEMY, "dcp_magellan_linedestroyer_theherd_support", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.RECKLESS);
		api.addToFleet(FleetSide.ENEMY, "dcp_magellan_linedestroyer_theherd_support", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		api.addToFleet(FleetSide.ENEMY, "dcp_magellan_supportfrigate_theherd_std", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "dcp_magellan_supportfrigate_theherd_std", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "dcp_magellan_phasefrig_theherd_std", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.RECKLESS);
                // Traitor garrison
                api.addToFleet(FleetSide.ENEMY, "dcp_magellan_cruiser_obsolete", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.AGGRESSIVE);
		api.addToFleet(FleetSide.ENEMY, "dcp_magellan_linedestroyer_std", FleetMemberType.SHIP, false).getCaptain().setPersonality(Personalities.STEADY);
		api.addToFleet(FleetSide.ENEMY, "dcp_magellan_supportfrigate_std", FleetMemberType.SHIP, false);
                
		// Mark fleet flagship as essential
		api.defeatOnShipLoss("SS Verdant Vision");
                
                // Set up the map.
		float width = 15000f;
		float height = 12000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		// All the addXXX methods take a pair of coordinates followed by data for
		// whatever object is being added.
		
		// And a few random ones to spice up the playing field.
		// A similar approach can be used to randomize everything
		// else, including fleet composition.
		for (int i = 0; i < 7; i++) {
			float x = (float) Math.random() * width - width/2;
			float y = (float) Math.random() * height - height/2;
			float radius = 100f + (float) Math.random() * 800f; 
			api.addNebula(x, y, radius);
		}
		
		// Add objectives. These can be captured by each side
		// and provide stat bonuses and extra command points to
		// bring in reinforcements.
		// Reinforcements only matter for large fleets - in this
		// case, assuming a 100 command point battle size,
		// both fleets will be able to deploy fully right away.

		api.addObjective(minX + width * 0.7f, minY + height * 0.25f, "nav_buoy");
		api.addObjective(minX + width * 0.8f, minY + height * 0.75f, "nav_buoy");
		api.addObjective(minX + width * 0.2f, minY + height * 0.25f, "sensor_array");
		
		
	}

}
