package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.trophy_fishes.TrophyFishModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.Pair;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class TrophyFish {

    @Getter private final ConcurrentMap<TrophyFishModel, ConcurrentMap<Tier, Integer>> fish;
    @Getter private final int totalCaught;

    TrophyFish(ConcurrentMap<String, Object> trophy_fish) {
        this.totalCaught = (int) trophy_fish.removeOrGet("total_caught", 0);

        this.fish = SimplifiedApi.getRepositoryOf(TrophyFishModel.class)
            .findAll()
            .stream()
            .map(trophyFishModel -> Pair.of(
                trophyFishModel,
                trophy_fish.stream()
                    .filter(entry -> entry.getKey().startsWith(trophyFishModel.getKey().toLowerCase()))
                    .map(entry -> Pair.of(
                        Tier.of(entry.getKey().replace(trophyFishModel.getKey(), "")).orElse(Tier.BRONZE),
                        (int) entry.getValue()
                    ))
                    .collect(Concurrent.toMap())
            ))
            .collect(Concurrent.toMap());
    }

    public enum Tier {

        BRONZE,
        SILVER,
        GOLD,
        DIAMOND;

        public static Optional<Tier> of(@NotNull String name) {
            return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst();
        }

    }

}
