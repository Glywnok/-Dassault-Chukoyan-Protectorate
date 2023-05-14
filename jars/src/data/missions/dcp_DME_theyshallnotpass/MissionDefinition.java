package data.missions.istl_theyshallnotpass;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "DMS", FleetGoal.ATTACK, false);
      api.initFleet(FleetSide.ENEMY, "NIS", FleetGoal.ATTACK, true);
      api.setFleetTagline(FleetSide.PLAYER, "3rd Fleet");
      api.setFleetTagline(FleetSide.ENEMY, "The Guarantor's Guard");
      api.addBriefingItem("Destroy the enemy.");
      api.addBriefingItem("Don't lose the DMS Talleyrand");
      api.addToFleet(FleetSide.PLAYER, "istl_jeannedarc_std", FleetMemberType.SHIP, "DMS Talleyrand", true);
      api.addToFleet(FleetSide.PLAYER, "istl_baikal_std", FleetMemberType.SHIP, "DMS Konev", false);
      api.addToFleet(FleetSide.PLAYER, "istl_leyte_std", FleetMemberType.SHIP, "DMS Hougoumont", false);
      api.addToFleet(FleetSide.PLAYER, "istl_stoat_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.PLAYER, "istl_stoat_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "conquest_Standard", FleetMemberType.SHIP, "NIS Imperator", false);
      api.addToFleet(FleetSide.ENEMY, "dominator_d_Assault", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "drover_Strike", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "condor_Strike", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "falcon_CS", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "hammerhead_Balanced", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "hammerhead_d_CS", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "lasher_d_CS", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "lasher_d_CS", FleetMemberType.SHIP, false);
      api.defeatOnShipLoss("DMS Talleyrand");
      float width = 15000.0F;
      float height = 18000.0F;
      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;
      api.addObjective(minX + width * 0.4F + 1000.0F, minY + height * 0.4F, "sensor_array");
      api.addObjective(minX + width * 0.8F - 2000.0F, minY + height * 0.3F + 1000.0F, "comm_relay");
      api.addObjective(minX + width * 0.85F - 3000.0F, minY + height * 0.7F - 1000.0F, "nav_buoy");
      api.addObjective(minX + width * 0.6F - 1000.0F, minY + height * 0.6F, "sensor_array");
      api.addPlanet(0.0F, 0.0F, 120.0F, "water", 100.0F, true);
   }
}
