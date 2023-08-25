package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.potions;

import dev.sbs.api.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PotionData {

    @Getter private final ConcurrentList<Potion> active;
    @Getter private final ConcurrentList<Potion> paused;
    @Getter private final ConcurrentList<String> disabled;

}
