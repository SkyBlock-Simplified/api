package dev.sbs.api.client.hypixel.response.skyblock;

import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockAuctionResponse {

    @Getter private boolean success;
    @Getter private ConcurrentList<SkyBlockAuction> auctions = Concurrent.newList();

}