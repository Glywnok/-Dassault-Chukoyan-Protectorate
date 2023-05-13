/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.scripts.world;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.scripts.world.systems.KhamnConstellation;

/**
 *
 * @author HarmfulMechanic
 */
public class MagellanGen implements SectorGeneratorPlugin
{

    @Override
    public void generate(SectorAPI sector)
    {
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("magellan_protectorate");
        initFactionRelationships(sector);
    
        new KhamnConstellation().generate(sector);
    }

    public static void initFactionRelationships(SectorAPI sector)
    {
        FactionAPI protectorate = sector.getFaction("magellan_protectorate");
        FactionAPI levellers = sector.getFaction("magellan_leveller");
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
        FactionAPI kol = sector.getFaction(Factions.KOL);
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
        FactionAPI diktat = sector.getFaction(Factions.DIKTAT);
        FactionAPI league = sector.getFaction(Factions.PERSEAN);
        FactionAPI remnants = sector.getFaction(Factions.REMNANTS);
        FactionAPI neutral = sector.getFaction(Factions.NEUTRAL);

        //FactionAPI sra = sector.getFaction("shadow_industry");
        //FactionAPI birdy = sector.getFaction("blackrock_driveyards");
        //FactionAPI thi = sector.getFaction("tiandong");

        protectorate.setRelationship(hegemony.getId(), RepLevel.VENGEFUL);
        protectorate.setRelationship(tritachyon.getId(), RepLevel.INHOSPITABLE);
        protectorate.setRelationship(pirates.getId(), RepLevel.HOSTILE);
        protectorate.setRelationship(independent.getId(), RepLevel.FAVORABLE);
        protectorate.setRelationship(kol.getId(), RepLevel.NEUTRAL);
        protectorate.setRelationship(church.getId(), RepLevel.FAVORABLE);
        protectorate.setRelationship(path.getId(), RepLevel.INHOSPITABLE);
        protectorate.setRelationship(diktat.getId(), RepLevel.HOSTILE);
        protectorate.setRelationship(league.getId(), RepLevel.INHOSPITABLE);
        protectorate.setRelationship(remnants.getId(), RepLevel.HOSTILE);
        
        protectorate.setRelationship("magellan_leveller", RepLevel.HOSTILE);
        protectorate.setRelationship("blade_breakers", RepLevel.HOSTILE);
        protectorate.setRelationship("shadow_industry", RepLevel.SUSPICIOUS);
        protectorate.setRelationship("blackrock_driveyards", RepLevel.SUSPICIOUS);
        protectorate.setRelationship("tiandong", RepLevel.WELCOMING);
        protectorate.setRelationship("diableavionics", RepLevel.HOSTILE);
        protectorate.setRelationship("new_galactic_order", RepLevel.VENGEFUL);
        
        levellers.setRelationship(hegemony.getId(), RepLevel.INHOSPITABLE);
        levellers.setRelationship(tritachyon.getId(), RepLevel.SUSPICIOUS);
        levellers.setRelationship(pirates.getId(), RepLevel.SUSPICIOUS);
        levellers.setRelationship(independent.getId(), RepLevel.FAVORABLE);
        levellers.setRelationship(kol.getId(), RepLevel.NEUTRAL);
        levellers.setRelationship(church.getId(), RepLevel.INHOSPITABLE);
        levellers.setRelationship(path.getId(), RepLevel.HOSTILE);
        levellers.setRelationship(diktat.getId(), RepLevel.VENGEFUL);
        levellers.setRelationship(league.getId(), RepLevel.INHOSPITABLE);
        levellers.setRelationship(remnants.getId(), RepLevel.HOSTILE);
        
        levellers.setRelationship("magellan_protectorate", RepLevel.HOSTILE);
        levellers.setRelationship("blade_breakers", RepLevel.VENGEFUL);
        levellers.setRelationship("shadow_industry", RepLevel.WELCOMING);
        levellers.setRelationship("blackrock_driveyards", RepLevel.SUSPICIOUS);
        levellers.setRelationship("tiandong", RepLevel.SUSPICIOUS);
        levellers.setRelationship("diableavionics", RepLevel.VENGEFUL);
        levellers.setRelationship("new_galactic_order", RepLevel.VENGEFUL);

    }
}