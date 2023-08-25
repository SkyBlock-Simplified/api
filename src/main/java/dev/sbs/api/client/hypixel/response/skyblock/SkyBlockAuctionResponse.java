package dev.sbs.api.client.hypixel.response.skyblock;

import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockAuction;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockAuctionResponse {

    @Getter private boolean success;
    @Getter private ConcurrentList<SkyBlockAuction> auctions = Concurrent.newList();

}
