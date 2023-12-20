package dev.sbs.api.client.impl.hypixel.response.skyblock;

import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.SkyBlockAuction;
import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SkyBlockAuctionsResponse {

    private boolean success;
    private int page;
    private int totalPages;
    private int totalAuctions;
    private SkyBlockDate.RealTime lastUpdated;
    private @NotNull ConcurrentList<SkyBlockAuction> auctions = Concurrent.newList();

}
