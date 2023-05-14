package data.scripts.campaign.intel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.listeners.DiscoverEntityListener;
import org.apache.log4j.Logger;

public class dcp_DME_DiscoverEntityListener implements DiscoverEntityListener {
   public static final Logger LOG = Global.getLogger(data.scripts.campaign.intel.dcp_DME_DiscoverEntityListener.class);

   public void reportEntityDiscovered(SectorEntityToken entity) {
      if (entity.hasTag("istl_hardened_warning_beacon")) {
         dcp_DME_BreakerBeaconIntel intel = new dcp_DME_BreakerBeaconIntel(entity);
         Global.getSector().getIntelManager().addIntel(intel);
      }

   }
}
