package data.scripts.plugins;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.dark.shaders.light.LightShader;
import org.dark.shaders.light.StandardLight;
import org.lwjgl.util.vector.Vector2f;

public class dcp_magellan_lightInjector extends BaseEveryFrameCombatPlugin {
   private static final String DATA_KEY = "dcp_magellan_LightInjector";
   private static final float SYSTEM_IN_TIME = 1.5F;
   private static final float SYSTEM_OUT_TIME = 0.75F;
   private static final Vector2f ZERO = new Vector2f();
   private CombatEngineAPI engine;

   public void advance(float amount, List<InputEventAPI> events) {
      if (this.engine != null) {
         if (!this.engine.isPaused()) {
            dcp_magellan_lightInjector.LocalData localData = (dcp_magellan_lightInjector.LocalData)this.engine.getCustomData().get("dcp_magellan_LightInjector");
            Map<ShipAPI, StandardLight> lights = localData.lights;
            List<ShipAPI> ships = this.engine.getShips();
            int shipsSize = ships.size();

            for(int i = 0; i < shipsSize; ++i) {
               ShipAPI ship = (ShipAPI)ships.get(i);
               if (!ship.isHulk()) {
                  ShipSystemAPI system = ship.getSystem();
                  if (system != null) {
                     String id = system.getId();
                     byte var12 = -1;
                     switch(id.hashCode()) {
                     case -2116229126:
                        if (id.equals("dcp_magellan_burstjets")) {
                           var12 = 0;
                        }
                        break;
                     case -423402381:
                        if (id.equals("dcp_magellan_microburn")) {
                           var12 = 2;
                        }
                        break;
                     case -117368929:
                        if (id.equals("dcp_magellan_burstjets_ftr")) {
                           var12 = 1;
                        }
                        break;
                     case 2002233756:
                        if (id.equals("dcp_magellan_microburn_capital")) {
                           var12 = 3;
                        }
                        break;
                     case 2021058299:
                        if (id.equals("dcp_magellan_burndrive")) {
                           var12 = 4;
                        }
                     }

                     Vector2f location;
                     List engines;
                     int num;
                     int enginesSize;
                     int j;
                     ShipEngineAPI eng;
                     float size;
                     Color color;
                     StandardLight light;
                     float intensity;
                     switch(var12) {
                     case 0:
                        if (!system.isActive()) {
                           break;
                        }

                        location = null;
                        if (ship.getEngineController() == null) {
                           break;
                        }

                        engines = ship.getEngineController().getShipEngines();
                        num = 0;
                        enginesSize = engines.size();
                        j = 0;

                        for(; j < enginesSize; ++j) {
                           eng = (ShipEngineAPI)engines.get(j);
                           if (eng.isActive() && !eng.isDisabled()) {
                              ++num;
                              if (location == null) {
                                 location = new Vector2f(eng.getLocation());
                              } else {
                                 Vector2f.add(location, eng.getLocation(), location);
                              }
                           }
                        }

                        if (location != null) {
                           location.scale(1.0F / (float)num);
                           if (!lights.containsKey(ship)) {
                              light = new StandardLight(location, ZERO, ZERO, (CombatEntityAPI)null);
                              intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 25.0F;
                              size = intensity * 400.0F;
                              light.setIntensity(intensity);
                              light.setSize(size);
                              color = new Color(255, 125, 100, 255);
                              light.setColor(color);
                              light.fadeIn(0.1F);
                              lights.put(ship, light);
                              LightShader.addLight(light);
                           } else {
                              light = (StandardLight)lights.get(ship);
                              light.setLocation(location);
                              if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                                 light.fadeOut(0.5F);
                              }
                           }
                        }
                        break;
                     case 1:
                        if (!system.isActive()) {
                           break;
                        }

                        location = null;
                        if (ship.getEngineController() == null) {
                           break;
                        }

                        engines = ship.getEngineController().getShipEngines();
                        num = 0;
                        enginesSize = engines.size();

                        for(j = 0; j < enginesSize; ++j) {
                           eng = (ShipEngineAPI)engines.get(j);
                           if (eng.isActive() && !eng.isDisabled()) {
                              ++num;
                              if (location == null) {
                                 location = new Vector2f(eng.getLocation());
                              } else {
                                 Vector2f.add(location, eng.getLocation(), location);
                              }
                           }
                        }

                        if (location == null) {
                           break;
                        }

                        location.scale(1.0F / (float)num);
                        if (!lights.containsKey(ship)) {
                           light = new StandardLight(location, ZERO, ZERO, (CombatEntityAPI)null);
                           intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 25.0F;
                           size = intensity * 400.0F;
                           light.setIntensity(intensity);
                           light.setSize(size);
                           color = new Color(255, 125, 100, 255);
                           light.setColor(color);
                           light.fadeIn(0.1F);
                           lights.put(ship, light);
                           LightShader.addLight(light);
                        } else {
                           light = (StandardLight)lights.get(ship);
                           light.setLocation(location);
                           if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                              light.fadeOut(0.5F);
                           }
                        }
                        break;
                     case 2:
                        if (!system.isActive()) {
                           break;
                        }

                        location = null;
                        if (ship.getEngineController() == null) {
                           break;
                        }

                        engines = ship.getEngineController().getShipEngines();
                        num = 0;
                        enginesSize = engines.size();

                        for(j = 0; j < enginesSize; ++j) {
                           eng = (ShipEngineAPI)engines.get(j);
                           if (eng.isActive() && !eng.isDisabled()) {
                              ++num;
                              if (location == null) {
                                 location = new Vector2f(eng.getLocation());
                              } else {
                                 Vector2f.add(location, eng.getLocation(), location);
                              }
                           }
                        }

                        if (location == null) {
                           break;
                        }

                        location.scale(1.0F / (float)num);
                        if (!lights.containsKey(ship)) {
                           light = new StandardLight(location, ZERO, ZERO, (CombatEntityAPI)null);
                           intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 25.0F;
                           size = intensity * 400.0F;
                           light.setIntensity(intensity);
                           light.setSize(size);
                           color = new Color(255, 125, 100, 255);
                           light.setColor(color);
                           light.fadeIn(0.1F);
                           lights.put(ship, light);
                           LightShader.addLight(light);
                        } else {
                           light = (StandardLight)lights.get(ship);
                           light.setLocation(location);
                           if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                              light.fadeOut(0.5F);
                           }
                        }
                        break;
                     case 3:
                        if (!system.isActive()) {
                           break;
                        }

                        location = null;
                        if (ship.getEngineController() == null) {
                           break;
                        }

                        engines = ship.getEngineController().getShipEngines();
                        num = 0;
                        enginesSize = engines.size();

                        for(j = 0; j < enginesSize; ++j) {
                           eng = (ShipEngineAPI)engines.get(j);
                           if (eng.isActive() && !eng.isDisabled()) {
                              ++num;
                              if (location == null) {
                                 location = new Vector2f(eng.getLocation());
                              } else {
                                 Vector2f.add(location, eng.getLocation(), location);
                              }
                           }
                        }

                        if (location == null) {
                           break;
                        }

                        location.scale(1.0F / (float)num);
                        if (!lights.containsKey(ship)) {
                           light = new StandardLight(location, ZERO, ZERO, (CombatEntityAPI)null);
                           intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 25.0F;
                           size = intensity * 400.0F;
                           light.setIntensity(intensity);
                           light.setSize(size);
                           color = new Color(255, 125, 100, 255);
                           light.setColor(color);
                           light.fadeIn(0.1F);
                           lights.put(ship, light);
                           LightShader.addLight(light);
                        } else {
                           light = (StandardLight)lights.get(ship);
                           light.setLocation(location);
                           if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                              light.fadeOut(0.5F);
                           }
                        }
                        break;
                     case 4:
                        if (system.isActive()) {
                           location = null;
                           if (ship.getEngineController() != null) {
                              engines = ship.getEngineController().getShipEngines();
                              num = 0;
                              enginesSize = engines.size();

                              for(j = 0; j < enginesSize; ++j) {
                                 eng = (ShipEngineAPI)engines.get(j);
                                 if (eng.isActive() && !eng.isDisabled()) {
                                    ++num;
                                    if (location == null) {
                                       location = new Vector2f(eng.getLocation());
                                    } else {
                                       Vector2f.add(location, eng.getLocation(), location);
                                    }
                                 }
                              }

                              if (location != null) {
                                 location.scale(1.0F / (float)num);
                                 if (!lights.containsKey(ship)) {
                                    light = new StandardLight(location, ZERO, ZERO, (CombatEntityAPI)null);
                                    intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 25.0F;
                                    size = intensity * 400.0F;
                                    light.setIntensity(intensity);
                                    light.setSize(size);
                                    color = new Color(255, 125, 100, 255);
                                    light.setColor(color);
                                    light.fadeIn(0.1F);
                                    lights.put(ship, light);
                                    LightShader.addLight(light);
                                 } else {
                                    light = (StandardLight)lights.get(ship);
                                    light.setLocation(location);
                                    if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                                       light.fadeOut(0.5F);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            Iterator iter = lights.entrySet().iterator();

            while(true) {
               Entry entry;
               ShipAPI ship;
               do {
                  if (!iter.hasNext()) {
                     return;
                  }

                  entry = (Entry)iter.next();
                  ship = (ShipAPI)entry.getKey();
               } while((ship.getSystem() == null || ship.getSystem().isActive()) && ship.isAlive());

               StandardLight light = (StandardLight)entry.getValue();
               light.unattach();
               light.fadeOut(0.0F);
               iter.remove();
            }
         }
      }
   }

   public void init(CombatEngineAPI engine) {
      this.engine = engine;
      engine.getCustomData().put("dcp_magellan_LightInjector", new dcp_magellan_lightInjector.LocalData());
   }

   private static final class LocalData {
      final Map<ShipAPI, StandardLight> lights;

      private LocalData() {
         this.lights = new LinkedHashMap(100);
      }

      // $FF: synthetic method
      LocalData(Object x0) {
         this();
      }
   }
}
