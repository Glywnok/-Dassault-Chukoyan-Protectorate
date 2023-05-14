package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import java.util.ArrayList;
import java.util.Iterator;

public class AddMarketplace {
   public static MarketAPI addMarketplace(String factionID, SectorEntityToken primaryEntity, ArrayList<SectorEntityToken> connectedEntities, String name, int size, ArrayList<String> marketConditions, ArrayList<String> Industries, ArrayList<String> submarkets, float tariff) {
      EconomyAPI globalEconomy = Global.getSector().getEconomy();
      String planetID = primaryEntity.getId();
      MarketAPI newMarket = Global.getFactory().createMarket(planetID, name, size);
      newMarket.setFactionId(factionID);
      newMarket.setPrimaryEntity(primaryEntity);
      newMarket.getTariff().modifyFlat("generator", tariff);
      Iterator var13;
      String industry;
      if (null != submarkets) {
         var13 = submarkets.iterator();

         while(var13.hasNext()) {
            industry = (String)var13.next();
            newMarket.addSubmarket(industry);
         }
      }

      var13 = marketConditions.iterator();

      while(var13.hasNext()) {
         industry = (String)var13.next();
         newMarket.addCondition(industry);
      }

      var13 = Industries.iterator();

      while(var13.hasNext()) {
         industry = (String)var13.next();
         newMarket.addIndustry(industry);
      }

      SectorEntityToken entity;
      if (null != connectedEntities) {
         var13 = connectedEntities.iterator();

         while(var13.hasNext()) {
            entity = (SectorEntityToken)var13.next();
            newMarket.getConnectedEntities().add(entity);
         }
      }

      globalEconomy.addMarket(newMarket, true);
      primaryEntity.setMarket(newMarket);
      primaryEntity.setFaction(factionID);
      if (null != connectedEntities) {
         var13 = connectedEntities.iterator();

         while(var13.hasNext()) {
            entity = (SectorEntityToken)var13.next();
            entity.setMarket(newMarket);
            entity.setFaction(factionID);
         }
      }

      return newMarket;
   }
}
