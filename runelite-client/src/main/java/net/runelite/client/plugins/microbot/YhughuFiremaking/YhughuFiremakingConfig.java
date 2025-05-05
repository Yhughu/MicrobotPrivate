package net.runelite.client.plugins.microbot.YhughuFiremaking;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.microbot.YhughuFiremaking.enums.FiremakingLogSelector;

@ConfigGroup("Main")
public interface YhughuFiremakingConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General",
            position = 0
    )
    String generalSection = "general";

    @ConfigItem(
            keyName = "Log",
            name = "Log",
            description = "Choose the type of logs",
            position = 0,
            section = generalSection
    )
    default FiremakingLogSelector LOG()
    {
        return FiremakingLogSelector.LOGS;
    }
}
