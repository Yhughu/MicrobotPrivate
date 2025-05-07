package net.runelite.client.plugins.microbot.YhughuCooker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.microbot.YhughuCooker.enums.FoodFishSelector;

@ConfigGroup("Main")
public interface YhughuCookerConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General",
            position = 0
    )
    String generalSection = "general";

    @ConfigItem(
            keyName = "Food",
            name = "Food",
            description = "Choose the type of food",
            position = 0,
            section = generalSection
    )
    default FoodFishSelector FISH()
    {
        return FoodFishSelector.SHRIMP;
    }
}
