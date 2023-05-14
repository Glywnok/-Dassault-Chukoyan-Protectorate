package data.scripts.campaign.intel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.intel.misc.WarningBeaconIntel;
import com.fs.starfarer.api.loading.Description;
import com.fs.starfarer.api.loading.Description.Type;
import com.fs.starfarer.api.ui.SectorMapAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import java.util.Set;

public class dcp_DME_BreakerBeaconIntel extends WarningBeaconIntel {
   public dcp_DME_BreakerBeaconIntel(SectorEntityToken beacon) {
      super(beacon);
   }

   public void createSmallDescription(TooltipMakerAPI info, float width, float height) {
      Color h = Misc.getHighlightColor();
      Color g = Misc.getGrayColor();
      Color tc = Misc.getTextColor();
      float pad = 3.0F;
      float opad = 10.0F;
      Description desc = Global.getSettings().getDescription("istl_bladebreaker_beacon", Type.CUSTOM);
      info.addPara(desc.getText1FirstPara(), opad);
      this.addBulletPoints(info, ListInfoMode.IN_DESC);
      if (this.beacon.isInHyperspace()) {
         StarSystemAPI system = Misc.getNearbyStarSystem(this.beacon, 1.0F);
         if (system != null) {
            info.addPara("This beacon is located near the " + system.getNameWithLowercaseType() + ", warning of a Blade Breaker presence within.", opad);
         }
      }

   }

   public String getIcon() {
      if (this.isLow()) {
         return Global.getSettings().getSpriteName("intel", "istl_hardenedbeacon_low");
      } else if (this.isMedium()) {
         return Global.getSettings().getSpriteName("intel", "istl_hardenedbeacon_medium");
      } else {
         return this.isHigh() ? Global.getSettings().getSpriteName("intel", "istl_hardenedbeacon_high") : Global.getSettings().getSpriteName("intel", "istl_hardenedbeacon_low");
      }
   }

   public Set<String> getIntelTags(SectorMapAPI map) {
      Set<String> tags = super.getIntelTags(map);
      tags.add("Warning beacons");
      tags.add("blade_breakers");
      return tags;
   }

   public String getName() {
      return "Hardened Warning Beacon";
   }
}
