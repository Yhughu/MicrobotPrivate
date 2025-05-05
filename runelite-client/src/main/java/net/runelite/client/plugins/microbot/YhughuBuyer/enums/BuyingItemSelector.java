package net.runelite.client.plugins.microbot.YhughuBuyer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum BuyingItemSelector {
    TINDERBOX("tinderbox", "varrockgeneral","Shop keeper", 0, null),
    MAGEHATS("blue wizard hat", "betty","Betty", 0, "wizard hat"),
    SARDINE("raw sardine", "gerrant","Gerrant", 180, null);

    private final String name;
    private final String location;
    private final String npcname;
    private final Integer threshold;
    private final String nameseconditem;
}
