package data.missions.istl_heavyiron;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;
import java.util.List;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "DMS", FleetGoal.ATTACK, false, 5);
      api.initFleet(FleetSide.ENEMY, "", FleetGoal.ATTACK, true);
      api.setFleetTagline(FleetSide.PLAYER, "Guard Detachment Juno");
      api.setFleetTagline(FleetSide.ENEMY, "Antilles Slaver Ring");
      api.addBriefingItem("Defeat all enemy forces");
      api.addBriefingItem("Use your range advantage and deeper flux pool to finish enemies. Don't get swarmed.");
      api.addBriefingItem("Remember: Your armor will absorb tremendous punishment. Use your shield sparingly in order to fire more often.");
      api.addToFleet(FleetSide.PLAYER, "istl_baikal_brone_elite", FleetMemberType.SHIP, "DMS Rokossovsky", true);
      api.addToFleet(FleetSide.PLAYER, "istl_grand_union_std", FleetMemberType.SHIP, "UNS Nameless", false);
      api.addToFleet(FleetSide.PLAYER, "istl_feeder_std", FleetMemberType.SHIP, "UNS Dar es Salaam", false);
      api.addToFleet(FleetSide.PLAYER, "istl_stoat_c_std", FleetMemberType.SHIP, "UNS Balls To The Wall", false);
      api.addToFleet(FleetSide.PLAYER, "istl_stoat_c_std", FleetMemberType.SHIP, "UNS Plowman", false);
      api.defeatOnShipLoss("DMS Rokossovsky");
      api.addToFleet(FleetSide.ENEMY, "heron_Attack", FleetMemberType.SHIP, "Hiryu", false);
      api.addToFleet(FleetSide.ENEMY, "istl_naja_export", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "istl_naja_mk1_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "colossus3_Pirate", FleetMemberType.SHIP, "Golden Circle", true);
      api.addToFleet(FleetSide.ENEMY, "enforcer_Assault", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "lasher_CS", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "hound_Overdriven", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "hound_d_pirates_Shielded", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "istl_sevastopol_mk1_std", FleetMemberType.SHIP, false);
      api.addToFleet(FleetSide.ENEMY, "nebula_Standard", FleetMemberType.SHIP, false);
      float width = 27000.0F;
      float height = 21000.0F;
      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;
      api.addNebula(minX + width * 0.5F - 300.0F, minY + height * 0.5F, 1000.0F);
      api.addNebula(minX + width * 0.5F + 300.0F, minY + height * 0.5F, 1000.0F);

      for(int i = 0; i < 5; ++i) {
         float x = (float)Math.random() * width - width / 2.0F;
         float y = (float)Math.random() * height - height / 2.0F;
         float radius = 100.0F + (float)Math.random() * 400.0F;
         api.addNebula(x, y, radius);
      }

      api.addAsteroidField(minX + width / 2.0F, minY + height / 2.0F, 15.0F, 9200.0F, 25.0F, 75.0F, 135);
      api.addPlanet(0.0F, 0.0F, 120.0F, "istl_aridbread", 50.0F, true);
      api.addPlugin(new BaseEveryFrameCombatPlugin() {
         public void init(CombatEngineAPI engine) {
            engine.getContext().setStandoffRange(6000.0F);
         }

         public void advance(float amount, List events) {
         }
      });
   }
}
