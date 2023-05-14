package data.scripts.world;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.campaign.ids.magellan_People;
import data.scripts.world.systems.KhamnConstellation;
import java.util.Iterator;
import java.util.List;

public class MagellanGen implements SectorGeneratorPlugin {
   public void generate(SectorAPI sector) {
      SharedData.getData().getPersonBountyEventData().addParticipatingFaction("magellan_protectorate");
      initFactionRelationships(sector);
      (new KhamnConstellation()).generate(sector);
      (new magellan_People()).advance();
   }

   public static void initFactionRelationships(SectorAPI sector) {
      FactionAPI protectorate = sector.getFaction("magellan_protectorate");
      FactionAPI levellers = sector.getFaction("magellan_leveller");
      FactionAPI theherd = sector.getFaction("magellan_theherd");
      FactionAPI ancientstarfarer = sector.getFaction("magellan_ancientstarfarer");
      FactionAPI hegemony = sector.getFaction("hegemony");
      FactionAPI tritachyon = sector.getFaction("tritachyon");
      FactionAPI pirates = sector.getFaction("pirates");
      FactionAPI independent = sector.getFaction("independent");
      FactionAPI kol = sector.getFaction("knights_of_ludd");
      FactionAPI church = sector.getFaction("luddic_church");
      FactionAPI path = sector.getFaction("luddic_path");
      FactionAPI diktat = sector.getFaction("sindrian_diktat");
      FactionAPI league = sector.getFaction("persean");
      FactionAPI remnants = sector.getFaction("remnant");
      FactionAPI neutral = sector.getFaction("neutral");
      protectorate.setRelationship(hegemony.getId(), RepLevel.VENGEFUL);
      protectorate.setRelationship(tritachyon.getId(), RepLevel.INHOSPITABLE);
      protectorate.setRelationship(pirates.getId(), RepLevel.HOSTILE);
      protectorate.setRelationship(independent.getId(), RepLevel.FAVORABLE);
      protectorate.setRelationship(kol.getId(), RepLevel.NEUTRAL);
      protectorate.setRelationship(church.getId(), RepLevel.FAVORABLE);
      protectorate.setRelationship(path.getId(), RepLevel.HOSTILE);
      protectorate.setRelationship(diktat.getId(), RepLevel.HOSTILE);
      protectorate.setRelationship(league.getId(), RepLevel.INHOSPITABLE);
      protectorate.setRelationship(remnants.getId(), RepLevel.HOSTILE);
      protectorate.setRelationship(levellers.getId(), RepLevel.HOSTILE);
      protectorate.setRelationship("blade_breakers", RepLevel.VENGEFUL);
      protectorate.setRelationship("shadow_industry", RepLevel.SUSPICIOUS);
      protectorate.setRelationship("blackrock_driveyards", RepLevel.SUSPICIOUS);
      protectorate.setRelationship("tiandong", RepLevel.WELCOMING);
      protectorate.setRelationship("interstellarimperium", RepLevel.SUSPICIOUS);
      protectorate.setRelationship("SCY", RepLevel.SUSPICIOUS);
      protectorate.setRelationship("ORA", RepLevel.INHOSPITABLE);
      protectorate.setRelationship("kadur_remnant", RepLevel.WELCOMING);
      protectorate.setRelationship("qamar_insurgency", RepLevel.HOSTILE);
      protectorate.setRelationship("diableavionics", RepLevel.HOSTILE);
      protectorate.setRelationship("roider", RepLevel.WELCOMING);
      protectorate.setRelationship("al_ars", RepLevel.INHOSPITABLE);
      protectorate.setRelationship("xhanempire", RepLevel.SUSPICIOUS);
      protectorate.setRelationship("hmi", RepLevel.SUSPICIOUS);
      protectorate.setRelationship("vanidad", RepLevel.HOSTILE);
      protectorate.setRelationship("scalartech", RepLevel.INHOSPITABLE);
      protectorate.setRelationship("apex_design", RepLevel.HOSTILE);
      protectorate.setRelationship("new_galactic_order", RepLevel.VENGEFUL);
      protectorate.setRelationship("aria", RepLevel.VENGEFUL);
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
      levellers.setRelationship("interstellarimperium", RepLevel.HOSTILE);
      levellers.setRelationship("SCY", RepLevel.SUSPICIOUS);
      levellers.setRelationship("ORA", RepLevel.WELCOMING);
      levellers.setRelationship("kadur_remnant", RepLevel.SUSPICIOUS);
      levellers.setRelationship("qamar_insurgency", RepLevel.WELCOMING);
      levellers.setRelationship("diableavionics", RepLevel.VENGEFUL);
      levellers.setRelationship("roider", RepLevel.WELCOMING);
      levellers.setRelationship("al_ars", RepLevel.HOSTILE);
      levellers.setRelationship("xhanempire", RepLevel.HOSTILE);
      levellers.setRelationship("HMI", RepLevel.INHOSPITABLE);
      levellers.setRelationship("vanidad", RepLevel.SUSPICIOUS);
      levellers.setRelationship("scalartech", RepLevel.SUSPICIOUS);
      levellers.setRelationship("apex_design", RepLevel.SUSPICIOUS);
      levellers.setRelationship("new_galactic_order", RepLevel.VENGEFUL);
      levellers.setRelationship("aria", RepLevel.VENGEFUL);
      List<FactionAPI> factionList = sector.getAllFactions();
      Iterator var17 = factionList.iterator();

      FactionAPI faction;
      while(var17.hasNext()) {
         faction = (FactionAPI)var17.next();
         if (faction != theherd && faction != pirates && !faction.isNeutralFaction()) {
            theherd.setRelationship(faction.getId(), RepLevel.HOSTILE);
         }
      }

      theherd.setRelationship(pirates.getId(), RepLevel.WELCOMING);
      theherd.setRelationship("player", RepLevel.HOSTILE);
      var17 = factionList.iterator();

      while(var17.hasNext()) {
         faction = (FactionAPI)var17.next();
         if (faction != ancientstarfarer && faction != independent && faction != league && faction != protectorate && !faction.isNeutralFaction()) {
            ancientstarfarer.setRelationship(faction.getId(), RepLevel.HOSTILE);
         }
      }

      ancientstarfarer.setRelationship(independent.getId(), RepLevel.WELCOMING);
      ancientstarfarer.setRelationship(league.getId(), RepLevel.FAVORABLE);
      ancientstarfarer.setRelationship(protectorate.getId(), RepLevel.SUSPICIOUS);
   }
}
