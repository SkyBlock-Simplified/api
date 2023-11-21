package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Experimentation {

    @SerializedName("claims_resets")
    @Getter private int resetClaims;
    @SerializedName("claims_resets_timestamp")
    @Getter private Optional<SkyBlockDate.RealTime> resetClaimsTimestamp = Optional.empty();
    @SerializedName("serums_drank")
    @Getter private int serumsDrank;

    private @NotNull ConcurrentMap<String, Long> pairings = Concurrent.newMap();
    private transient Optional<Table> superpairs;

    private @NotNull ConcurrentMap<String, Long> simon = Concurrent.newMap();
    private transient Optional<Table> chronomatron;

    private @NotNull ConcurrentMap<String, Long> numbers = Concurrent.newMap();
    private transient Optional<Table> ultrasequencer;

    public @NotNull Optional<Table> getSuperpairs() {
        if (Objects.isNull(this.superpairs))
            this.superpairs = Optional.of(new Table(this.pairings));

        return this.superpairs;
    }

    public @NotNull Optional<Table> getChronomatron() {
        if (Objects.isNull(this.chronomatron))
            this.chronomatron = Optional.of(new Table(this.simon));

        return this.chronomatron;
    }

    public @NotNull Optional<Table> getUltrasequencer() {
        if (Objects.isNull(this.ultrasequencer))
            this.ultrasequencer = Optional.of(new Table(this.numbers));

        return this.ultrasequencer;
    }

    @Getter
    public static class Table {

        private final @NotNull SkyBlockDate.RealTime lastAttempt;
        private final @NotNull SkyBlockDate.RealTime lastClaimed;
        private final int bonusClicks;
        private final @NotNull ConcurrentMap<Integer, Integer> attempts;
        private final @NotNull ConcurrentMap<Integer, Integer> claims;
        private final @NotNull ConcurrentMap<Integer, Integer> bestScore;

        private Table(@NotNull ConcurrentMap<String, Long> tableData) {
            this.lastAttempt = new SkyBlockDate.RealTime(tableData.removeOrGet("last_attempt", 0L));
            this.lastClaimed = new SkyBlockDate.RealTime(tableData.removeOrGet("last_claimed", 0L));
            this.bonusClicks = tableData.removeOrGet("bonus_clicks", 0L).intValue();

            ConcurrentMap<String, ConcurrentMap<Integer, Integer>> filteredData = Concurrent.newMap();

            tableData.forEach((key, value) -> {
                if (!filteredData.containsKey(key))
                    filteredData.put(key, Concurrent.newMap());

                String actual = key.substring(0, key.lastIndexOf("_"));
                filteredData.get(key).put(Integer.parseInt(key.replace(String.format("%s_", actual), "")), value.intValue());
            });

            this.attempts = filteredData.removeOrGet("attempts", Concurrent.newMap());
            this.claims = filteredData.removeOrGet("claims", Concurrent.newMap());
            this.bestScore = filteredData.removeOrGet("best_score", Concurrent.newMap());
        }

    }

}
