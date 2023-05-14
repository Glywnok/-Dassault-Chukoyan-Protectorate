package data.campaign.ids;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.characters.FullName.Gender;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Voices;

public class magellan_People {
   public static final String MAGELLAN_ARAL = "magellan_protectorate_aral";
   public static final String MAGELLAN_MADSCIENTIST = "magellan_protectorate_madscientist";
   public static final String MAGELLAN_LEV_OBILO = "magellan_leveller_obilo";
   public static final String MAGELLAN_LEV_VALCA = "magellan_leveller_valca";
   public static final String MAGELLAN_LEV_GHAMMOL = "magellan_leveller_ghammol";

   public static PersonAPI getPerson(String id) {
      return Global.getSector().getImportantPeople().getPerson(id);
   }

   public void advance() {
      createFactionLeaders();
      createProtectorateCharacters();
      createLevellerCharacters();
   }

   public static void createFactionLeaders() {
      ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
      MarketAPI market = null;
      market = Global.getSector().getEconomy().getMarket("dcp_magellan_planet_jeshad");
      if (market != null) {
         PersonAPI person = Global.getFactory().createPerson();
         person.setId("dcp_magellan_protectorate_aral");
         person.setFaction("magellan_protectorate");
         person.setGender(Gender.MALE);
         person.setRankId("magellan_lordregent");
         person.setPostId(Ranks.POST_FACTION_LEADER);
         person.setImportance(PersonImportance.HIGH);
         person.getName().setFirst("Aral");
         person.getName().setLast("Dalganos");
         person.setPortraitSprite(Global.getSettings().getSpriteName("characters", "dcp_magellan_aral"));
         person.getStats().setSkillLevel("coordinated_maneuvers", 2.0F);
         person.getStats().setSkillLevel("officer_training", 2.0F);
         person.getStats().setSkillLevel("officer_management", 2.0F);
         person.getStats().setSkillLevel("crew_training", 1.0F);
         person.setVoice(Voices.OFFICIAL);
         market.getCommDirectory().addPerson(person, 0);
         market.addPerson(person);
         ip.addPerson(person);
      }

   }

   public static void createProtectorateCharacters() {
      ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
      MarketAPI market = null;
      market = Global.getSector().getEconomy().getMarket("dcp_magellan_planet_pariya");
      if (market != null) {
         PersonAPI person = Global.getSector().getFaction("magellan_protectorate").createRandomPerson();
         person.setId("dcp_magellan_protectorate_madscientist");
         person.setRankId(Ranks.CITIZEN);
         person.setPostId(Ranks.POST_SCIENTIST);
         person.setImportance(PersonImportance.MEDIUM);
         person.setVoice(Voices.SCIENTIST);
         market.getCommDirectory().addPerson(person, 0);
         market.addPerson(person);
         ip.addPerson(person);
      }

   }

   public static void createLevellerCharacters() {
      ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
      MarketAPI market = null;
      market = Global.getSector().getEconomy().getMarket("dcp_station_obilotbase");
      PersonAPI person;
      if (market != null) {
         person = Global.getSector().getFaction("magellan_leveller").createRandomPerson();
         person.setId("dcp_magellan_leveller_obilo");
         person.setRankId(Ranks.TERRORIST);
         person.setPostId(Ranks.POST_OUTPOST_COMMANDER);
         person.setImportance(PersonImportance.MEDIUM);
         person.setVoice(Voices.SOLDIER);
         market.getCommDirectory().addPerson(person, 0);
         market.addPerson(person);
         ip.addPerson(person);
      }

      market = Global.getSector().getEconomy().getMarket("dcp_magellan_planet_valca");
      if (market != null) {
         person = Global.getSector().getFaction("magellan_leveller").createRandomPerson();
         person.setId("dcp_magellan_leveller_valca");
         person.setRankId(Ranks.TERRORIST);
         person.setPostId(Ranks.POST_SCIENTIST);
         person.setImportance(PersonImportance.MEDIUM);
         person.setVoice(Voices.SCIENTIST);
         market.getCommDirectory().addPerson(person, 0);
         market.addPerson(person);
         ip.addPerson(person);
      }

      market = Global.getSector().getEconomy().getMarket("dcp_station_junkyardstarport");
      if (market != null) {
         person = Global.getSector().getFaction("magellan_leveller").createRandomPerson();
         person.setId("dcp_magellan_leveller_ghammol");
         person.setRankId(Ranks.TERRORIST);
         person.setPostId(Ranks.POST_ARMS_DEALER);
         person.setImportance(PersonImportance.MEDIUM);
         person.setVoice(Voices.BUSINESS);
         market.getCommDirectory().addPerson(person, 0);
         market.addPerson(person);
         ip.addPerson(person);
      }

   }
}
