package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Potion {

    private String effect;
    private int level;
    @SerializedName("ticks_remaining")
    private int remainingTicks;
    private boolean infinite;
    private @NotNull ConcurrentList<Modifier> modifiers = Concurrent.newList();

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Modifier {

        private String key;
        @SerializedName("amp")
        private int amplifier;

    }

}
