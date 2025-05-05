package net.runelite.client.plugins.microbot.YhughuWoodcutter.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
