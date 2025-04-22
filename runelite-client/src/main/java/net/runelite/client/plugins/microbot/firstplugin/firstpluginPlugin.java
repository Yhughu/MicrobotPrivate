package net.runelite.client.plugins.microbot.firstplugin;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = PluginDescriptor.Default + "firstplugin",
        description = "Microbot firstplugin",
        tags = {"firstplugin", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class firstpluginPlugin extends Plugin {
    @Inject
    private firstpluginConfig config;
    @Provides
    firstpluginConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(firstpluginConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private firstpluginOverlay firstpluginOverlay;

    @Inject
    firstpluginScript firstpluginScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(firstpluginOverlay);
        }
        firstpluginScript.run(config);
    }

    protected void shutDown() {
        firstpluginScript.shutdown();
        overlayManager.remove(firstpluginOverlay);
    }
    int ticks = 10;
    @Subscribe
    public void onGameTick(GameTick tick)
    {
        //System.out.println(getName().chars().mapToObj(i -> (char)(i + 3)).map(String::valueOf).collect(Collectors.joining()));

        if (ticks > 0) {
            ticks--;
        } else {
            ticks = 10;
        }

    }

}
