package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FairyData {

    @Getter private final int collectedFairySouls;
    @Getter private final int unclaimedFairySouls;
    @Getter private final int fairyExchanges;

}
