package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Currencies {

    @SerializedName("motes_purse")
    @Getter private int motes;
    @SerializedName("coin_purse")
    @Getter private double purse;
    private @NotNull ConcurrentMap<String, ConcurrentMap<String, Integer>> essence = Concurrent.newMap();

    public @NotNull ConcurrentMap<Essence, Integer> getEssence() {
        return this.essence.stream()
            .map(entry -> Pair.of(Essence.of(entry.getKey()), entry.getValue().get("current")))
            .collect(Concurrent.toMap());
    }

    public enum Essence {

        UNKNOWN,
        CRIMSON,
        DIAMOND,
        DRAGON,
        GOLD,
        ICE,
        SPIDER,
        UNDEAD,
        WITHER;

        public static @NotNull Essence of(@NotNull String name) {
            return Arrays.stream(values())
                .filter(essence -> essence.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(UNKNOWN);
        }

    }

}
