package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Leveling {

    @Getter private int experience;
    @SerializedName("category_expanded")
    @Getter private boolean categoriesExpanded;
    @SerializedName("last_viewed_tasks")
    @Getter private ConcurrentList<String> lastViewedTasks = Concurrent.newList();
    @SerializedName("completed_tasks")
    @Getter private ConcurrentList<String> completedTasks = Concurrent.newList();
    @SerializedName("highest_pet_score")
    @Getter private int highestPetScore;
    @SerializedName("mining_fiesta_ores_mined")
    @Getter private int miningFiestaOresMined;

    public int getLevel() {
        return (int) Math.floor(this.getExperience() / 100.0);
    }

}
