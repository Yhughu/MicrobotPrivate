package net.runelite.client.plugins.microbot.YhughuWizard;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.microbot.YhughuWizard.enums.EnemySelector;
import net.runelite.client.plugins.microbot.util.misc.Rs2Food;

@ConfigGroup("Main")
public interface YhughuWizardConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "General",
            position = 0
    )
    String generalSection = "general";

    @ConfigSection(
            name = "Food",
            description = "Food Items",
            position = 0
    )
    String FoodSection = "food";

    @ConfigItem(
            keyName = "Enemy",
            name = "Enemy",
            description = "Pick an enemy.",
            position = 0,
            section = generalSection
    )
    default EnemySelector Enemy()
    {
        return EnemySelector.CHICKEN;
    }

    @ConfigItem(
            keyName = "CustomEnemy",
            name = "CustomEnemy",
            description = "Pick a custom enemy. Also set Enemy to Custom for this to work.",
            position = 0,
            section = generalSection
    )
    default String CustomEnemy()
    {
        return "Frog";
    }

    @ConfigItem(
            keyName = "GePriceToLoot",
            name = "GE Price to loot from",
            description = "From which value to pick items up.",
            position = 1,
            section = generalSection
    )
    default int PriceToLoot(){return 500;}

    @ConfigItem(
            keyName = "LootItemException",
            name = "Additional items to loot",
            description = "Additional items to loot.",
            position = 2,
            section = generalSection
    )
    default String AdditionalItem(){return "feather";}

    @ConfigItem(
            keyName = "IsBury",
            name = "Bury remains?",
            description = "Toggle to turn on or off.",
            position = 3,
            section = generalSection
    )
    default boolean IsBury() {
        return true;
    }

    @ConfigItem(
            keyName = "Food",
            name = "Food Type",
            description = "Pick a food type.",
            position = 0,
            section = FoodSection
    )
    default Rs2Food food() {
        return Rs2Food.PIKE;
    }

    @ConfigItem(
            keyName = "Food At Percentage",
            name = "Food At Percentage",
            description = "Pick a HP percentage to eat food at.",
            position = 0,
            section = FoodSection
    )
    default int FoodPercentage() {
        return 70;
    }
}
