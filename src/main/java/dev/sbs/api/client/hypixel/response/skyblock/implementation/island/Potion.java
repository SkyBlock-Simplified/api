package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Potion {

    @Getter private String effect;
    @Getter private int level;
    @SerializedName("ticks_remaining")
    @Getter private int remainingTicks;
    @Getter private boolean infinite;
    @Getter private ConcurrentList<Modifier> modifiers = Concurrent.newList();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Modifier {

        @Getter private String key;
        @SerializedName("amp")
        @Getter private int amplifier;

    }

}
