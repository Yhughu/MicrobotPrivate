package net.runelite.client.plugins.microbot.YhughuCooker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FoodFishSelector {
    SHRIMP("Raw shrimps"),
    SARDINE("Raw sardine"),
    HERRING("Raw herring"),
    TROUT("Raw trout"),
    PIKE("Raw pike"),
    SALMON("Raw salmon"),
    TUNA("Raw tuna"),
    LOBSTER("Raw lobster"),
    SWORDFISH("Raw swordfish");


    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
