package net.runelite.client.plugins.microbot.YhughuWoodcutter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;

@Getter
@RequiredArgsConstructor
public enum WoodcuttingAxeSelector {
    BRONZE("bronze axe"),
    IRON("iron axe"),
    STEEL("steel axe"),
    BLACK("black axe"),
    MITHRIL("mithril axe"),
    ADAMANT("adamant axe"),
    RUNE("rune axe"),
    DRAGON("dragon axe");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
    }
