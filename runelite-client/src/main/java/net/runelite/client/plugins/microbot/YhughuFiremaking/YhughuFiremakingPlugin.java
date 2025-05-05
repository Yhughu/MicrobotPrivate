package net.runelite.client.plugins.microbot.YhughuFiremaking;

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
        name = PluginDescriptor.Default + "Yhughu's Firemaking",
        description = "A firemaking script, to burn logs in varrock west.",
        tags = {"firemaking", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class YhughuFiremakingPlugin extends Plugin {
    @Inject
    private YhughuFiremakingConfig config;
    @Provides
    YhughuFiremakingConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(YhughuFiremakingConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private YhughuFiremakingOverlay YhughuFiremakingOverlay;

    @Inject
    YhughuFiremakingScript YhughuFiremakingScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(YhughuFiremakingOverlay);
        }
        YhughuFiremakingScript.run(config);
    }

    protected void shutDown() {
        YhughuFiremakingScript.shutdown();
        overlayManager.remove(YhughuFiremakingOverlay);
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
