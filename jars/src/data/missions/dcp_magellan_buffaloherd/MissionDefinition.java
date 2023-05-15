package data.missions.dcp_magellan_buffaloherd;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "HS", FleetGoal.ATTACK, false);
      api.initFleet(FleetSide.ENEMY, "SS", FleetGoal.ATTACK, true);
      api.setFleetTagline(FleetSide.PLAYER, "The boys");
      api.setFleetTagline(FleetSide.ENEMY, "A cold one");
      api.addBriefingItem("Show these rustlers how we deal with someone horning in on our perfectly legitimate illegal activities.");
      api.addBriefingItem("Don't lose the 'Stranger Than Friction' - it's too big a loss to replace");
      boolean testMode = false;
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_linedestroyer_theherd_support", FleetMemberType.SHIP, "HS Brisket With Attitude", true);
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_herdcarrier_std", FleetMemberType.SHIP, "HS Stranger Than Friction", false);
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_ltfreight_theherd_std", FleetMemberType.SHIP, "HS Milk Run", false);
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_supportfrigate_theherd_std", FleetMemberType.SHIP, "HS Half Calf", false);
      api.addToFleet(FleetSide.PLAYER, "dcp_magellan_missilefrigate_theherd_std", FleetMemberType.SHIP, "HS Look Ma, No Hands!", false);
      api.addToFleet(FleetSide.ENEMY, "colossus3_Pirate", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "enforcer_d_pirates_Strike", FleetMemberType.SHIP, "SS Moose Juice", false);
      api.addToFleet(FleetSide.ENEMY, "manticore_pirates_Assault", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "mule_d_pirates_Smuggler", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "buffalo2_FS", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "buffalo2_FS", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "buffalo2_FS", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "cerberus_d_pirates_Standard", FleetMemberType.SHIP, false);
      api.defeatOnShipLoss("HS Stranger Than Friction");
      float width = 9600.0F;
      float height = 14400.0F;
      if (testMode) {
         width += 4800.0F;
         height += 7200.0F;
      }

      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;
      api.addAsteroidField(minX, minY + height / 2.0F, 0.0F, 8000.0F, 20.0F, 70.0F, 100);
      api.addPlanet(0.0F, 0.0F, 50.0F, "star_red_giant", 250.0F, true);
   }
}
