package data.missions.istl_hellsownsun;

import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {
   public void defineMission(MissionDefinitionAPI api) {
      api.initFleet(FleetSide.PLAYER, "BCS", FleetGoal.ATTACK, false);
      api.initFleet(FleetSide.ENEMY, "SBS", FleetGoal.ATTACK, true);
      api.setFleetTagline(FleetSide.PLAYER, "Besson Active Service Unit");
      api.setFleetTagline(FleetSide.ENEMY, "Perfidious forces of the Sixth Bureau");
      api.addBriefingItem("Destroy or drive off the Sixth Bureau task group.");
      api.addBriefingItem("Don't lose your flagship - your forces will be easy pickings if command is disrupted.");
      api.addBriefingItem("Acquit yourself well, volunteers. The Council expects great things from you.");
      api.addToFleet(FleetSide.PLAYER, "istl_devilray_strike", FleetMemberType.SHIP, "Fang Leader", true);
      api.addToFleet(FleetSide.PLAYER, "istl_stormkestrel_elite", FleetMemberType.SHIP, "Base One", false);
      api.addToFleet(FleetSide.PLAYER, "istl_snakeeye_support", FleetMemberType.SHIP, "Fang I", false);
      api.addToFleet(FleetSide.PLAYER, "istl_imp_assault", FleetMemberType.SHIP, "Fang II", false);
      api.addToFleet(FleetSide.PLAYER, "istl_imp_assault", FleetMemberType.SHIP, "Fang III", false);
      api.addToFleet(FleetSide.ENEMY, "istl_wildgoose_elite", FleetMemberType.SHIP, "SBS Night Killer V", false);
      api.addToFleet(FleetSide.ENEMY, "istl_kormoran_6e_elite", FleetMemberType.SHIP, "SBS Ultima Ratio", false);
      api.addToFleet(FleetSide.ENEMY, "istl_tunguska_6e_elite", FleetMemberType.SHIP, "SBS Eccentric Orbit", false);
      api.addToFleet(FleetSide.ENEMY, "afflictor_patrol", FleetMemberType.SHIP, "SBS String 1", false);
      api.addToFleet(FleetSide.ENEMY, "istl_vesper_6e_elite", FleetMemberType.SHIP, "SBS String 2", false);
      api.addToFleet(FleetSide.ENEMY, "istl_vesper_6e_elite", FleetMemberType.SHIP, "SBS String 3", false);
      api.addToFleet(FleetSide.ENEMY, "istl_vesper_6e_elite", FleetMemberType.SHIP, "SBS String 4", false);
      api.addToFleet(FleetSide.ENEMY, "wolf_patrol", FleetMemberType.SHIP, "SBS String 5", false);
      api.addToFleet(FleetSide.ENEMY, "omen_patrol", FleetMemberType.SHIP, "SBS String 6", false);
      api.addToFleet(FleetSide.ENEMY, "omen_patrol", FleetMemberType.SHIP, "SBS String 7", false);
      api.defeatOnShipLoss("Fang Leader");
      float width = 16000.0F;
      float height = 16000.0F;
      api.initMap(-width / 2.0F, width / 2.0F, -height / 2.0F, height / 2.0F);
      float minX = -width / 2.0F;
      float minY = -height / 2.0F;
      api.addAsteroidField(minX, minY + height / 2.0F, 0.0F, 8000.0F, 20.0F, 70.0F, 100);
      api.addPlanet(0.0F, 0.0F, 560.0F, "star_browndwarf", 320.0F, true);
   }
}
