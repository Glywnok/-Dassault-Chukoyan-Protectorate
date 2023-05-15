package data.missions.dcp_DME_goosechase;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "DMS", FleetGoal.ATTACK, false);
      api.initFleet(FleetSide.ENEMY, "", FleetGoal.ATTACK, true);
      api.setFleetTagline(FleetSide.PLAYER, "Snow Goose Test Group");
      api.setFleetTagline(FleetSide.ENEMY, "Automated Test Targets");
      api.addBriefingItem("Destroy the enemy fleet.");
      api.addBriefingItem("Don't lose the prototype. We'll bill you.");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_snowgoose_std", FleetMemberType.SHIP, "DMS Incontrovertible", true);
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_samoyed_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_samoyed_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "dominator_Assault", FleetMemberType.SHIP, "Incapable Of Listening To Reason", false);
      api.addToFleet(FleetSide.ENEMY, "enforcer_Assault", FleetMemberType.SHIP, "Smooth Jazz Will Be Deployed", false);
      api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "The Part Where He Kills You", false);
      api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, "Android Hell Is A Real Place", false);
      api.addToFleet(FleetSide.ENEMY, "lasher_Assault", FleetMemberType.SHIP, "I Guess I Haven't Killed You Yet", false);
      api.addToFleet(FleetSide.ENEMY, "condor_Support", FleetMemberType.SHIP, "Making A Note Here, Huge Success", false);
      api.defeatOnShipLoss("DMS Incontrovertible");
      float width = 24000.0F;
      float height = 18000.0F;
      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;

      for(int i = 0; i < 25; ++i) {
         float x = (float)Math.random() * width - width / 2.0F;
         float y = (float)Math.random() * height - height / 2.0F;
         float radius = 1000.0F + (float)Math.random() * 1000.0F;
         api.addNebula(x, y, radius);
      }

      api.addNebula(minX + width * 0.8F - 2000.0F, minY + height * 0.4F, 2000.0F);
      api.addNebula(minX + width * 0.8F - 2000.0F, minY + height * 0.5F, 2000.0F);
      api.addNebula(minX + width * 0.8F - 2000.0F, minY + height * 0.6F, 2000.0F);
      api.addObjective(minX + width * 0.4F + 1000.0F, minY + height * 0.4F, "sensor_array");
      api.addObjective(minX + width * 0.8F - 2000.0F, minY + height * 0.3F + 1000.0F, "comm_relay");
      api.addObjective(minX + width * 0.85F - 3000.0F, minY + height * 0.7F - 1000.0F, "nav_buoy");
      api.addObjective(minX + width * 0.2F + 2000.0F, minY + height * 0.7F - 1000.0F, "comm_relay");
      api.addAsteroidField(minX, minY + height * 0.5F, 0.0F, height, 20.0F, 70.0F, 50);
   }
}
