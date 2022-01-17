package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockBazaarResponse {

    @Getter private boolean success;
    @Getter private SkyBlockDate.RealTime lastUpdated;
    @Getter private ConcurrentMap<String, Product> products = Concurrent.newMap();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product {

        @SerializedName("product_id")
        @Getter private String id;
        @SerializedName("buy_summary")
        @Getter private ConcurrentList<Summary> buySummary = Concurrent.newList();
        @SerializedName("sell_summary")
        @Getter private ConcurrentList<Summary> sellSummary = Concurrent.newList();
        @SerializedName("quick_status")
        @Getter private Status quickStatus;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Status {

        private String productId;
        @Getter private double sellPrice;
        @Getter private long sellVolume;
        @Getter private long sellMovingWeek;
        @Getter private long sellOrders;
        @Getter private double buyPrice;
        @Getter private long buyVolume;
        @Getter private long buyMovingWeek;
        @Getter private long buyOrders;

    }

    public static class Summary {

        @Getter private long amount;
        @Getter private double pricePerUnit;
        @SerializedName("orders")
        @Getter private int numberOfOrders;

    }

}
