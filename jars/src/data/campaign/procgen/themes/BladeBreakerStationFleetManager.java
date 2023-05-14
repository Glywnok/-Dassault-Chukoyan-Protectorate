package data.campaign.procgen.themes;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.CampaignEventListener.FleetDespawnReason;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.fleets.SourceBasedFleetManager;
import java.util.Random;

public class BladeBreakerStationFleetManager extends SourceBasedFleetManager {
   protected int minPts;
   protected int maxPts;
   protected int totalLost;

   public BladeBreakerStationFleetManager(SectorEntityToken source, float thresholdLY, int minFleets, int maxFleets, float respawnDelay, int minPts, int maxPts) {
      super(source, thresholdLY, minFleets, maxFleets, respawnDelay);
      this.minPts = minPts;
      this.maxPts = maxPts;
   }

   protected CampaignFleetAPI spawnFleet() {
      Random random = new Random();
      int combatPoints = this.minPts + random.nextInt(this.maxPts - this.minPts + 1);
      int bonus = this.totalLost * 4;
      if (bonus > this.maxPts) {
         bonus = this.maxPts;
      }

      combatPoints += bonus;
      String type = "patrolSmall";
      if (combatPoints > 8) {
         type = "patrolMedium";
      }

      if (combatPoints > 16) {
         type = "patrolLarge";
      }

      combatPoints = (int)((float)combatPoints * 8.0F);
      FleetParamsV3 params = new FleetParamsV3(this.source.getMarket(), this.source.getLocationInHyperspace(), "blade_breakers", 1.0F, type, (float)combatPoints, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      params.officerNumberBonus = 10;
      params.random = random;
      CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
      if (fleet == null) {
         return null;
      } else {
         LocationAPI location = this.source.getContainingLocation();
         location.addEntity(fleet);
         BladeBreakerSeededFleetManager.initBladeBreakerFleetProperties(random, fleet, false);
         fleet.setLocation(this.source.getLocation().x, this.source.getLocation().y);
         fleet.setFacing(random.nextFloat() * 360.0F);
         fleet.addScript(new BladeBreakerAssignmentAI(fleet, (StarSystemAPI)this.source.getContainingLocation(), this.source));
         return fleet;
      }
   }

   public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, FleetDespawnReason reason, Object param) {
      super.reportFleetDespawnedToListener(fleet, reason, param);
      if (reason == FleetDespawnReason.DESTROYED_BY_BATTLE) {
         ++this.totalLost;
      }

   }
}
