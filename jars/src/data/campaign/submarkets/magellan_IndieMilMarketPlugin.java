package data.campaign.submarkets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionDoctrineAPI;
import com.fs.starfarer.api.campaign.FactionAPI.ShipPickMode;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.impl.campaign.submarkets.MilitarySubmarketPlugin;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.Color;
import java.util.Iterator;

public class magellan_IndieMilMarketPlugin extends MilitarySubmarketPlugin {
   private String getString(String key) {
      return Global.getSettings().getString("Strings", "dcp_magellan_" + key);
   }

   public void updateCargoPrePlayerInteraction() {
      float seconds = Global.getSector().getClock().convertToSeconds(this.sinceLastCargoUpdate);
      this.addAndRemoveStockpiledResources(seconds, false, true, true);
      this.sinceLastCargoUpdate = 0.0F;
      String replaceFaction = "dcp_magellan_independentmkt";
      if (this.okToUpdateShipsAndWeapons()) {
         this.sinceSWUpdate = 0.0F;
         this.pruneWeapons(0.0F);
         int weapons = 7 + Math.max(0, this.market.getSize() - 1) * 2;
         int fighters = 2 + Math.max(0, this.market.getSize() - 3);
         this.addWeapons(weapons, weapons + 2, 3, replaceFaction);
         this.addFighters(fighters, fighters + 2, 3, replaceFaction);
         float stability = this.market.getStabilityValue();
         float sMult = Math.max(0.1F, stability / 10.0F);
         this.getCargo().getMothballedShips().clear();
         this.addShips(replaceFaction, 200.0F * sMult, 15.0F, 10.0F, 20.0F, 10.0F, 10.0F, (Float)null, 0.0F, (ShipPickMode)null, (FactionDoctrineAPI)null);
         this.addHullMods(4, 2 + this.itemGenRandom.nextInt(4));
      }

      this.getCargo().sort();
   }

   public String getName() {
      return this.submarket.getFaction().getId().equals("dcp_magellan_leveller") ? this.getString("str_revolutionary") : Misc.ucFirst(this.submarket.getFaction().getPersonNamePrefix()) + "\n" + this.getString("str_armsmarket");
   }

   protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
      float pad = 10.0F;
      float padS = 2.0F;
      Color quote = data.hullmods.dcp_magellan_hullmodUtils.getQuoteColor();
      Color attrib = Misc.getGrayColor();
      LabelAPI label = tooltip.addPara(this.getString("str_armsmarket_quote"), quote, pad);
      label.italicize(0.12F);
      tooltip.addPara("      " + this.getString("Magellan2ndEmDash") + this.getString("str_armsmarket_attrib"), attrib, padS);
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
