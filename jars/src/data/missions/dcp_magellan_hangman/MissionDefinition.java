package data.missions.dcp_magellan_hangman;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "FSS", FleetGoal.ATTACK, false, 3);
      api.initFleet(FleetSide.ENEMY, "SS", FleetGoal.ATTACK, true, 8);
      api.setFleetTagline(FleetSide.PLAYER, "Blackcollar Response Fleet");
      api.setFleetTagline(FleetSide.ENEMY, "Valca Herd and local traitors");
      api.addBriefingItem("Destroy the Herd fleet and run down any survivors.");
      api.addBriefingItem("Kill Maxau Sxown, who's visiting the Herdmaster on SS Verdant Vision.");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_fastdestroyer_blackcollar_elite", FleetMemberType.SHIP, "FSS Ring Of Swords", true);
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_lightcruiser_blackcollar_elite", FleetMemberType.SHIP, "FSS Three Hearts, Three Suns", false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_patroldestroyer_blackcollar_elite", FleetMemberType.SHIP, false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_supportdestroyer_blackcollar_elite", FleetMemberType.SHIP, "FSS Knight Of Cups", false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_supportdestroyer_blackcollar_elite", FleetMemberType.SHIP, "FSS Queen Of Clubs", false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_supportdestroyer_blackcollar_elite", FleetMemberType.SHIP, "FSS Knave Of Wands", false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_phasefrig_blackcollar_attack", FleetMemberType.SHIP, false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_herdcarrier_std", FleetMemberType.SHIP, "SS Verdant Vision", false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_linedestroyer_theherd_support", FleetMemberType.SHIP, false).getCaptain().setPersonality("reckless");
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_ltfreight_theherd_std", FleetMemberType.SHIP, false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_ltfreight_theherd_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_missilefrigate_theherd_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_supportfrigate_theherd_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_phasefrig_theherd_std", FleetMemberType.SHIP, false).getCaptain().setPersonality("reckless");
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_cruiser_obsolete", FleetMemberType.SHIP, false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_linedestroyer_std", FleetMemberType.SHIP, false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.ENEMY, "dcp_magellan_supportfrigate_std", FleetMemberType.SHIP, false);
      api.defeatOnShipLoss("SS Verdant Vision");
      float width = 15000.0F;
      float height = 12000.0F;
      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;

      for(int i = 0; i < 7; ++i) {
         float x = (float)Math.random() * width - width / 2.0F;
         float y = (float)Math.random() * height - height / 2.0F;
         float radius = 100.0F + (float)Math.random() * 800.0F;
         api.addNebula(x, y, radius);
      }

      api.addObjective(minX + width * 0.7F, minY + height * 0.25F, "nav_buoy");
      api.addObjective(minX + width * 0.8F, minY + height * 0.75F, "nav_buoy");
      api.addObjective(minX + width * 0.2F, minY + height * 0.25F, "sensor_array");
   }
}
