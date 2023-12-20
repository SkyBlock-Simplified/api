package dev.sbs.api.client.impl.hypixel.response.skyblock;

import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.SkyBlockBazaarProduct;
import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.Getter;

@Getter
public class SkyBlockBazaarResponse {

    private boolean success;
    private SkyBlockDate.RealTime lastUpdated;
    private final ConcurrentMap<String, SkyBlockBazaarProduct> products = Concurrent.newMap();

}
