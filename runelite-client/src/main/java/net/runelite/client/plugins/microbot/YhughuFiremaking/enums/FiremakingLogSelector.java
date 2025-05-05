package net.runelite.client.plugins.microbot.YhughuFiremaking.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FiremakingLogSelector {
    LOGS("Logs"),
    OAK("Oak logs"),
    WILLOW("Willow logs"),
    MAPLE("Maple logs"),
    YEW("Yew logs");


    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
