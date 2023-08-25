package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.data.model.skyblock.sack_items.SackItemModel;
import dev.sbs.api.data.model.skyblock.sacks.SackModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Sack {

    @Getter private final SackModel type;
    @Getter private final ConcurrentMap<SackItemModel, Integer> stored;

}