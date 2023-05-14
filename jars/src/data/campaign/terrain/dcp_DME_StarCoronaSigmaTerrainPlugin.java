package data.campaign.terrain;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.impl.campaign.terrain.AuroraRenderer;
import com.fs.starfarer.api.impl.campaign.terrain.FlareManager;
import com.fs.starfarer.api.impl.campaign.terrain.RangeBlockerUtil;
import com.fs.starfarer.api.impl.campaign.terrain.StarCoronaTerrainPlugin;
import com.fs.starfarer.api.loading.Description.Type;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;

public class dcp_DME_StarCoronaSigmaTerrainPlugin extends StarCoronaTerrainPlugin {
   public Color getAuroraColorForAngle(float angle) {
      if (this.color == null) {
         if (this.params.relatedEntity instanceof PlanetAPI) {
            this.color = new Color(50, 30, 100, 120);
         } else {
            this.color = Color.white;
         }

         this.color = Misc.setAlpha(this.color, 155);
      }

      return this.flareManager.isInActiveFlareArc(angle) ? this.flareManager.getColorForAngle(this.color, angle) : super.getAuroraColorForAngle(angle);
   }

   public String getTerrainName() {
      return "Dimensional Turbulence";
   }

   protected Object readResolve() {
      super.readResolve();
      this.texture = Global.getSettings().getSprite("terrain", "aurora");
      if (this.renderer == null) {
         this.renderer = new AuroraRenderer(this);
      }

      if (this.flareManager == null) {
         this.flareManager = new FlareManager(this);
      }

      if (this.blocker == null) {
         this.blocker = new RangeBlockerUtil(360, super.params.bandWidthInEngine + 1000.0F);
      }

      return this;
   }

   public void createTooltip(TooltipMakerAPI tooltip, boolean expanded) {
      float pad = 10.0F;
      float small = 5.0F;
      tooltip.addTitle("Dimensional Turbulence");
      tooltip.addPara(Global.getSettings().getDescription(this.getTerrainId(), Type.TERRAIN).getText1(), pad);
      float nextPad = pad;
      if (expanded) {
         tooltip.addSectionHeading("Travel", Alignment.MID, small);
         nextPad = small;
      }

      tooltip.addPara("The intense radiation and gravitational distortion reduces the combat readiness of all ships in the turbulent region at a steady pace.", nextPad);
      tooltip.addPara("The dimensional brane shifts make the planet difficult to approach.", pad);
      if (expanded) {
         tooltip.addSectionHeading("Combat", Alignment.MID, pad);
         tooltip.addPara("Reduces the peak performance time of ships and increases the rate of combat readiness degradation in protracted engagements.", small);
      }

   }
}
