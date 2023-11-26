package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.gson.SerializedPath;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
public class Bestiary {

    @SerializedName("migrated_stats")
    private boolean migratedStats;
    @Accessors(fluent = true)
    @SerializedName("migrated")
    private boolean hasMigrated;
    private @NotNull ConcurrentMap<String, Integer> kills = Concurrent.newMap();
    private @NotNull ConcurrentMap<String, Integer> deaths = Concurrent.newMap();
    @SerializedPath("milestone.last_claimed_milestone")
    private int lastClaimedMilestone;

}
