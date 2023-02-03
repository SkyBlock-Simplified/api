package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
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
    private ConcurrentLinkedMap<Integer, String> completed = Concurrent.newLinkedMap();
    private ConcurrentLinkedMap<Integer, String> last_viewed_tasks = Concurrent.newLinkedMap();

    public ConcurrentList<String> getCompletedTasks() {
        return Concurrent.newUnmodifiableList(this.completed.values());
    }

    public ConcurrentList<String> getLastViewedTasks() {
        return Concurrent.newUnmodifiableList(this.last_viewed_tasks.values());
    }

    public int getLevel() {
        return (int) Math.floor(this.getExperience() / 100.0);
    }

}
