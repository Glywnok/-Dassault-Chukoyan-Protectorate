/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.scripts.campaign.intel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.listeners.DiscoverEntityListener;
import data.campaign.ids.dcp_Tags;
import org.apache.log4j.Logger;

/**
 *
 * @author HarmfulMechanic
 */
public class dcp_DiscoverEntityListener implements DiscoverEntityListener {

    public static final Logger LOG = Global.getLogger(dcp_DiscoverEntityListener.class);
    
    @Override
    public void reportEntityDiscovered(SectorEntityToken entity) {

        if (entity.hasTag(dcp_Tags.HARDENED_WARNING_BEACON)) {
    
            dcp_BreakerBeaconIntel intel = new dcp_BreakerBeaconIntel(entity);
            Global.getSector().getIntelManager().addIntel(intel);
        }
    }

}
