package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Essence {

    @Getter private final int undead;
    @Getter private final int diamond;
    @Getter private final int dragon;
    @Getter private final int gold;
    @Getter private final int ice;
    @Getter private final int wither;
    @Getter private final int spider;
    @Getter private final int crimson;

}
