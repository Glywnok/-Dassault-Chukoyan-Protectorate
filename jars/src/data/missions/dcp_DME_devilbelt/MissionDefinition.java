package data.missions.dcp_DME_devilbelt;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.BattleCreationContext;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.combat.EscapeRevealPlugin;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "SS", FleetGoal.ESCAPE, false, 5);
      api.initFleet(FleetSide.ENEMY, "BBS", FleetGoal.ATTACK, true, 5);
      api.setFleetTagline(FleetSide.PLAYER, "Contract Fleet 774-B");
      api.setFleetTagline(FleetSide.ENEMY, "Unknown Adversary");
      api.addBriefingItem("SS Just A Working Girl (and your rescued miner) must survive");
      api.addBriefingItem("At least 25% of your mining fleet must escape");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_tereshkova_export", FleetMemberType.SHIP, "SS Goats On Toast", true);
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_wanderer_std", FleetMemberType.SHIP, false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_naja_export", FleetMemberType.SHIP, false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_sevastopol_std", FleetMemberType.SHIP, "SS Just A Working Girl", false).getCaptain().setPersonality("cautious");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_centaur_export", FleetMemberType.SHIP, false).getCaptain().setPersonality("steady");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_puddlejumper_export", FleetMemberType.SHIP, false).getCaptain().setPersonality("timid");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_puddlejumper_export", FleetMemberType.SHIP, false).getCaptain().setPersonality("timid");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_puddlejumper_export", FleetMemberType.SHIP, false).getCaptain().setPersonality("timid");
      api.addToFleet(FleetSide.PLAYER, "dcp_DME_puddlejumper_export", FleetMemberType.SHIP, false).getCaptain().setPersonality("timid");
      api.defeatOnShipLoss("SS Just A Working Girl");
      api.addToFleet(FleetSide.ENEMY, "medusa_Attack", FleetMemberType.SHIP, "IMS Chastain", false).getCaptain().setPersonality("reckless");
      api.addToFleet(FleetSide.ENEMY, "dcp_DME_imp_assault", FleetMemberType.SHIP, false).getCaptain().setPersonality("reckless");
      api.addToFleet(FleetSide.ENEMY, "dcp_DME_imp_assault", FleetMemberType.SHIP, false).getCaptain().setPersonality("reckless");
      api.addToFleet(FleetSide.ENEMY, "dcp_DME_imp_support", FleetMemberType.SHIP, false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.ENEMY, "dcp_DME_imp_support", FleetMemberType.SHIP, false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.ENEMY, "tempest_Attack", FleetMemberType.SHIP, false).getCaptain().setPersonality("aggressive");
      api.addToFleet(FleetSide.ENEMY, "dcp_DME_snakeeye_cqb", FleetMemberType.SHIP, false);
      float width = 15000.0F;
      float height = 27000.0F;
      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;

      for(int i = 0; i < 15; ++i) {
         float x = (float)Math.random() * width - width / 2.0F;
         float y = (float)Math.random() * height - height / 2.0F;
         float radius = 100.0F + (float)Math.random() * 900.0F;
         api.addNebula(x, y, radius);
      }

      api.addNebula(minX + width * 0.8F, minY + height * 0.4F, 2000.0F);
      api.addNebula(minX + width * 0.8F, minY + height * 0.5F, 2000.0F);
      api.addNebula(minX + width * 0.8F, minY + height * 0.6F, 2000.0F);
      api.addObjective(minX + width * 0.8F, minY + height * 0.4F, "sensor_array");
      api.addObjective(minX + width * 0.8F, minY + height * 0.6F, "nav_buoy");
      api.addObjective(minX + width * 0.3F, minY + height * 0.3F, "nav_buoy");
      api.addObjective(minX + width * 0.3F, minY + height * 0.7F, "sensor_array");
      api.addObjective(minX + width * 0.2F, minY + height * 0.5F, "comm_relay");
      api.addAsteroidField(minX + width * 0.5F, minY + height, 270.0F, width, 25.0F, 75.0F, 120);
      BattleCreationContext context = new BattleCreationContext((CampaignFleetAPI)null, (FleetGoal)null, (CampaignFleetAPI)null, (FleetGoal)null);
      context.setInitialEscapeRange(7000.0F);
      api.addPlugin(new EscapeRevealPlugin(context));
   }
}
