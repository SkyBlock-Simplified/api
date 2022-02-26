package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockBazaarResponse {

    @Getter private boolean success;
    @Getter private SkyBlockDate.RealTime lastUpdated;
    @Getter private final ConcurrentMap<String, Product> products = Concurrent.newMap();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product {

        @SerializedName("product_id")
        private String id;
        @SerializedName("buy_summary")
        @Getter private ConcurrentList<Summary> buySummary = Concurrent.newList();
        @SerializedName("sell_summary")
        @Getter private ConcurrentList<Summary> sellSummary = Concurrent.newList();
        @SerializedName("quick_status")
        @Getter private Status quickStatus;

        public ItemModel getItem() {
            return SimplifiedApi.getRepositoryOf(ItemModel.class).findFirstOrNull(ItemModel::getItemId, this.id);
        }

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
