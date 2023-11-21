package dev.sbs.api.client.hypixel.response.skyblock.implementation;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.NbtContent;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
public class SkyBlockMuseum {

    private long value;
    private boolean appraisal;
    private @NotNull ConcurrentMap<String, Item> items = Concurrent.newMap();
    @SerializedName("special")
    private @NotNull ConcurrentList<Item> specialItems = Concurrent.newList();

    @Getter
    public static class Item {

        @SerializedName("donated_time")
        private SkyBlockDate.RealTime donated;
        private boolean borrowing;
        @SerializedName("featured_slot")
        private Optional<String> featuredSlot = Optional.empty();
        private NbtContent items;

        public boolean isFeatured() {
            return this.featuredSlot.isPresent();
        }

        public boolean notFeatured() {
            return !this.isFeatured();
        }

    }

}