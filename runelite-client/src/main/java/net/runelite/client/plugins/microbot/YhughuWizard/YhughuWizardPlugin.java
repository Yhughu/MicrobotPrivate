package net.runelite.client.plugins.microbot.YhughuWizard;

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
        name = PluginDescriptor.Default + "Yhughu's Wizard",
        description = "Kills enemies using magic",
        tags = {"Magic", "microbot"},
        enabledByDefault = false
)
@Slf4j
public class YhughuWizardPlugin extends Plugin {
    @Inject
    private YhughuWizardConfig config;
    @Provides
    YhughuWizardConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(YhughuWizardConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private YhughuWizardOverlay yhughuWizardOverlay;

    @Inject
    YhughuWizardScript yhughuWizardScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(yhughuWizardOverlay);
        }
        yhughuWizardScript.run(config);
    }

    protected void shutDown() {
        yhughuWizardScript.shutdown();
        overlayManager.remove(yhughuWizardOverlay);
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
