package com.fs.starfarer.api.impl.campaign.rulecmd.salvage;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoPickerListener;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.comm.CommMessageAPI;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.CoreReputationPlugin.CustomRepImpact;
import com.fs.starfarer.api.impl.campaign.CoreReputationPlugin.RepActionEnvelope;
import com.fs.starfarer.api.impl.campaign.CoreReputationPlugin.RepActions;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddRemoveCommodity;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Misc.Token;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class dcp_DME_SigmaMatterDataRecovery extends BaseCommandPlugin {
   protected CampaignFleetAPI playerFleet;
   protected SectorEntityToken entity;
   protected FactionAPI playerFaction;
   protected FactionAPI entityFaction;
   protected TextPanelAPI text;
   protected OptionPanelAPI options;
   protected CargoAPI playerCargo;
   protected MemoryAPI memory;
   protected InteractionDialogAPI dialog;
   protected Map<String, MemoryAPI> memoryMap;
   protected PersonAPI person;
   protected FactionAPI faction;
   protected boolean buysSMatter;
   protected float valueMult;
   protected float repMult;

   public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Token> params, Map<String, MemoryAPI> memoryMap) {
      this.dialog = dialog;
      this.memoryMap = memoryMap;
      String command = ((Token)params.get(0)).getString(memoryMap);
      if (command == null) {
         return false;
      } else {
         this.memory = getEntityMemory(memoryMap);
         this.entity = dialog.getInteractionTarget();
         this.text = dialog.getTextPanel();
         this.options = dialog.getOptionPanel();
         this.playerFleet = Global.getSector().getPlayerFleet();
         this.playerCargo = this.playerFleet.getCargo();
         this.playerFaction = Global.getSector().getPlayerFaction();
         this.entityFaction = this.entity.getFaction();
         this.person = dialog.getInteractionTarget().getActivePerson();
         this.faction = this.person.getFaction();
         this.buysSMatter = this.faction.getCustomBoolean("buysSMatter");
         this.valueMult = this.faction.getCustomFloat("SMatterValueMult");
         this.repMult = this.faction.getCustomFloat("SMatterRepMult");
         byte var7 = -1;
         switch(command.hashCode()) {
         case -2126230927:
            if (command.equals("personCanAcceptSMatter")) {
               var7 = 2;
            }
            break;
         case 2004760696:
            if (command.equals("selectSMatter")) {
               var7 = 0;
            }
            break;
         case 2090598107:
            if (command.equals("playerHasSMatter")) {
               var7 = 1;
            }
         }

         switch(var7) {
         case 0:
            this.selectSMatter();
         default:
            return true;
         case 1:
            return this.playerHasSMatter();
         case 2:
            return this.personCanAcceptSMatter();
         }
      }
   }

   protected boolean personCanAcceptSMatter() {
      if (this.person != null && this.buysSMatter) {
         return Ranks.POST_SCIENTIST.equals(this.person.getPostId()) || "dcp_DME_smatterresearch".equals(this.person.getRankId()) || "dcp_DME_SNRIrep".equals(this.person.getPostId());
      } else {
         return false;
      }
   }

   protected void selectSMatter() {
      CargoAPI copy = Global.getFactory().createCargo(false);
      Iterator var2 = this.playerCargo.getStacksCopy().iterator();

      while(var2.hasNext()) {
         CargoStackAPI stack = (CargoStackAPI)var2.next();
         CommoditySpecAPI spec = stack.getResourceIfResource();
         if (spec != null && spec.hasTag("sigma_matter")) {
            copy.addFromStack(stack);
         }
      }

      copy.sort();
      float width = 310.0F;
      this.dialog.showCargoPickerDialog("Select Sigma matter to turn in", "Confirm", "Cancel", true, 310.0F, copy, new CargoPickerListener() {
         public void pickedCargo(CargoAPI cargo) {
            cargo.sort();
            Iterator var2 = cargo.getStacksCopy().iterator();

            while(var2.hasNext()) {
               CargoStackAPI stack = (CargoStackAPI)var2.next();
               dcp_DME_SigmaMatterDataRecovery.this.playerCargo.removeItems(stack.getType(), stack.getData(), stack.getSize());
               if (stack.isCommodityStack()) {
                  AddRemoveCommodity.addCommodityLossText(stack.getCommodityId(), (int)stack.getSize(), dcp_DME_SigmaMatterDataRecovery.this.text);
               }
            }

            float bounty = dcp_DME_SigmaMatterDataRecovery.this.computeSMatterCreditValue(cargo);
            float repChange = dcp_DME_SigmaMatterDataRecovery.this.computeSMatterReputationValue(cargo);
            if (bounty > 0.0F) {
               dcp_DME_SigmaMatterDataRecovery.this.playerCargo.getCredits().add(bounty);
               AddRemoveCommodity.addCreditsGainText((int)bounty, dcp_DME_SigmaMatterDataRecovery.this.text);
            }

            if (repChange >= 1.0F) {
               CustomRepImpact impact = new CustomRepImpact();
               impact.delta = repChange * 0.01F;
               Global.getSector().adjustPlayerReputation(new RepActionEnvelope(RepActions.CUSTOM, impact, (CommMessageAPI)null, dcp_DME_SigmaMatterDataRecovery.this.text, true), dcp_DME_SigmaMatterDataRecovery.this.faction.getId());
               impact.delta *= 0.25F;
               if (impact.delta >= 0.01F) {
                  Global.getSector().adjustPlayerReputation(new RepActionEnvelope(RepActions.CUSTOM, impact, (CommMessageAPI)null, dcp_DME_SigmaMatterDataRecovery.this.text, true), dcp_DME_SigmaMatterDataRecovery.this.person);
               }
            }

            FireBest.fire((String)null, dcp_DME_SigmaMatterDataRecovery.this.dialog, dcp_DME_SigmaMatterDataRecovery.this.memoryMap, "SigmaMatterTurnedIn");
         }

         public void cancelledCargoSelection() {
         }

         public void recreateTextPanel(TooltipMakerAPI panel, CargoAPI cargo, CargoStackAPI pickedUp, boolean pickedUpFromSource, CargoAPI combined) {
            float bounty = dcp_DME_SigmaMatterDataRecovery.this.computeSMatterCreditValue(combined);
            float repChange = dcp_DME_SigmaMatterDataRecovery.this.computeSMatterReputationValue(combined);
            float pad = 3.0F;
            float small = 5.0F;
            float opad = 10.0F;
            panel.setParaFontOrbitron();
            panel.addPara(Misc.ucFirst(dcp_DME_SigmaMatterDataRecovery.this.faction.getDisplayName()), dcp_DME_SigmaMatterDataRecovery.this.faction.getBaseUIColor(), 1.0F);
            panel.setParaFontDefault();
            panel.addImage(dcp_DME_SigmaMatterDataRecovery.this.faction.getLogo(), 310.0F, 3.0F);
            panel.addPara("Compared to dealing with other factions, turning Sigma matter in to " + dcp_DME_SigmaMatterDataRecovery.this.faction.getDisplayNameLongWithArticle() + " will result in:", opad);
            panel.beginGridFlipped(310.0F, 1, 40.0F, 10.0F);
            panel.addToGrid(0, 0, "Payment value", "" + (int)(dcp_DME_SigmaMatterDataRecovery.this.valueMult * 100.0F) + "%");
            panel.addToGrid(0, 1, "Reputation gain", "" + (int)(dcp_DME_SigmaMatterDataRecovery.this.repMult * 100.0F) + "%");
            panel.addGrid(pad);
            panel.addPara("If you turn in the selected Sigma matter, you will receive %s in compensation and your standing with " + dcp_DME_SigmaMatterDataRecovery.this.faction.getDisplayNameWithArticle() + " will improve by %s points.", opad * 1.0F, Misc.getHighlightColor(), new String[]{Misc.getWithDGS(bounty) + "¢", "" + (int)repChange});
         }
      });
   }

   protected float computeSMatterCreditValue(CargoAPI cargo) {
      float bounty = 0.0F;
      Iterator var3 = cargo.getStacksCopy().iterator();

      while(var3.hasNext()) {
         CargoStackAPI stack = (CargoStackAPI)var3.next();
         CommoditySpecAPI spec = stack.getResourceIfResource();
         if (spec != null && spec.hasTag("sigma_matter")) {
            bounty += spec.getBasePrice() * stack.getSize();
         }
      }

      bounty *= this.valueMult;
      return bounty;
   }

   protected float computeSMatterReputationValue(CargoAPI cargo) {
      float rep = 0.0F;
      Iterator var3 = cargo.getStacksCopy().iterator();

      while(var3.hasNext()) {
         CargoStackAPI stack = (CargoStackAPI)var3.next();
         CommoditySpecAPI spec = stack.getResourceIfResource();
         if (spec != null && spec.hasTag("sigma_matter")) {
            rep += getBaseRepValue(spec.getId()) * stack.getSize();
         }
      }

      rep *= this.repMult;
      return rep;
   }

   public static float getBaseRepValue(String dcp_DME_SigmaMatterType) {
      if ("dcp_DME_sigma_matter2".equals(dcp_DME_SigmaMatterType)) {
         return 7.0F;
      } else if ("dcp_DME_sigma_matter1".equals(dcp_DME_SigmaMatterType)) {
         return 5.0F;
      } else {
         return "dcp_DME_sigma_matter3".equals(dcp_DME_SigmaMatterType) ? 2.0F : 1.0F;
      }
   }

   protected boolean playerHasSMatter() {
      Iterator var1 = this.playerCargo.getStacksCopy().iterator();

      CommoditySpecAPI spec;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         CargoStackAPI stack = (CargoStackAPI)var1.next();
         spec = stack.getResourceIfResource();
      } while(spec == null || !spec.hasTag("sigma_matter"));

      return true;
   }
}
