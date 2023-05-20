package data.scripts.world;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SectorThemeGenerator;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.campaign.procgen.themes.BladeBreakerThemeGenerator;
import data.scripts.world.systems.BessonConstellation;
import data.scripts.world.systems.Hejaz;
import data.scripts.world.systems.Kostroma;
import data.scripts.world.systems.Martinique;
import data.scripts.world.systems.Nikolaev;
import java.util.Iterator;
import java.util.List;

public class ISTLGen implements SectorGeneratorPlugin {
   public void generate(SectorAPI sector) {
      SharedData.getData().getPersonBountyEventData().addParticipatingFaction("dassault_mikoyan");
      initFactionRelationships(sector);
      (new Nikolaev()).generate(sector);
      (new Martinique()).generate(sector);
      (new Kostroma()).generate(sector);
      (new Hejaz()).generate(sector);
      (new BessonConstellation()).generate(sector);
      (new data.campaign.ids.dcp_DME_People()).advance();
   }

   public void randombreakers(SectorAPI sector) {
      SectorThemeGenerator.generators.add(1, new BladeBreakerThemeGenerator());
   }

   public static void blotOut(boolean[][] cells, int x, int y, int c) {
      for(int i = Math.max(0, x - c / 2); i <= x + c / 2 && i < cells.length; ++i) {
         for(int j = Math.max(0, y - c / 2); j <= y + c / 2 && j < cells[0].length; ++j) {
            cells[i][j] = true;
         }
      }

   }

   public static void initFactionRelationships(SectorAPI sector) {
      FactionAPI dassault = sector.getFaction("dassault_mikoyan");
      FactionAPI dassault2 = sector.getFaction("6eme_bureau");
      FactionAPI bladebreakers = sector.getFaction("blade_breakers");
      FactionAPI breakerdeserter = sector.getFaction("the_deserter");
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
      dassault.setRelationship(hegemony.getId(), RepLevel.WELCOMING);
      dassault.setRelationship(tritachyon.getId(), RepLevel.INHOSPITABLE);
      dassault.setRelationship(pirates.getId(), RepLevel.VENGEFUL);
      dassault.setRelationship(independent.getId(), RepLevel.FAVORABLE);
      dassault.setRelationship(kol.getId(), RepLevel.HOSTILE);
      dassault.setRelationship(church.getId(), RepLevel.HOSTILE);
      dassault.setRelationship(path.getId(), RepLevel.VENGEFUL);
      dassault.setRelationship(diktat.getId(), RepLevel.HOSTILE);
      dassault.setRelationship(league.getId(), RepLevel.INHOSPITABLE);
      dassault.setRelationship(remnants.getId(), RepLevel.HOSTILE);
      dassault.setRelationship("blade_breakers", RepLevel.VENGEFUL);
      dassault.setRelationship("magellan_protectorate", RepLevel.HOSTILE);
      dassault.setRelationship("magellan_leveller", RepLevel.SUSPICIOUS);
      dassault.setRelationship("shadow_industry", RepLevel.FAVORABLE);
      dassault.setRelationship("blackrock_driveyards", RepLevel.SUSPICIOUS);
      dassault.setRelationship("tiandong", RepLevel.WELCOMING);
      dassault.setRelationship("diableavionics", RepLevel.HOSTILE);
      dassault.setRelationship("roider", RepLevel.WELCOMING);
      dassault.setRelationship("al_ars", RepLevel.HOSTILE);
      dassault.setRelationship("scalartech", RepLevel.HOSTILE);
      dassault.setRelationship("vic", RepLevel.HOSTILE);
      dassault.setRelationship("new_galactic_order", RepLevel.VENGEFUL);
      dassault2.setRelationship(dassault.getId(), RepLevel.COOPERATIVE);
      dassault2.setRelationship(path.getId(), RepLevel.VENGEFUL);
      dassault2.setRelationship(diktat.getId(), RepLevel.HOSTILE);
      dassault2.setRelationship(remnants.getId(), RepLevel.VENGEFUL);
      dassault2.setRelationship("blade_breakers", RepLevel.VENGEFUL);
      dassault2.setRelationship("new_galactic_order", RepLevel.VENGEFUL);
      List<FactionAPI> factionList = sector.getAllFactions();
      factionList.remove(bladebreakers);
      Iterator var17 = factionList.iterator();

      FactionAPI faction;
      while(var17.hasNext()) {
         faction = (FactionAPI)var17.next();
         if (faction != bladebreakers && !faction.isNeutralFaction()) {
            bladebreakers.setRelationship(faction.getId(), RepLevel.VENGEFUL);
         }
      }

      bladebreakers.setRelationship("player", RepLevel.VENGEFUL);
      var17 = factionList.iterator();

      while(var17.hasNext()) {
         faction = (FactionAPI)var17.next();
         if (faction != breakerdeserter && faction != independent && !faction.isNeutralFaction()) {
            breakerdeserter.setRelationship(faction.getId(), RepLevel.HOSTILE);
         }
      }

      breakerdeserter.setRelationship(independent.getId(), RepLevel.SUSPICIOUS);
   }
}
