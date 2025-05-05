package net.runelite.client.plugins.microbot.YhughuBuyer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.microbot.YhughuBuyer.enums.BuyingItemSelector;
import net.runelite.client.plugins.microbot.YhughuBuyer.enums.BuyingAmountOptions;

@ConfigGroup("YhughuWoodcutter")
public interface YhughuBuyerConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General",
            position = 0
    )
    String generalSection = "general";

    @ConfigItem(
            keyName = "Item",
            name = "Item",
            description = "Choose the item to buy.",
            position = 0,
            section = generalSection
    )
    default BuyingItemSelector ITEM()
    {
        return BuyingItemSelector.TINDERBOX;
    }

    @ConfigItem(
            keyName = "Amount",
            name = "Amount to buy",
            description = "Set how many of the items the player should buy before stopping.",
            position = 1,
            section = generalSection
    )
    default int AmountToBuy()
    {
        return 20;
    }

    @ConfigItem(
            keyName = "AmountAtOnce",
            name = "Amount to buy per click",
            description = "Set how many of the items the player should buy per click.",
            position = 1,
            section = generalSection
    )
    default BuyingAmountOptions AMOUNT()
    {
        return BuyingAmountOptions.ONE;
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
