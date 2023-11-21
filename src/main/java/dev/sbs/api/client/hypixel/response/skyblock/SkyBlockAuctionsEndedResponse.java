package dev.sbs.api.client.hypixel.response.skyblock;

import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockAuction;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SkyBlockAuctionsEndedResponse {

    private boolean success;
    private SkyBlockDate.RealTime lastUpdated;
    private @NotNull ConcurrentList<SkyBlockAuction.Ended> auctions = Concurrent.newList();

}
