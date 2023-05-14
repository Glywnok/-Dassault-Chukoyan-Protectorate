package data.campaign.fleets;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;

public class dcp_DME_SigmaShield extends BaseIndustry {
   public static float DEFENSE_BONUS = 4.0F;
   public static float ALPHA_CORE_BONUS = 0.5F;

   public boolean isHidden() {
      return !this.market.getFactionId().equals("blade_breakers");
   }

   public boolean isFunctional() {
      return super.isFunctional() && this.market.getFactionId().equals("blade_breakers");
   }

   public void apply() {
      super.apply(false);
      int size = 5;
      this.applyIncomeAndUpkeep((float)size);
      float bonus = DEFENSE_BONUS;
      this.market.getStats().getDynamic().getMod("ground_defenses_mod").modifyMult(this.getModId(), 1.0F + bonus, this.getNameForModifier());
      if (this.isFunctional()) {
         applyVisuals(this.market.getPlanetEntity());
      } else {
         this.unapply();
      }

   }

   public static void applyVisuals(PlanetAPI planet) {
      if (planet != null) {
         planet.getSpec().setShieldTexture(Global.getSettings().getSpriteName("industry", "istl_shield_texture"));
         planet.getSpec().setShieldThickness(0.06F);
         planet.getSpec().setShieldColor(new Color(255, 255, 255, 100));
         planet.applySpecChanges();
      }
   }

   public static void unapplyVisuals(PlanetAPI planet) {
      if (planet != null) {
         planet.getSpec().setShieldTexture((String)null);
         planet.getSpec().setShieldThickness(0.0F);
         planet.getSpec().setShieldColor((Color)null);
         planet.applySpecChanges();
      }
   }

   public void unapply() {
      super.unapply();
      unapplyVisuals(this.market.getPlanetEntity());
      this.market.getStats().getDynamic().getMod("ground_defenses_mod").unmodifyMult(this.getModId());
   }

   public boolean isAvailableToBuild() {
      return false;
   }

   public boolean showWhenUnavailable() {
      return Global.getSector().getPlayerFaction().knowsIndustry(this.getId());
   }

   protected boolean hasPostDemandSection(boolean hasDemand, IndustryTooltipMode mode) {
      return mode != IndustryTooltipMode.NORMAL || this.isFunctional();
   }

   protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
      if (mode != IndustryTooltipMode.NORMAL || this.isFunctional()) {
         float bonus = DEFENSE_BONUS;
         this.addGroundDefensesImpactSection(tooltip, bonus, (String[])null);
      }

   }

   protected int getBaseStabilityMod() {
      return 5;
   }

   protected void applyAlphaCoreModifiers() {
      this.market.getStats().getDynamic().getMod("ground_defenses_mod").modifyMult(this.getModId(1), 1.0F + ALPHA_CORE_BONUS, "Alpha core (" + this.getNameForModifier() + ")");
   }

   protected void applyNoAICoreModifiers() {
      this.market.getStats().getDynamic().getMod("ground_defenses_mod").unmodifyMult(this.getModId(1));
   }

   protected void applyAlphaCoreSupplyAndDemandModifiers() {
      this.demandReduction.modifyFlat(this.getModId(0), (float)DEMAND_REDUCTION, "Alpha core");
   }

   protected void addAlphaCoreDescription(TooltipMakerAPI tooltip, AICoreDescriptionMode mode) {
      float opad = 10.0F;
      Color highlight = Misc.getHighlightColor();
      String pre = "Alpha-level AI core currently assigned. ";
      if (mode == AICoreDescriptionMode.MANAGE_CORE_DIALOG_LIST || mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
         pre = "Alpha-level AI core. ";
      }

      float a = ALPHA_CORE_BONUS;
      String str = "×" + (1.0F + a) + "";
      if (mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
         CommoditySpecAPI coreSpec = Global.getSettings().getCommoditySpec(this.aiCoreId);
         TooltipMakerAPI text = tooltip.beginImageWithText(coreSpec.getIconName(), 48.0F);
         text.addPara(pre + "Reduces upkeep cost by %s. Reduces demand by %s unit. Increases ground defenses by %s.", 0.0F, highlight, new String[]{"" + (int)((1.0F - UPKEEP_MULT) * 100.0F) + "%", "" + DEMAND_REDUCTION, str});
         tooltip.addImageWithText(opad);
      } else {
         tooltip.addPara(pre + "Reduces upkeep cost by %s. Reduces demand by %s unit. Increases ground defenses by %s.", opad, highlight, new String[]{"" + (int)((1.0F - UPKEEP_MULT) * 100.0F) + "%", "" + DEMAND_REDUCTION, str});
      }
   }
}
