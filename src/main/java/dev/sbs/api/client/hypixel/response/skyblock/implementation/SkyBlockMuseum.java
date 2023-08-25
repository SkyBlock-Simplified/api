package dev.sbs.api.client.hypixel.response.skyblock.implementation;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.NbtContent;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockMuseum {

    @Getter private long value;
    @Getter private boolean appraisal;
    @Getter private ConcurrentMap<String, Item> items = Concurrent.newMap();
    @SerializedName("special")
    @Getter private ConcurrentList<Item> specialItems = Concurrent.newList();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Item {

        @SerializedName("donated_time")
        @Getter private SkyBlockDate.RealTime donated;
        @Getter private boolean borrowing;
        @SerializedName("featured_slot")
        private String featuredSlot;
        @Getter private NbtContent items;

        public Optional<String> getFeaturedSlot() {
            return Optional.ofNullable(this.featuredSlot);
        }

        public boolean isFeatured() {
            return this.featuredSlot != null;
        }

    }

}