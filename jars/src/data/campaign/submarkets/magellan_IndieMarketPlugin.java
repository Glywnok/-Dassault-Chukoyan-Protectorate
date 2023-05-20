package data.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionDoctrineAPI;
import com.fs.starfarer.api.campaign.FactionAPI.ShipPickMode;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.OpenMarketPlugin;
import com.fs.starfarer.api.util.Misc;
import java.util.Iterator;

public class magellan_IndieMarketPlugin extends OpenMarketPlugin {
   public void updateCargoPrePlayerInteraction() {
      float seconds = Global.getSector().getClock().convertToSeconds(this.sinceLastCargoUpdate);
      this.addAndRemoveStockpiledResources(seconds, false, true, true);
      this.sinceLastCargoUpdate = 0.0F;
      if (this.okToUpdateShipsAndWeapons()) {
         this.sinceSWUpdate = 0.0F;
         String replaceFaction = "dcp_magellan_independentmkt";
         this.pruneWeapons(0.0F);
         int weapons = 5 + Math.max(0, this.market.getSize() - 1) + (Misc.isMilitary(this.market) ? 5 : 0);
         int fighters = 1 + Math.max(0, (this.market.getSize() - 3) / 2) + (Misc.isMilitary(this.market) ? 2 : 0);
         this.addWeapons(weapons, weapons + 2, 0, replaceFaction);
         this.addFighters(fighters, fighters + 2, 0, replaceFaction);
         this.getCargo().getMothballedShips().clear();
         float freighters = 10.0F;
         CommodityOnMarketAPI com = this.market.getCommodityData("ships");
         freighters += (float)com.getMaxSupply() * 2.0F;
         if (freighters > 30.0F) {
            freighters = 30.0F;
         }

         this.addShips(replaceFaction, 10.0F, freighters, 0.0F, 10.0F, 10.0F, 5.0F, (Float)null, 0.0F, ShipPickMode.PRIORITY_THEN_ALL, (FactionDoctrineAPI)null);
         this.addShips(replaceFaction, 40.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, (Float)null, -0.5F, (ShipPickMode)null, (FactionDoctrineAPI)null);
         float tankers = 20.0F;
         com = this.market.getCommodityData("fuel");
         tankers += (float)com.getMaxSupply() * 3.0F;
         if (tankers > 40.0F) {
            tankers = 40.0F;
         }

         this.addShips(replaceFaction, 0.0F, 0.0F, tankers, 0.0F, 0.0F, 0.0F, (Float)null, 0.0F, ShipPickMode.PRIORITY_THEN_ALL, (FactionDoctrineAPI)null);
         this.addHullMods(1, 1 + this.itemGenRandom.nextInt(3));
      }

      this.getCargo().sort();
   }

   public boolean isHidden() {
      if (!this.market.getFactionId().equals("independent") && !this.market.getFactionId().equals("pirates")) {
         return true;
      } else {
         Iterator var1 = this.market.getSubmarketsCopy().iterator();

         while(var1.hasNext()) {
            SubmarketAPI sub = (SubmarketAPI)var1.next();
            if (sub.getSpecId().equals("open_market")) {
               this.market.removeSubmarket("open_market");
            }
         }

         return false;
      }
   }
}
