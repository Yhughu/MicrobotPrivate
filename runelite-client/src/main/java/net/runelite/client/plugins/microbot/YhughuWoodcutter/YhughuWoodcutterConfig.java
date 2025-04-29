package net.runelite.client.plugins.microbot.YhughuWoodcutter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.microbot.YhughuWoodcutter.enums.WoodcuttingAxeSelector;
import net.runelite.client.plugins.microbot.YhughuWoodcutter.enums.WoodcuttingTreeSelector;

@ConfigGroup("YhughuWoodcutter")
public interface YhughuWoodcutterConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General",
            position = 0
    )
    String generalSection = "general";

    @ConfigItem(
            keyName = "Tree",
            name = "Tree",
            description = "Choose the tree",
            position = 0,
            section = generalSection
    )
    default WoodcuttingTreeSelector TREE()
    {
        return WoodcuttingTreeSelector.TREE;
    }

    @ConfigItem(
            keyName = "Axe",
            name = "Axe",
            description = "Choose the axe you want to use",
            position = 0,
            section = generalSection
    )
    default WoodcuttingAxeSelector AXE()
    {
        return WoodcuttingAxeSelector.BRONZE;
    }



/*    @ConfigItem(
            keyName = "Ore",
            name = "Ore",
            description = "Choose the ore",
            position = 0
    )
    default List<String> ORE()
    {
        return Rocks.TIN;
    }*/
}
