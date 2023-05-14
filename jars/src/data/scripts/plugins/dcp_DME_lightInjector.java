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

public class dcp_DME_lightInjector extends BaseEveryFrameCombatPlugin {
   private static final String DATA_KEY = "DME_LightInjector";
   private static final float SYSTEM_IN_TIME = 1.5F;
   private static final float SYSTEM_OUT_TIME = 0.75F;
   private static final Vector2f ZERO = new Vector2f();
   private CombatEngineAPI engine;

   public void advance(float amount, List<InputEventAPI> events) {
      if (this.engine != null) {
         if (!this.engine.isPaused()) {
            dcp_DME_lightInjector.LocalData localData = (dcp_DME_lightInjector.LocalData)this.engine.getCustomData().get("DME_LightInjector");
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
                     case -2047486922:
                        if (id.equals("istl_BBpulsejets")) {
                           var12 = 8;
                        }
                        break;
                     case -1679772003:
                        if (id.equals("istl_bravejets")) {
                           var12 = 9;
                        }
                        break;
                     case -803047367:
                        if (id.equals("istl_heavyburn")) {
                           var12 = 6;
                        }
                        break;
                     case -507108695:
                        if (id.equals("istl_ramdrive")) {
                           var12 = 4;
                        }
                        break;
                     case -55722704:
                        if (id.equals("istl_jetinjector")) {
                           var12 = 3;
                        }
                        break;
                     case 123430954:
                        if (id.equals("istl_dockjets")) {
                           var12 = 5;
                        }
                        break;
                     case 805851446:
                        if (id.equals("istl_pulsejets")) {
                           var12 = 0;
                        }
                        break;
                     case 1215710802:
                        if (id.equals("istl_slammerjets")) {
                           var12 = 7;
                        }
                        break;
                     case 1688044178:
                        if (id.equals("istl_pulsejets_frg")) {
                           var12 = 2;
                        }
                        break;
                     case 1688044251:
                        if (id.equals("istl_pulsejets_ftr")) {
                           var12 = 1;
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
                              color = new Color(105, 145, 255, 255);
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
                           color = new Color(100, 100, 255, 255);
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
                           color = new Color(100, 100, 255, 255);
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
                           color = new Color(100, 100, 255, 255);
                           light.setColor(color);
                           light.fadeIn(2.0F);
                           lights.put(ship, light);
                           LightShader.addLight(light);
                        } else {
                           light = (StandardLight)lights.get(ship);
                           light.setLocation(location);
                           if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                              light.fadeOut(1.0F);
                           }
                        }
                        break;
                     case 4:
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
                           color = new Color(255, 100, 100, 255);
                           light.setColor(color);
                           light.fadeIn(2.0F);
                           lights.put(ship, light);
                           LightShader.addLight(light);
                        } else {
                           light = (StandardLight)lights.get(ship);
                           light.setLocation(location);
                           if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                              light.fadeOut(1.0F);
                           }
                        }
                        break;
                     case 5:
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
                           intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 40.0F;
                           size = intensity * 600.0F;
                           light.setIntensity(intensity);
                           light.setSize(size);
                           color = null;
                           if (!ship.getEngineController().getShipEngines().isEmpty()) {
                              color = ((ShipEngineAPI)ship.getEngineController().getShipEngines().get(0)).getEngineColor();
                           }

                           if (color != null) {
                              light.setColor(color);
                           }

                           light.fadeIn(0.25F);
                           lights.put(ship, light);
                           LightShader.addLight(light);
                        } else {
                           light = (StandardLight)lights.get(ship);
                           light.setLocation(location);
                           if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                              light.fadeOut(1.0F);
                           }
                        }
                        break;
                     case 6:
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
                           intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 40.0F;
                           size = intensity * 600.0F;
                           light.setIntensity(intensity);
                           light.setSize(size);
                           color = null;
                           if (!ship.getEngineController().getShipEngines().isEmpty()) {
                              color = ((ShipEngineAPI)ship.getEngineController().getShipEngines().get(0)).getEngineColor();
                           }

                           if (color != null) {
                              light.setColor(color);
                           }

                           light.fadeIn(0.25F);
                           lights.put(ship, light);
                           LightShader.addLight(light);
                        } else {
                           light = (StandardLight)lights.get(ship);
                           light.setLocation(location);
                           if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                              light.fadeOut(1.0F);
                           }
                        }
                        break;
                     case 7:
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
                           intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 40.0F;
                           size = intensity * 600.0F;
                           light.setIntensity(intensity);
                           light.setSize(size);
                           color = null;
                           if (!ship.getEngineController().getShipEngines().isEmpty()) {
                              color = ((ShipEngineAPI)ship.getEngineController().getShipEngines().get(0)).getEngineColor();
                           }

                           if (color != null) {
                              light.setColor(color);
                           }

                           light.fadeIn(0.25F);
                           lights.put(ship, light);
                           LightShader.addLight(light);
                        } else {
                           light = (StandardLight)lights.get(ship);
                           light.setLocation(location);
                           if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                              light.fadeOut(1.0F);
                           }
                        }
                        break;
                     case 8:
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
                           intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 40.0F;
                           size = intensity * 600.0F;
                           light.setIntensity(intensity);
                           light.setSize(size);
                           color = null;
                           if (!ship.getEngineController().getShipEngines().isEmpty()) {
                              color = ((ShipEngineAPI)ship.getEngineController().getShipEngines().get(0)).getEngineColor();
                           }

                           if (color != null) {
                              light.setColor(color);
                           }

                           light.fadeIn(0.25F);
                           lights.put(ship, light);
                           LightShader.addLight(light);
                        } else {
                           light = (StandardLight)lights.get(ship);
                           light.setLocation(location);
                           if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                              light.fadeOut(1.0F);
                           }
                        }
                        break;
                     case 9:
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
                                    intensity = (float)Math.sqrt((double)ship.getCollisionRadius()) / 40.0F;
                                    size = intensity * 600.0F;
                                    light.setIntensity(intensity);
                                    light.setSize(size);
                                    color = null;
                                    if (!ship.getEngineController().getShipEngines().isEmpty()) {
                                       color = ((ShipEngineAPI)ship.getEngineController().getShipEngines().get(0)).getEngineColor();
                                    }

                                    if (color != null) {
                                       light.setColor(color);
                                    }

                                    light.fadeIn(0.25F);
                                    lights.put(ship, light);
                                    LightShader.addLight(light);
                                 } else {
                                    light = (StandardLight)lights.get(ship);
                                    light.setLocation(location);
                                    if ((system.isActive() && !system.isOn() || system.isChargedown()) && !light.isFadingOut()) {
                                       light.fadeOut(1.0F);
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
      engine.getCustomData().put("DME_LightInjector", new dcp_DME_lightInjector.LocalData());
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
