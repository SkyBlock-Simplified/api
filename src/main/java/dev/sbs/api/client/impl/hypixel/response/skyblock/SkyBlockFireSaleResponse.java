package dev.sbs.api.client.impl.hypixel.response.skyblock;

import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.SkyBlockFireSale;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SkyBlockFireSaleResponse {

    private boolean success;
    private @NotNull ConcurrentList<SkyBlockFireSale> sales = Concurrent.newList();

}
