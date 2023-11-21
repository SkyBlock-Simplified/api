package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.dungeon;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.Experience;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.weight.Weight;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.weight.Weighted;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_levels.DungeonLevelModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeons.DungeonModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.NumberUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class EnhancedDungeon extends Dungeon implements Experience, Weighted {

    private final @NotNull DungeonModel typeModel;
    private final @NotNull ConcurrentList<Double> experienceTiers;

    EnhancedDungeon(@NotNull Dungeon dungeon) {
        // Re-initialize Fields
        super.type = dungeon.getType();
        super.experience = dungeon.experience;
        super.highestCompletedTier = dungeon.highestCompletedTier;
        super.bestRuns = dungeon.bestRuns;
        super.timesPlayed = dungeon.timesPlayed;
        super.completions = dungeon.completions;
        super.milestoneCompletions = dungeon.milestoneCompletions;
        super.bestScore = dungeon.bestScore;
        super.watcherKills = dungeon.watcherKills;
        super.mobsKilled = dungeon.mobsKilled;
        super.mostMobsKilled = dungeon.mostMobsKilled;
        super.mostHealing = dungeon.mostHealing;
        super.mostDamageHealer = dungeon.mostDamageHealer;
        super.mostDamageMage = dungeon.mostDamageMage;
        super.mostDamageBerserk = dungeon.mostDamageBerserk;
        super.mostDamageArcher = dungeon.mostDamageArcher;
        super.mostDamageTank = dungeon.mostDamageTank;
        super.fastestTime = dungeon.fastestTime;
        super.fastestSTierTime = dungeon.fastestSTierTime;
        super.fastestSPlusTierTime = dungeon.fastestSPlusTierTime;

        this.typeModel = this.getType().getModel();
        this.experienceTiers = SimplifiedApi.getRepositoryOf(DungeonLevelModel.class)
            .stream()
            .map(DungeonLevelModel::getTotalExpRequired)
            .collect(Concurrent.toList());
    }

    @Override
    public int getMaxLevel() {
        return this.getExperienceTiers().size();
    }

    @Override
    public @NotNull Weight getWeight() {
        double rawLevel = this.getRawLevel();
        ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
        double maxDungeonClassExperienceRequired = experienceTiers.get(experienceTiers.size() - 1);

        if (rawLevel < this.getMaxLevel())
            rawLevel += (this.getProgressPercentage() / 100); // Add Percentage Progress to Next Level

        double base = Math.pow(rawLevel, 4.5) * this.getTypeModel().getWeightMultiplier();
        double weightValue = NumberUtil.round(base, 2);
        double weightOverflow = 0;

        if (this.getExperience() > maxDungeonClassExperienceRequired) {
            double overflow = Math.pow((this.getExperience() - maxDungeonClassExperienceRequired) / (4 * maxDungeonClassExperienceRequired / base), 0.968);
            weightOverflow = NumberUtil.round(overflow, 2);
        }

        return Weight.of(weightValue, weightOverflow);
    }

}