package net.runelite.client.plugins.microbot.YhughuBuyer;

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
        name = PluginDescriptor.Default + "YhughuBuyer",
        description = "Yhughu's item buyer script",
        tags = {"shopping", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class YhughuBuyerPlugin extends Plugin {
    @Inject
    private YhughuBuyerConfig config;
    @Provides
    YhughuBuyerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(YhughuBuyerConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private YhughuBuyerOverlay yhughuBuyerOverlay;

    @Inject
    YhughuBuyerScript yhughuBuyerScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(yhughuBuyerOverlay);
        }
        yhughuBuyerScript.run(config);
    }

    protected void shutDown() {
        yhughuBuyerScript.shutdown();
        overlayManager.remove(yhughuBuyerOverlay);
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
