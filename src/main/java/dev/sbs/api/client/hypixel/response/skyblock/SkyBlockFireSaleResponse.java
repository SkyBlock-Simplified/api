package dev.sbs.api.client.hypixel.response.skyblock;

import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockFireSale;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SkyBlockFireSaleResponse {

    private boolean success;
    private @NotNull ConcurrentList<SkyBlockFireSale> sales = Concurrent.newList();

}
