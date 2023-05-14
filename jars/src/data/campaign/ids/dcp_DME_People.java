package data.campaign.ids;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.ImportantPeopleAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.characters.FullName.Gender;
import com.fs.starfarer.api.impl.campaign.ids.People;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Voices;

public class dcp_DME_People extends People {
   //DME
   public static final String DME_ARI = "dcp_dme_ari";
   public static final String DME_NIKO = "dcp_dme_niko";
   public static final String DME_MAYA = "dcp_dme_maya";
   public static final String DME_EMILE = "dcp_dme_emile";
   public static final String DME_ARMSDEV = "dcp_dme_armsdev";



   //Magellan
   public static final String MAGELLAN_ARAL = "dcp_magellan_protectorate_aral";
   public static final String MAGELLAN_MADSCIENTIST = "dcp_magellan_protectorate_madscientist";
   public static final String MAGELLAN_LEV_OBILO = "dcp_magellan_leveller_obilo";
   public static final String MAGELLAN_LEV_VALCA = "dcp_magellan_leveller_valca";
   public static final String MAGELLAN_LEV_GHAMMOL = "dcp_magellan_leveller_ghammol";



   public static PersonAPI getPerson(String id) {
      return Global.getSector().getImportantPeople().getPerson(id);
   }

   public void advance() {
      createFactionLeaders();
      createLavoisierBaseCharacters();
      createLaReoleCharacters();
   }

   public static void createFactionLeaders() {
      ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
      MarketAPI market = null;
      market = Global.getSector().getEconomy().getMarket("dcp_DME_planet_peremohy");
      if (market != null) {
         PersonAPI person = Global.getFactory().createPerson();
         person.setId("dcp_dme_ari");
         person.setFaction("dassault_mikoyan");
         person.setGender(Gender.FEMALE);
         person.setRankId(Ranks.FACTION_LEADER);
         person.setPostId(Ranks.POST_FACTION_LEADER);
         person.setImportance(PersonImportance.VERY_HIGH);
         person.getName().setFirst("Ariane");
         person.getName().setLast("Soisson");
         person.setPortraitSprite(Global.getSettings().getSpriteName("characters", "dcp_DME_ari"));
         person.getStats().setSkillLevel("industrial_planning", 3.0F);
         person.setVoice(Voices.OFFICIAL);
         market.getCommDirectory().addPerson(person, 0);
         market.addPerson(person);
         ip.addPerson(person);
      }

   }

   public static void createLavoisierBaseCharacters() {
      ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
      MarketAPI market = null;
      market = Global.getSector().getEconomy().getMarket("nikolaev_lab");
      PersonAPI person;
      if (market != null) {
         person = Global.getFactory().createPerson();
         person.setId("dcp_dme_emile");
         person.setFaction("dassault_mikoyan");
         person.setGender(Gender.MALE);
         person.setRankId("dcp_DME_smatterresearch");
         person.setPostId(Ranks.POST_SCIENTIST);
         person.setImportance(PersonImportance.HIGH);
         person.getName().setFirst("Émile");
         person.getName().setLast("Nguyen");
         person.setPortraitSprite(Global.getSettings().getSpriteName("characters", "dcp_DME_emile"));
         person.getStats().setSkillLevel("electronic_warfare", 3.0F);
         person.setVoice(Voices.SCIENTIST);
         market.getCommDirectory().addPerson(person, 0);
         market.addPerson(person);
         ip.addPerson(person);
      }

      if (market != null) {
         person = Global.getSector().getFaction("dassault_mikoyan").createRandomPerson();
         person.setId("dcp_dme_armsdev");
         person.setRankId(Ranks.CITIZEN);
         person.setPostId("dcp_DME_armsengineer");
         person.setImportance(PersonImportance.MEDIUM);
         person.setVoice(Voices.SCIENTIST);
         market.getCommDirectory().addPerson(person, 1);
         market.addPerson(person);
         ip.addPerson(person);
      }

   }

   public static void createLaReoleCharacters() {
      ImportantPeopleAPI ip = Global.getSector().getImportantPeople();
      MarketAPI market = null;
      market = Global.getSector().getEconomy().getMarket("dcp_DME_planet_lareole");
      PersonAPI person;
      if (market != null) {
         person = Global.getFactory().createPerson();
         person.setId("dcp_dme_niko");
         person.setFaction("dassault_mikoyan");
         person.setGender(Gender.MALE);
         person.setRankId(Ranks.SPACE_CAPTAIN);
         person.setPostId("dcp_DME_SNRIrep");
         person.setImportance(PersonImportance.HIGH);
         person.getName().setFirst("Niko");
         person.getName().setLast("Sphaleros");
         person.setPortraitSprite(Global.getSettings().getSpriteName("characters", "dcp_DME_niko"));
         person.getStats().setSkillLevel("wolfpack_tactics", 3.0F);
         person.getStats().setSkillLevel("impact_mitigation", 1.0F);
         market.getCommDirectory().addPerson(person, 0);
         market.addPerson(person);
         ip.addPerson(person);
      }

      if (market != null) {
         person = Global.getFactory().createPerson();
         person.setId("dcp_dme_maya");
         person.setFaction("dassault_mikoyan");
         person.setGender(Gender.FEMALE);
         person.setRankId(Ranks.GROUND_MAJOR);
         person.setPostId(Ranks.POST_SPECIAL_AGENT);
         person.setImportance(PersonImportance.MEDIUM);
         person.getName().setFirst("Maya");
         person.getName().setLast("Zabiarov");
         person.setPortraitSprite(Global.getSettings().getSpriteName("characters", "dcp_DME_maya"));
         person.getStats().setSkillLevel("weapon_drills", 3.0F);
         person.getStats().setSkillLevel("gunnery_implants", 3.0F);
         person.setVoice(Voices.SOLDIER);
         market.getCommDirectory().addPerson(person, 1);
         market.addPerson(person);
         ip.addPerson(person);
      }

   }


}
