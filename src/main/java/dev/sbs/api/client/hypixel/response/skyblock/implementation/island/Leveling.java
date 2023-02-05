package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Leveling {

    @Getter private int experience;
    @SerializedName("migrated_completions")
    @Getter private boolean migratedCompletions;
    @SerializedName("category_expanded")
    @Getter private boolean categoriesExpanded;
    @SerializedName("completed")
    private ConcurrentList<String> completedTasks = Concurrent.newList();
    @SerializedName("last_viewed_tasks")
    private ConcurrentList<String> lastViewedTasks = Concurrent.newList();

    public int getLevel() {
        return (int) Math.floor(this.getExperience() / 100.0);
    }

}
