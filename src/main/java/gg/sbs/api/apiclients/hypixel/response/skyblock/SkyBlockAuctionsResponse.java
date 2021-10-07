package gg.sbs.api.apiclients.hypixel.response.skyblock;

import gg.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("all")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockAuctionsResponse {

    @Getter private boolean success;
    @Getter private int page;
    @Getter private int totalPages;
    @Getter private int totalAuctions;
    @Getter private SkyBlockDate.RealTime lastUpdated;
    @Getter private ConcurrentList<SkyBlockAuction> auctions;

}