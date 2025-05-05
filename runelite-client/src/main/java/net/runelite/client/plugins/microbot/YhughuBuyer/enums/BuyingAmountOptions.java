package net.runelite.client.plugins.microbot.YhughuBuyer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum BuyingAmountOptions {
    ONE("1"),
    FIVE("5"),
    TEN("10"),
    TWENTY("20"),
    FIFTY("50");

    private final String amount;
}
