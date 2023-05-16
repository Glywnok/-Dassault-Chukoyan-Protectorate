package data.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CustomEntitySpecAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.SectorEntityToken.VisibilityLevel;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.campaign.BaseCustomEntityPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.campaign.procgen.themes.BladeBreakerThemeGenerator;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class dcp_DME_BladeBreakerBeaconPlugin extends BaseCustomEntityPlugin {
   public static String GLOW_COLOR_KEY = "$core_beaconGlowColor";
   public static String PING_COLOR_KEY = "$core_beaconPingColor";
   public static float GLOW_FREQUENCY = 1.2F;
   private transient SpriteAPI sprite;
   private transient SpriteAPI glow;
   private float phase = 0.0F;
   private float freqMult = 1.0F;
   private float sincePing = 10.0F;

   public void init(SectorEntityToken entity, Object pluginParams) {
      super.init(entity, pluginParams);
      entity.setDetectionRangeDetailsOverrideMult(0.75F);
      this.readResolve();
   }

   Object readResolve() {
      this.sprite = Global.getSettings().getSprite("campaignEntities", "dcp_DME_breaker_beacon");
      this.glow = Global.getSettings().getSprite("campaignEntities", "dcp_DME_breaker_beacon_glow");
      return this;
   }

   public void advance(float amount) {
      for(this.phase += amount * GLOW_FREQUENCY * this.freqMult; this.phase > 1.0F; --this.phase) {
      }

      if (this.entity.isInCurrentLocation()) {
         this.sincePing += amount;
         if (this.sincePing >= 6.0F && this.phase > 0.1F && this.phase < 0.2F) {
            this.sincePing = 0.0F;
            CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
            if (playerFleet != null && this.entity.getVisibilityLevelTo(playerFleet) == VisibilityLevel.COMPOSITION_AND_FACTION_DETAILS) {
               String pingId = "warning_beacon1";
               this.freqMult = 1.0F;
               if (this.entity.getMemoryWithoutUpdate().getBoolean(BladeBreakerThemeGenerator.BladeBreakerSystemType.SUPPRESSED.getBeaconFlag())) {
                  pingId = "warning_beacon2";
                  this.freqMult = 1.25F;
               } else if (this.entity.getMemoryWithoutUpdate().getBoolean(BladeBreakerThemeGenerator.BladeBreakerSystemType.RESURGENT.getBeaconFlag())) {
                  pingId = "warning_beacon3";
                  this.freqMult = 1.5F;
               }

               Color pingColor = new Color(75, 255, 175, 255);
               if (this.entity.getMemoryWithoutUpdate().contains(PING_COLOR_KEY)) {
                  pingColor = (Color)this.entity.getMemoryWithoutUpdate().get(PING_COLOR_KEY);
               }

               Global.getSector().addPing(this.entity, pingId, pingColor);
            }
         }
      }

   }

   public float getRenderRange() {
      return this.entity.getRadius() + 100.0F;
   }

   public void render(CampaignEngineLayers layer, ViewportAPI viewport) {
      float alphaMult = viewport.getAlphaMult();
      if (!(alphaMult <= 0.0F)) {
         CustomEntitySpecAPI spec = this.entity.getCustomEntitySpec();
         if (spec != null) {
            float w = spec.getSpriteWidth();
            float h = spec.getSpriteHeight();
            Vector2f loc = this.entity.getLocation();
            this.sprite.setAngle(this.entity.getFacing() - 90.0F);
            this.sprite.setSize(w, h);
            this.sprite.setAlphaMult(alphaMult);
            this.sprite.setNormalBlend();
            this.sprite.renderAtCenter(loc.x, loc.y);
            float glowAlpha = 0.0F;
            if (this.phase < 0.5F) {
               glowAlpha = this.phase * 2.0F;
            }

            if (this.phase >= 0.5F) {
               glowAlpha = 1.0F - (this.phase - 0.5F) * 2.0F;
            }

            float glowAngle1 = (this.phase * 1.3F % 1.0F - 0.5F) * 12.0F;
            float glowAngle2 = (this.phase * 1.9F % 1.0F - 0.5F) * 12.0F;
            boolean glowAsLayer = true;
            if (glowAsLayer) {
               Color glowColor = new Color(75, 255, 175, 255);
               if (this.entity.getMemoryWithoutUpdate().contains(GLOW_COLOR_KEY)) {
                  glowColor = (Color)this.entity.getMemoryWithoutUpdate().get(GLOW_COLOR_KEY);
               }

               this.glow.setColor(glowColor);
               this.glow.setSize(w, h);
               this.glow.setAlphaMult(alphaMult * glowAlpha);
               this.glow.setAdditiveBlend();
               this.glow.setAngle(this.entity.getFacing() - 90.0F + glowAngle1);
               this.glow.renderAtCenter(loc.x, loc.y);
               this.glow.setAngle(this.entity.getFacing() - 90.0F + glowAngle2);
               this.glow.setAlphaMult(alphaMult * glowAlpha * 0.5F);
               this.glow.renderAtCenter(loc.x, loc.y);
            } else {
               this.glow.setAngle(this.entity.getFacing() - 90.0F);
               this.glow.setColor(new Color(125, 255, 255, 255));
               float gs = w * 3.0F;
               this.glow.setSize(gs, gs);
               this.glow.setAdditiveBlend();
               float spacing = 10.0F;
               this.glow.setAlphaMult(alphaMult * glowAlpha * 0.5F);
               this.glow.renderAtCenter(loc.x - spacing, loc.y);
               this.glow.renderAtCenter(loc.x + spacing, loc.y);
               this.glow.setAlphaMult(alphaMult * glowAlpha);
               this.glow.setSize(gs * 0.25F, gs * 0.25F);
               this.glow.renderAtCenter(loc.x - spacing, loc.y);
               this.glow.renderAtCenter(loc.x + spacing, loc.y);
            }

         }
      }
   }

   public void createMapTooltip(TooltipMakerAPI tooltip, boolean expanded) {
      String post = "";
      Color color = this.entity.getFaction().getBaseUIColor();
      Color postColor = color;
      if (this.entity.getMemoryWithoutUpdate().getBoolean(BladeBreakerThemeGenerator.BladeBreakerSystemType.DESTROYED.getBeaconFlag())) {
         post = " - Low";
         postColor = Misc.getPositiveHighlightColor();
      } else if (this.entity.getMemoryWithoutUpdate().getBoolean(BladeBreakerThemeGenerator.BladeBreakerSystemType.SUPPRESSED.getBeaconFlag())) {
         post = " - Medium";
         postColor = Misc.getHighlightColor();
      } else if (this.entity.getMemoryWithoutUpdate().getBoolean(BladeBreakerThemeGenerator.BladeBreakerSystemType.RESURGENT.getBeaconFlag())) {
         post = " - High";
         postColor = Misc.getNegativeHighlightColor();
      }

      tooltip.addPara(this.entity.getName() + post, 0.0F, color, postColor, new String[]{post.replaceFirst(" - ", "")});
   }

   public boolean hasCustomMapTooltip() {
      return true;
   }

   public void appendToCampaignTooltip(TooltipMakerAPI tooltip, VisibilityLevel level) {
      if (level == VisibilityLevel.COMPOSITION_AND_FACTION_DETAILS || level == VisibilityLevel.COMPOSITION_DETAILS) {
         String post = "";
         Color color = Misc.getTextColor();
         Color postColor = color;
         if (this.entity.getMemoryWithoutUpdate().getBoolean(BladeBreakerThemeGenerator.BladeBreakerSystemType.DESTROYED.getBeaconFlag())) {
            post = "low";
            postColor = Misc.getPositiveHighlightColor();
         } else if (this.entity.getMemoryWithoutUpdate().getBoolean(BladeBreakerThemeGenerator.BladeBreakerSystemType.SUPPRESSED.getBeaconFlag())) {
            post = "medium";
            postColor = Misc.getHighlightColor();
         } else if (this.entity.getMemoryWithoutUpdate().getBoolean(BladeBreakerThemeGenerator.BladeBreakerSystemType.RESURGENT.getBeaconFlag())) {
            post = "high";
            postColor = Misc.getNegativeHighlightColor();
         }

         if (!post.isEmpty()) {
            tooltip.setParaFontDefault();
            tooltip.addPara("    - Danger level: " + post, 10.0F, color, postColor, new String[]{post});
         }
      }

   }
}
