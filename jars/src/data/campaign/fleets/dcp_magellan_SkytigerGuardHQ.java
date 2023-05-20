package data.campaign.fleets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BattleAPI;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CampaignEventListener.FleetDespawnReason;
import com.fs.starfarer.api.campaign.FactionAPI.ShipPickMode;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.listeners.FleetEventListener;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase.PatrolFleetData;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.fleets.PatrolAssignmentAIV4;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactory.PatrolType;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.OptionalFleetData;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.RouteData;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.RouteFleetSpawner;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager.RouteSegment;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.MarketCMD.RaidDangerLevel;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import java.util.Iterator;
import java.util.Random;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_SkytigerGuardHQ extends BaseIndustry implements RouteFleetSpawner, FleetEventListener {
   protected IntervalUtil tracker = new IntervalUtil(Global.getSettings().getFloat("averagePatrolSpawnInterval") * 0.7F, Global.getSettings().getFloat("averagePatrolSpawnInterval") * 1.3F);
   protected float returningPatrolValue = 0.0F;

   public boolean isHidden() {
      return !this.market.getFactionId().equals("dcp_magellan_protectorate");
   }

   public boolean isFunctional() {
      return super.isFunctional() && this.market.getFactionId().equals("dcp_magellan_protectorate");
   }

   public void apply() {
      super.apply(true);
      int size = this.market.getSize();
      this.demand("supplies", size - 1);
      this.demand("fuel", size - 1);
      this.demand("ships", size - 1);
      this.supply("crew", size);
      this.demand("hand_weapons", size);
      this.supply("marines", size);
      Pair<String, Integer> deficit = this.getMaxDeficit(new String[]{"hand_weapons"});
      this.applyDeficitToProduction(1, deficit, new String[]{"marines"});
      this.modifyStabilityWithBaseMod();
      MemoryAPI memory = this.market.getMemoryWithoutUpdate();
      Misc.setFlagWithReason(memory, "$patrol", this.getModId(), true, -1.0F);
      Misc.setFlagWithReason(memory, "$military", this.getModId(), true, -1.0F);
      if (!this.isFunctional()) {
         this.supply.clear();
         this.unapply();
      }

   }

   public void unapply() {
      super.unapply();
      MemoryAPI memory = this.market.getMemoryWithoutUpdate();
      Misc.setFlagWithReason(memory, "$patrol", this.getModId(), false, -1.0F);
      Misc.setFlagWithReason(memory, "$military", this.getModId(), false, -1.0F);
      this.unmodifyStabilityWithBaseMod();
   }

   protected boolean hasPostDemandSection(boolean hasDemand, IndustryTooltipMode mode) {
      return mode != IndustryTooltipMode.NORMAL || this.isFunctional();
   }

   protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
      if (mode != IndustryTooltipMode.NORMAL || this.isFunctional()) {
         this.addStabilityPostDemandSection(tooltip, hasDemand, mode);
      }

   }

   protected int getBaseStabilityMod() {
      return 2;
   }

   public String getNameForModifier() {
      return this.getSpec().getName().contains("HQ") ? this.getSpec().getName() : Misc.ucFirst(this.getSpec().getName());
   }

   protected Pair<String, Integer> getStabilityAffectingDeficit() {
      return this.getMaxDeficit(new String[]{"supplies", "fuel", "ships", "hand_weapons"});
   }

   public String getCurrentImage() {
      return super.getCurrentImage();
   }

   public boolean isDemandLegal(CommodityOnMarketAPI com) {
      return true;
   }

   public boolean isSupplyLegal(CommodityOnMarketAPI com) {
      return true;
   }

   protected void buildingFinished() {
      super.buildingFinished();
      this.tracker.forceIntervalElapsed();
   }

   protected void upgradeFinished(Industry previous) {
      super.upgradeFinished(previous);
      this.tracker.forceIntervalElapsed();
   }

   public void advance(float amount) {
      super.advance(amount);
      if (!Global.getSector().getEconomy().isSimMode()) {
         if (this.isFunctional()) {
            float days = Global.getSector().getClock().convertToDays(amount);
            float spawnRate = 1.0F;
            float rateMult = this.market.getStats().getDynamic().getStat("combat_fleet_spawn_rate_mult").getModifiedValue();
            spawnRate *= rateMult;
            float extraTime = 0.0F;
            if (this.returningPatrolValue > 0.0F) {
               float interval = this.tracker.getIntervalDuration();
               extraTime = interval * days;
               this.returningPatrolValue -= days;
               if (this.returningPatrolValue < 0.0F) {
                  this.returningPatrolValue = 0.0F;
               }
            }

            this.tracker.advance(days * spawnRate + extraTime);
            if (this.tracker.intervalElapsed()) {
               String sid = this.getRouteSourceId();
               int medium = this.getCount(PatrolType.COMBAT);
               int heavy = this.getCount(PatrolType.HEAVY);
               int maxMedium = 4;
               int maxHeavy = 1;
               WeightedRandomPicker<PatrolType> picker = new WeightedRandomPicker();
               picker.add(PatrolType.HEAVY, (float)(maxHeavy - heavy));
               picker.add(PatrolType.COMBAT, (float)(maxMedium - medium));
               if (picker.isEmpty()) {
                  return;
               }

               PatrolType type = (PatrolType)picker.pick();
               PatrolFleetData custom = new PatrolFleetData(type);
               OptionalFleetData extra = new OptionalFleetData(this.market);
               extra.fleetType = type.getFleetType();
               RouteData route = RouteManager.getInstance().addRoute(sid, this.market, Misc.genRandomSeed(), extra, this, custom);
               float patrolDays = 35.0F + (float)Math.random() * 10.0F;
               route.addSegment(new RouteSegment(patrolDays, this.market.getPrimaryEntity()));
            }

         }
      }
   }

   public void reportAboutToBeDespawnedByRouteManager(RouteData route) {
   }

   public boolean shouldRepeat(RouteData route) {
      return false;
   }

   public int getCount(PatrolType... types) {
      int count = 0;
      Iterator var3 = RouteManager.getInstance().getRoutesForSource(this.getRouteSourceId()).iterator();

      while(true) {
         while(true) {
            RouteData data;
            do {
               if (!var3.hasNext()) {
                  return count;
               }

               data = (RouteData)var3.next();
            } while(!(data.getCustom() instanceof PatrolFleetData));

            PatrolFleetData custom = (PatrolFleetData)data.getCustom();
            PatrolType[] var6 = types;
            int var7 = types.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               PatrolType type = var6[var8];
               if (type == custom.type) {
                  ++count;
                  break;
               }
            }
         }
      }
   }

   public int getMaxPatrols(PatrolType type) {
      if (type == PatrolType.COMBAT) {
         return (int)this.market.getStats().getDynamic().getMod("patrol_num_medium_mod").computeEffective(0.0F);
      } else {
         return type == PatrolType.HEAVY ? (int)this.market.getStats().getDynamic().getMod("patrol_num_heavy_mod").computeEffective(0.0F) : 0;
      }
   }

   public boolean shouldCancelRouteAfterDelayCheck(RouteData route) {
      return false;
   }

   public void reportBattleOccurred(CampaignFleetAPI fleet, CampaignFleetAPI primaryWinner, BattleAPI battle) {
   }

   public void reportFleetDespawnedToListener(CampaignFleetAPI fleet, FleetDespawnReason reason, Object param) {
      if (this.isFunctional()) {
         if (reason == FleetDespawnReason.REACHED_DESTINATION) {
            RouteData route = RouteManager.getInstance().getRoute(this.getRouteSourceId(), fleet);
            if (route.getCustom() instanceof PatrolFleetData) {
               PatrolFleetData custom = (PatrolFleetData)route.getCustom();
               if (custom.spawnFP > 0) {
                  float fraction = (float)(fleet.getFleetPoints() / custom.spawnFP);
                  this.returningPatrolValue += fraction;
               }
            }
         }

      }
   }

   public CampaignFleetAPI spawnFleet(RouteData route) {
      PatrolFleetData custom = (PatrolFleetData)route.getCustom();
      PatrolType type = custom.type;
      Random random = route.getRandom();
      float combat = 0.0F;
      float tanker = 0.0F;
      float freighter = 0.0F;
      String fleetType = type.getFleetType();
      switch(type) {
      case COMBAT:
         combat = (float)Math.round(8.0F + random.nextFloat() * 4.0F) * 6.0F;
         tanker = (float)Math.round(random.nextFloat()) * 2.0F;
         break;
      case HEAVY:
         combat = (float)Math.round(12.0F + random.nextFloat() * 6.0F) * 6.0F;
         tanker = (float)Math.round(random.nextFloat()) * 5.0F;
         freighter = (float)Math.round(random.nextFloat()) * 5.0F;
      }

      FleetParamsV3 params = new FleetParamsV3(this.market, (Vector2f)null, "dcp_magellan_startigers", route.getQualityOverride(), fleetType, combat, freighter, tanker, 0.0F, 0.0F, 0.0F, 0.0F);
      params.timestamp = route.getTimestamp();
      params.random = random;
      params.modeOverride = Misc.getShipPickMode(this.market);
      params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
      CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
      if (fleet != null && !fleet.isEmpty()) {
         fleet.setFaction(this.market.getFactionId(), true);
         fleet.setNoFactionInName(true);
         fleet.addEventListener(this);
         fleet.getMemoryWithoutUpdate().set("$isPatrol", true);
         fleet.getMemoryWithoutUpdate().set("$cfai_ignoreOtherFleets", true, 0.3F);
         if (type == PatrolType.COMBAT) {
            fleet.getMemoryWithoutUpdate().set("$isCustomsInspector", true);
         }

         String postId = Ranks.POST_PATROL_COMMANDER;
         String rankId = Ranks.SPACE_COMMANDER;
         switch(type) {
         case COMBAT:
            rankId = Ranks.SPACE_COMMANDER;
            break;
         case HEAVY:
            rankId = Ranks.SPACE_CAPTAIN;
         }

         fleet.getCommander().setPostId(postId);
         fleet.getCommander().setRankId(rankId);
         this.market.getContainingLocation().addEntity(fleet);
         fleet.setFacing((float)Math.random() * 360.0F);
         fleet.setLocation(this.market.getPrimaryEntity().getLocation().x, this.market.getPrimaryEntity().getLocation().y);
         fleet.addScript(new PatrolAssignmentAIV4(fleet, route));
         if (custom.spawnFP <= 0) {
            custom.spawnFP = fleet.getFleetPoints();
         }

         return fleet;
      } else {
         return null;
      }
   }

   public String getRouteSourceId() {
      return this.getMarket().getId() + "_skytigers";
   }

   public boolean isAvailableToBuild() {
      return false;
   }

   public boolean showWhenUnavailable() {
      return false;
   }

   public boolean canImprove() {
      return false;
   }

   public RaidDangerLevel adjustCommodityDangerLevel(String commodityId, RaidDangerLevel level) {
      return level.next();
   }

   public RaidDangerLevel adjustItemDangerLevel(String itemId, String data, RaidDangerLevel level) {
      return level.next();
   }
}
