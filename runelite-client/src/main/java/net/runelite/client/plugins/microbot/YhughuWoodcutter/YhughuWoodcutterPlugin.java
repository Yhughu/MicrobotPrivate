package net.runelite.client.plugins.microbot.YhughuWoodcutter;

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
        name = PluginDescriptor.Default + "YhughuWoodcutter",
        description = "Yhughu's woodcutting script",
        tags = {"Woodcutting", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class YhughuWoodcutterPlugin extends Plugin {
    @Inject
    private YhughuWoodcutterConfig config;
    @Provides
    YhughuWoodcutterConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(YhughuWoodcutterConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private YhughuWoodcutterOverlay yhughuWoodcutterOverlay;

    @Inject
    YhughuWoodcutterScript yhughuWoodcutterScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(yhughuWoodcutterOverlay);
        }
        yhughuWoodcutterScript.run(config);
    }

    protected void shutDown() {
        yhughuWoodcutterScript.shutdown();
        overlayManager.remove(yhughuWoodcutterOverlay);
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
