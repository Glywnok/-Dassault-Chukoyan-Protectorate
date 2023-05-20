package data.campaign.fleets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.fleets.DisposableFleetManager;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.intel.bases.PirateBaseManager;
import java.util.Iterator;

public class dcp_magellan_DisposableHerdFleetManager extends DisposableFleetManager {
   protected Object readResolve() {
      super.readResolve();
      return this;
   }

   protected String getSpawnId() {
      return "dcp_magellan_herd_spawnID";
   }

   protected int getDesiredNumFleetsForSpawnLocation() {
      MarketAPI mags_ind = this.getLargestTaggedMarket("dcp_magellan_indiemarket");
      return mags_ind == null ? 0 : mags_ind.getSize();
   }

   protected MarketAPI getLargestTaggedMarket(String tag) {
      if (this.currSpawnLoc == null) {
         return null;
      } else {
         MarketAPI largest = null;
         int maxSize = 0;
         Iterator var4 = Global.getSector().getEconomy().getMarkets(this.currSpawnLoc).iterator();

         while(var4.hasNext()) {
            MarketAPI market = (MarketAPI)var4.next();
            if (!market.isHidden() && market.getTags().contains(tag) && market.getSize() > maxSize) {
               maxSize = market.getSize();
               largest = market;
            }
         }

         return largest;
      }
   }

   protected CampaignFleetAPI spawnFleetImpl() {
      StarSystemAPI system = this.currSpawnLoc;
      if (system == null) {
         return null;
      } else {
         int size = this.getDesiredNumFleetsForSpawnLocation();
         if (size == 0) {
            return null;
         } else {
            String fleetType = "patrolSmall";
            float combat = 1.0F;

            for(int i = 0; i < 3; ++i) {
               if ((float)Math.random() > 0.5F) {
                  ++combat;
               }
            }

            float desired = (float)size;
            if (desired > 2.0F) {
               float timeFactor = (PirateBaseManager.getInstance().getDaysSinceStart() - 180.0F) / 730.0F;
               if (timeFactor < 0.0F) {
                  timeFactor = 0.0F;
               }

               if (timeFactor > 1.0F) {
                  timeFactor = 1.0F;
               }

               combat += (desired - 2.0F) * (0.5F + (float)Math.random() * 0.5F) * 1.0F * timeFactor;
            }

            combat *= 11.0F;
            FleetParamsV3 params = new FleetParamsV3((MarketAPI)null, system.getLocation(), "dcp_magellan_theherd", (Float)null, fleetType, combat, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4F);
            params.ignoreMarketFleetSizeMult = true;
            CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
            if (fleet != null && !fleet.isEmpty()) {
               fleet.setFaction("pirates", true);
               fleet.setNoFactionInName(true);
               fleet.getMemoryWithoutUpdate().set("$isPirate", true);
               fleet.getMemoryWithoutUpdate().set("$core_fleetNoMilitaryResponse", true);
               this.setLocationAndOrders(fleet, 0.12F, 1.0F);
               return fleet;
            } else {
               return null;
            }
         }
      }
   }
}
