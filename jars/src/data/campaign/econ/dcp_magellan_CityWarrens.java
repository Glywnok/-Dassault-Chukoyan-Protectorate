package data.campaign.econ;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.Color;
import java.util.Arrays;

public class dcp_magellan_CityWarrens extends BaseMarketConditionPlugin {
   private static String[] magellanFactions = new String[]{"dcp_magellan_protectorate", "dcp_magellan_leveller"};
   public static float DEFENSE_BONUS_MAGELLAN = 4.0F;
   public static float DEFENSE_BONUS_OTHER = 2.0F;

   private String getString(String key) {
      return Global.getSettings().getString("Strings", "dcp_magellan_" + key);
   }

   public void apply(String id) {
      if (Arrays.asList(magellanFactions).contains(this.market.getFactionId())) {
         this.market.getStats().getDynamic().getMod("ground_defenses_mod").modifyMult(id, DEFENSE_BONUS_MAGELLAN, this.getString("citywarrens_title"));
      } else {
         this.market.getStats().getDynamic().getMod("ground_defenses_mod").modifyMult(id, DEFENSE_BONUS_OTHER, this.getString("citywarrens_title"));
      }

      if (Arrays.asList(magellanFactions).contains(this.market.getFactionId())) {
         this.market.getStability().modifyFlat(id, 1.0F, this.getString("citywarrens_desc"));
      } else {
         this.market.getStability().modifyFlat(id, -2.0F, this.getString("citywarrens_desc"));
      }

   }

   public void unapply(String id) {
      this.market.getStats().getDynamic().getMod("ground_defenses_mod").unmodify(id);
      this.market.getStability().unmodify(id);
   }

   protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
      super.createTooltipAfterDescription(tooltip, expanded);
      float pad = 10.0F;
      float padS = 2.0F;
      Color pos = Misc.getPositiveHighlightColor();
      Color neg = Misc.getNegativeHighlightColor();
      Color mag = data.hullmods.dcp_magellan_hullmodUtils.getMagellanHLColor();
      Color magbg = data.hullmods.dcp_magellan_hullmodUtils.getMagellanBGColor();
      Color quote = data.hullmods.dcp_magellan_hullmodUtils.getQuoteColor();
      Color attrib = Misc.getGrayColor();
      tooltip.addSectionHeading(this.getString("citywarrens_effects"), mag, magbg, Alignment.MID, pad);
      tooltip.addPara(this.getString("citywarrens_ef1"), pad, pos, new String[]{"+1"});
      tooltip.addPara(this.getString("citywarrens_ef2"), padS, neg, new String[]{"-2"});
      tooltip.addPara(this.getString("citywarrens_ef3"), padS, pos, new String[]{"+" + (int)((DEFENSE_BONUS_MAGELLAN - 1.0F) * 100.0F) + "%"});
      tooltip.addPara(this.getString("citywarrens_ef4"), padS, pos, new String[]{"+" + (int)((DEFENSE_BONUS_OTHER - 1.0F) * 100.0F) + "%"});
      if (expanded) {
         Color lev = data.hullmods.dcp_magellan_hullmodUtils.getLevellerHLColor();
         tooltip.addSectionHeading(this.getString("citywarrens_listfaction"), mag, magbg, Alignment.MID, pad);
         tooltip.addPara("- " + this.getString("citywarrens_protectorate"), mag, pad);
         tooltip.addPara("- " + this.getString("citywarrens_leveller"), lev, padS);
      }

      LabelAPI label = tooltip.addPara(this.getString("citywarrens_quote"), quote, pad);
      label.italicize(0.12F);
      tooltip.addPara("      " + this.getString("Magellan2ndEmDash") + this.getString("citywarrens_attrib"), attrib, pad);
   }

   public boolean isTooltipExpandable() {
      return true;
   }
}
