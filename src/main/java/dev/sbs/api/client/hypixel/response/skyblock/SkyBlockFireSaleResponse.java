package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockFireSaleResponse {

    @Getter private boolean success;
    @Getter private ConcurrentList<Sale> sales;

    public static class Sale {

        @SerializedName("item_id")
        @Getter private String itemId;
        @Getter private long start;
        @Getter private long end;
        @Getter private int amount;
        @Getter private int price;

    }

}
