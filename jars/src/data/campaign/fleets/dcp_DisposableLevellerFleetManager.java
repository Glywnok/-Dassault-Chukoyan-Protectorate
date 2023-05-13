package data.campaign.fleets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.FleetTypes;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.intel.bases.PirateBaseManager;
import com.fs.starfarer.api.impl.campaign.fleets.DisposableFleetManager;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.util.Misc;
import data.campaign.ids.dcp_Factions;

public class dcp_DisposableLevellerFleetManager extends DisposableFleetManager {

	// Don't know what this is for, but it was in the DisposableLuddicPathFleetManager so it's probably important.
	protected Object readResolve() {
		super.readResolve();
		return this;
	}
	
	@Override
	protected String getSpawnId() {
		return "magellan_leveller_spawnID"; // not a faction id, just an identifier for this spawner
	}
	
	// Returns the size of the largest local magellan-affiliated market
	@Override
	protected int getDesiredNumFleetsForSpawnLocation() {
		MarketAPI mags = getLargestMarket( dcp_Factions.MG_PROTECTORATE );
		
		// if the player has a magellan commission, count their markets as magellan markets
		String commission = Misc.getCommissionFactionId();
		if( dcp_Factions.MG_PROTECTORATE.equals( commission ) ) {
			MarketAPI player = getLargestMarket( Factions.PLAYER );
			if( player != null && ( mags == null || player.getSize() > mags.getSize() ) ) {
				mags = player;
			}
		}
		
		if( mags == null ) {
			return 0;
		}
		
		return mags.getSize();
	}
	
	// copied directly from DisposableLuddicPathFleetManager
	protected MarketAPI getLargestMarket(String faction) {
		if (currSpawnLoc == null) return null;
		MarketAPI largest = null;
		int maxSize = 0;
		for (MarketAPI market : Global.getSector().getEconomy().getMarkets(currSpawnLoc)) {
			if (market.isHidden()) continue;
			if (!market.getFactionId().equals(faction)) continue;
			
			if (market.getSize() > maxSize) {
				maxSize = market.getSize();
				largest = market;
			}
		}
		return largest;
	}
	
	// almost entirely copied from DisposableLuddicPathFleetManager; lots of places to adjust values if desired.
	protected CampaignFleetAPI spawnFleetImpl() {
		StarSystemAPI system = currSpawnLoc;
		if (system == null) return null;

		int size = getDesiredNumFleetsForSpawnLocation();
		if( size == 0 ) {
			return null;
		}

                // may want to spawn other fleet types, too?
		String fleetType = FleetTypes.PATROL_SMALL;
		
		float combat = 1;
		for (int i = 0; i < 3; i++) {
			if ((float) Math.random() > 0.5f) {
				combat++;
			}
		}
		
		float desired = size;
		if (desired > 2) {
			// not sure why there's a time factor involved; this part was copied from DisposableLuddicPathFleetManager
			// without really paying much attention to what it was doing.
			float timeFactor = (PirateBaseManager.getInstance().getDaysSinceStart() - 180f) / (365f * 2f);
			if (timeFactor < 0) timeFactor = 0;
			if (timeFactor > 1) timeFactor = 1;
			
			combat += ((desired - 2) * (0.5f + (float) Math.random() * 0.5f)) * 1f * timeFactor;
		}
		
		// changed this up from 5f in DisposableLuddicPathFleetManager to make for slightly larger fleets.
		combat *= 7f;
		
		FleetParamsV3 params = new FleetParamsV3(
				null, // source market 
				system.getLocation(),
				dcp_Factions.MG_LEVELLERS,
				null,
				fleetType,
				combat, // combatPts
				0, // freighterPts 
				0, // tankerPts
				0f, // transportPts
				0f, // linerPts
				0f, // utilityPts
				0f // qualityMod
				);
		params.ignoreMarketFleetSizeMult = true;
		
		//params.random = random;
		CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
		if (fleet == null || fleet.isEmpty()) return null;
		
		// setting the below means: transponder off and more "go dark" use when traveling
		//fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_PIRATE, true);
		
		fleet.getMemoryWithoutUpdate().set(MemFlags.FLEET_NO_MILITARY_RESPONSE, true);
		
		// original DisposableLuddicPathFleetManager code had a check here; if fleets very small,
		// it only created a hyperspace watcher fleet; the 0.12f here says roughly 12% chance of the
		// generated fleet being in hyperspace.
		// I don't know what the third parameter actually does.
		setLocationAndOrders(fleet, 0.12f, 1f);
		
		return fleet;
	}
	
}










