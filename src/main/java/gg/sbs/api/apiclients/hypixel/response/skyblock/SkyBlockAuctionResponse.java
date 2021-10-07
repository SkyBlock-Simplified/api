package gg.sbs.api.apiclients.hypixel.response.skyblock;

import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockAuctionResponse {

    @Getter private boolean success;
    @Getter private ConcurrentList<SkyBlockAuction> auctions = Concurrent.newList();

}