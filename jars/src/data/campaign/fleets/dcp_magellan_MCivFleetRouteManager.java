package data.campaign.fleets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.fleets.BaseRouteFleetManager;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.OptionalFleetData;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.RouteData;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.RouteSegment;
import com.fs.starfarer.api.impl.campaign.procgen.themes.ScavengerFleetAssignmentAI;
import com.fs.starfarer.api.impl.campaign.tutorial.TutorialMissionIntel;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import java.util.Iterator;
import java.util.Random;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_MCivFleetRouteManager extends BaseRouteFleetManager {
   protected StarSystemAPI system;

   public dcp_magellan_MCivFleetRouteManager(StarSystemAPI system) {
      super(1.0F, 14.0F);
      this.system = system;
   }

   protected String getRouteSourceId() {
      return "magellan_civvy_" + this.system.getId();
   }

   protected int getMaxFleets() {
      float salvage = getVeryApproximateSalvageValue(this.system);
      return (int)(1.0F + Math.min(salvage / 2.0F, 10.0F));
   }

   protected void addRouteFleetIfPossible() {
      if (!TutorialMissionIntel.isTutorialInProgress()) {
         MarketAPI market = this.pickSourceMarket();
         if (market != null) {
            Long seed = (new Random()).nextLong();
            String id = this.getRouteSourceId();
            OptionalFleetData extra = new OptionalFleetData(market);
            RouteData route = RouteManager.getInstance().addRoute(id, market, seed, extra, this);
            float distLY = Misc.getDistanceLY(market.getLocationInHyperspace(), this.system.getLocation());
            float travelDays = distLY * 1.5F;
            float prepDays = 2.0F + (float)Math.random() * 3.0F;
            float endDays = 8.0F + (float)Math.random() * 3.0F;
            float totalTravelTime = prepDays + endDays + travelDays * 2.0F;
            float stayDays = Math.max(20.0F, totalTravelTime);
            route.addSegment(new RouteSegment(prepDays, market.getPrimaryEntity()));
            route.addSegment(new RouteSegment(travelDays, market.getPrimaryEntity(), this.system.getCenter()));
            route.addSegment(new RouteSegment(stayDays, this.system.getCenter()));
            route.addSegment(new RouteSegment(travelDays, this.system.getCenter(), market.getPrimaryEntity()));
            route.addSegment(new RouteSegment(endDays, market.getPrimaryEntity()));
         }
      }
   }

   public static float getVeryApproximateSalvageValue(StarSystemAPI system) {
      return (float)system.getEntitiesWithTag("salvageable").size() * 1.5F;
   }

   public MarketAPI pickSourceMarket() {
      WeightedRandomPicker<MarketAPI> markets = new WeightedRandomPicker();
      Iterator var2 = Global.getSector().getEconomy().getMarketsCopy().iterator();

      while(var2.hasNext()) {
         MarketAPI market = (MarketAPI)var2.next();
         if (!market.getFaction().isHostileTo("independent") && market.getContainingLocation() != null && !market.getContainingLocation().isHyperspace() && !market.isHidden() && market.getTags().contains("magellan_indiemarket")) {
            float distLY = Misc.getDistanceLY(this.system.getLocation(), market.getLocationInHyperspace());
            float weight = (float)market.getSize();
            float f = Math.max(0.1F, 1.0F - Math.min(1.0F, distLY / 20.0F));
            f *= f;
            weight *= f;
            markets.add(market, weight);
         }
      }

      return (MarketAPI)markets.pick();
   }

   public CampaignFleetAPI spawnFleet(RouteData route) {
      Random random = route.getRandom();
      WeightedRandomPicker<String> picker = new WeightedRandomPicker(random);
      picker.add("scavengerSmall", 10.0F);
      picker.add("scavengerMedium", 15.0F);
      picker.add("scavengerLarge", 5.0F);
      String type = (String)picker.pick();
      boolean pirate = random.nextBoolean();
      CampaignFleetAPI fleet = createScavenger(type, this.system.getLocation(), route, route.getMarket(), pirate, random);
      if (fleet == null) {
         return null;
      } else {
         fleet.addScript(new ScavengerFleetAssignmentAI(fleet, route, pirate));
         return fleet;
      }
   }

   public static CampaignFleetAPI createScavenger(String type, Vector2f locInHyper, MarketAPI source, boolean pirate, Random random) {
      return createScavenger(type, locInHyper, (RouteData)null, source, pirate, random);
   }

   public static CampaignFleetAPI createScavenger(String type, Vector2f locInHyper, RouteData route, MarketAPI source, boolean pirate, Random random) {
      if (random == null) {
         random = new Random();
      }

      if (type == null) {
         WeightedRandomPicker<String> picker = new WeightedRandomPicker(random);
         picker.add("scavengerSmall", 10.0F);
         picker.add("scavengerMedium", 15.0F);
         picker.add("scavengerLarge", 5.0F);
         type = (String)picker.pick();
      }

      int combat = 0;
      int freighter = 0;
      int tanker = 0;
      int transport = 0;
      int utility = 0;
      if (type.equals("scavengerSmall")) {
         combat = random.nextInt(2) + 1;
         tanker = random.nextInt(2) + 1;
         utility = random.nextInt(2) + 1;
      } else if (type.equals("scavengerMedium")) {
         combat = 4 + random.nextInt(5);
         freighter = 4 + random.nextInt(5);
         tanker = 3 + random.nextInt(4);
         transport = random.nextInt(2);
         utility = 2 + random.nextInt(3);
      } else if (type.equals("scavengerLarge")) {
         combat = 7 + random.nextInt(8);
         freighter = 6 + random.nextInt(7);
         tanker = 5 + random.nextInt(6);
         transport = 3 + random.nextInt(8);
         utility = 4 + random.nextInt(5);
      }

      if (pirate) {
         utility = 0;
         transport = 0;
      }

      combat = (int)((float)combat * 3.0F);
      freighter = (int)((float)freighter * 2.0F);
      tanker = (int)((float)tanker * 2.0F);
      transport = (int)((float)transport * 1.0F);
      FleetParamsV3 params = new FleetParamsV3(route != null ? route.getMarket() : source, locInHyper, "magellan_civviescavs", route == null ? null : route.getQualityOverride(), type, (float)combat, (float)freighter, (float)tanker, (float)transport, 0.0F, (float)utility, MathUtils.getRandomNumberInRange(0.2F, 0.6F));
      if (route != null) {
         params.timestamp = route.getTimestamp();
      }

      params.random = random;
      CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
      if (fleet != null && !fleet.isEmpty()) {
         fleet.setFaction("independent", true);
         fleet.getMemoryWithoutUpdate().set("$isScavenger", true);
         if (!pirate) {
         }

         Misc.makeLowRepImpact(fleet, "magellan_scav");
         return fleet;
      } else {
         return null;
      }
   }

   public boolean shouldCancelRouteAfterDelayCheck(RouteData data) {
      return false;
   }

   public boolean shouldRepeat(RouteData route) {
      return false;
   }

   public void reportAboutToBeDespawnedByRouteManager(RouteData route) {
   }
}
