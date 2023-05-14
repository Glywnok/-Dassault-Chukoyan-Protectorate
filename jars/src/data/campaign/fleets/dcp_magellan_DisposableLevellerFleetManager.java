package data.campaign.fleets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.fleets.DisposableFleetManager;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.intel.bases.PirateBaseManager;
import com.fs.starfarer.api.util.Misc;
import java.util.Iterator;

public class dcp_magellan_DisposableLevellerFleetManager extends DisposableFleetManager {
   protected Object readResolve() {
      super.readResolve();
      return this;
   }

   protected String getSpawnId() {
      return "magellan_leveller_spawnID";
   }

   protected int getDesiredNumFleetsForSpawnLocation() {
      MarketAPI mags = this.getLargestMarket("magellan_protectorate");
      String commission = Misc.getCommissionFactionId();
      if ("magellan_protectorate".equals(commission)) {
         MarketAPI player = this.getLargestMarket("player");
         if (player != null && (mags == null || player.getSize() > mags.getSize())) {
            mags = player;
         }
      }

      return mags == null ? 0 : mags.getSize();
   }

   protected MarketAPI getLargestMarket(String faction) {
      if (this.currSpawnLoc == null) {
         return null;
      } else {
         MarketAPI largest = null;
         int maxSize = 0;
         Iterator var4 = Global.getSector().getEconomy().getMarkets(this.currSpawnLoc).iterator();

         while(var4.hasNext()) {
            MarketAPI market = (MarketAPI)var4.next();
            if (!market.isHidden() && market.getFactionId().equals(faction) && market.getSize() > maxSize) {
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

            combat *= 7.0F;
            FleetParamsV3 params = new FleetParamsV3((MarketAPI)null, system.getLocation(), "magellan_leveller", (Float)null, fleetType, combat, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6F);
            params.ignoreMarketFleetSizeMult = true;
            CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
            if (fleet != null && !fleet.isEmpty()) {
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
