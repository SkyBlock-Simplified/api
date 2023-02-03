package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_classes.DungeonClassModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_floors.DungeonFloorModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_levels.DungeonLevelModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeons.DungeonModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.NumberUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Dungeon extends Experience implements Weighted {

    @Getter DungeonModel type;
    @Getter boolean masterMode;
    @Getter private double experience;
    @SerializedName("highest_tier_completed")
    @Getter private int highestCompletedTier;
    @SerializedName("best_runs")
    @Getter private ConcurrentMap<Integer, ConcurrentList<BestRun>> bestRuns = Concurrent.newMap();

    @SerializedName("times_played")
    @Getter private ConcurrentMap<Integer, Integer> timesPlayed = Concurrent.newMap();
    @SerializedName("tier_completions")
    @Getter private ConcurrentMap<Integer, Integer> completions = Concurrent.newMap();
    @SerializedName("milestone_completions")
    @Getter private ConcurrentMap<Integer, Integer> milestoneCompletions = Concurrent.newMap();

    @SerializedName("best_score")
    @Getter private ConcurrentMap<Integer, Integer> bestScore = Concurrent.newMap();
    @SerializedName("watcher_kills")
    @Getter private ConcurrentMap<Integer, Integer> watcherKills = Concurrent.newMap();
    @SerializedName("mobs_killed")
    @Getter private ConcurrentMap<Integer, Integer> mobsKilled = Concurrent.newMap();
    @SerializedName("most_mobs_killed")
    @Getter private ConcurrentMap<Integer, Integer> mostMobsKilled = Concurrent.newMap();
    @SerializedName("most_healing")
    @Getter private ConcurrentMap<Integer, Double> mostHealing = Concurrent.newMap();

    // Class Damage
    @SerializedName("most_damage_healer")
    @Getter private ConcurrentMap<Integer, Double> mostDamageHealer = Concurrent.newMap();
    @SerializedName("most_damage_mage")
    @Getter private ConcurrentMap<Integer, Double> mostDamageMage = Concurrent.newMap();
    @SerializedName("most_damage_berserk")
    @Getter private ConcurrentMap<Integer, Double> mostDamageBerserk = Concurrent.newMap();
    @SerializedName("most_damage_archer")
    @Getter private ConcurrentMap<Integer, Double> mostDamageArcher = Concurrent.newMap();
    @SerializedName("most_damage_tank")
    @Getter private ConcurrentMap<Integer, Double> mostDamageTank = Concurrent.newMap();

    // Fastest Times
    @SerializedName("fastest_time")
    @Getter private ConcurrentMap<Integer, Integer> fastestTime = Concurrent.newMap();
    @SerializedName("fastest_time_s")
    @Getter private ConcurrentMap<Integer, Integer> fastestSTierTime = Concurrent.newMap();
    @SerializedName("fastest_time_s_plus")
    @Getter private ConcurrentMap<Integer, Integer> fastestSPlusTierTime = Concurrent.newMap();

    public ConcurrentList<BestRun> getBestRuns(DungeonFloorModel dungeonFloorModel) {
        return this.getBestRuns().get(dungeonFloorModel.getFloor());
    }

    public int getBestScore(DungeonFloorModel dungeonFloorModel) {
        return this.getBestScore().getOrDefault(dungeonFloorModel.getFloor(), 0);
    }

    public int getCompletions(DungeonFloorModel dungeonFloorModel) {
        return this.getCompletions().getOrDefault(dungeonFloorModel.getFloor(), 0);
    }

    public int getFastestTime(DungeonFloorModel dungeonFloorModel) {
        return this.getFastestTime().getOrDefault(dungeonFloorModel.getFloor(), 0);
    }

    public int getFastestSTierTime(DungeonFloorModel dungeonFloorModel) {
        return this.getFastestSTierTime().getOrDefault(dungeonFloorModel.getFloor(), 0);
    }

    public int getFastestSPlusTierTime(DungeonFloorModel dungeonFloorModel) {
        return this.getFastestSPlusTierTime().getOrDefault(dungeonFloorModel.getFloor(), 0);
    }

    public int getMilestoneCompletions(DungeonFloorModel dungeonFloorModel) {
        return this.getMilestoneCompletions().getOrDefault(dungeonFloorModel.getFloor(), 0);
    }

    public ConcurrentMap<DungeonFloorModel, Double> getMostDamage(DungeonClassModel dungeonClassModel) {
        switch (dungeonClassModel.getKey()) {
            case "HEALER":
                return this.getMostDamageHealer()
                    .stream()
                    .map(entry -> Pair.of(
                        SimplifiedApi.getRepositoryOf(DungeonFloorModel.class).findFirstOrNull(DungeonFloorModel::getFloor, entry.getKey()),
                        entry.getValue()
                    ))
                    .collect(Concurrent.toMap());
            case "MAGE":
                return this.getMostDamageMage()
                    .stream()
                    .map(entry -> Pair.of(
                        SimplifiedApi.getRepositoryOf(DungeonFloorModel.class).findFirstOrNull(DungeonFloorModel::getFloor, entry.getKey()),
                        entry.getValue()
                    ))
                    .collect(Concurrent.toMap());
            case "BERSERK":
                return this.getMostDamageBerserk()
                    .stream()
                    .map(entry -> Pair.of(
                        SimplifiedApi.getRepositoryOf(DungeonFloorModel.class).findFirstOrNull(DungeonFloorModel::getFloor, entry.getKey()),
                        entry.getValue()
                    ))
                    .collect(Concurrent.toMap());
            case "ARCHER":
                return this.getMostDamageArcher()
                    .stream()
                    .map(entry -> Pair.of(
                        SimplifiedApi.getRepositoryOf(DungeonFloorModel.class).findFirstOrNull(DungeonFloorModel::getFloor, entry.getKey()),
                        entry.getValue()
                    ))
                    .collect(Concurrent.toMap());
            case "TANK":
                return this.getMostDamageTank()
                    .stream()
                    .map(entry -> Pair.of(
                        SimplifiedApi.getRepositoryOf(DungeonFloorModel.class).findFirstOrNull(DungeonFloorModel::getFloor, entry.getKey()),
                        entry.getValue()
                    ))
                    .collect(Concurrent.toMap());
            default:
                return null;
        }
    }

    public double getMostDamage(DungeonClassModel dungeonClassModel, DungeonFloorModel dungeonFloorModel) {
        return this.getMostDamage(dungeonClassModel).get(dungeonFloorModel);
    }

    public int getTimesPlayed(DungeonFloorModel dungeonFloorModel) {
        return this.getTimesPlayed().get(dungeonFloorModel.getFloor());
    }

    public int getWatcherKills(DungeonFloorModel dungeonFloorModel) {
        return this.getWatcherKills().get(dungeonFloorModel.getFloor());
    }

    public int getMobsKilled(DungeonFloorModel dungeonFloorModel) {
        return this.getMobsKilled().get(dungeonFloorModel.getFloor());
    }

    public int getMostMobsKilled(DungeonFloorModel dungeonFloorModel) {
        return this.getMostMobsKilled().get(dungeonFloorModel.getFloor());
    }

    public double getMostHealing(DungeonFloorModel dungeonFloorModel) {
        return this.getMostHealing().get(dungeonFloorModel.getFloor());
    }

    @Override
    public ConcurrentList<Double> getExperienceTiers() {
        return SimplifiedApi.getRepositoryOf(DungeonLevelModel.class)
            .stream()
            .map(DungeonLevelModel::getTotalExpRequired)
            .collect(Concurrent.toList());
    }

    @Override
    public int getMaxLevel() {
        return this.getExperienceTiers().size();
    }

    @Override
    public Weight getWeight() {
        double rawLevel = this.getRawLevel();
        ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
        double maxDungeonClassExperienceRequired = experienceTiers.get(experienceTiers.size() - 1);

        if (rawLevel < this.getMaxLevel())
            rawLevel += (this.getProgressPercentage() / 100); // Add Percentage Progress to Next Level

        double base = Math.pow(rawLevel, 4.5) * this.getType().getWeightMultiplier();
        double weightValue = NumberUtil.round(base, 2);
        double weightOverflow = 0;

        if (this.getExperience() > maxDungeonClassExperienceRequired) {
            double overflow = Math.pow((this.getExperience() - maxDungeonClassExperienceRequired) / (4 * maxDungeonClassExperienceRequired / base), 0.968);
            weightOverflow = NumberUtil.round(overflow, 2);
        }

        return new Weight(weightValue, weightOverflow);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Class extends Experience implements Weighted {

        @Getter DungeonClassModel type;
        @Getter private double experience;

        @Override
        public ConcurrentList<Double> getExperienceTiers() {
            return SimplifiedApi.getRepositoryOf(DungeonLevelModel.class)
                .stream()
                .map(DungeonLevelModel::getTotalExpRequired)
                .collect(Concurrent.toList());
        }

        @Override
        public int getMaxLevel() {
            return this.getExperienceTiers().size();
        }

        @Override
        public Weight getWeight() {
            double rawLevel = this.getRawLevel();
            ConcurrentList<Double> experienceTiers = this.getExperienceTiers();
            double maxDungeonClassExperienceRequired = experienceTiers.get(experienceTiers.size() - 1);

            if (rawLevel < this.getMaxLevel())
                rawLevel += (this.getProgressPercentage() / 100); // Add Percentage Progress to Next Level

            double base = Math.pow(rawLevel, 4.5) * this.getType().getWeightMultiplier();
            double weightValue = NumberUtil.round(base, 2);
            double weightOverflow = 0;

            if (this.getExperience() > maxDungeonClassExperienceRequired) {
                double overflow = Math.pow((this.getExperience() - maxDungeonClassExperienceRequired) / (4 * maxDungeonClassExperienceRequired / base), 0.968);
                weightOverflow = NumberUtil.round(overflow, 2);
            }

            return new Weight(weightValue, weightOverflow);
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BestRun {

        // Time
        @Getter private SkyBlockDate.RealTime timestamp;
        @SerializedName("elapsed_time")
        @Getter private int elapsedTime;

        // Score
        @SerializedName("score_exploration")
        @Getter private int explorationScore;
        @SerializedName("score_speed")
        @Getter private int speedScore;
        @SerializedName("score_skill")
        @Getter private int skillScore;
        @SerializedName("score_bonus")
        @Getter private int bonusScore;

        // Damage
        @SerializedName("damage_dealt")
        @Getter private double damageDealt;
        @SerializedName("damage_mitigated")
        @Getter private double damageMitigated;
        @SerializedName("ally_healing")
        @Getter private double allyHealing;

        @SerializedName("dungeon_class")
        private String dungeonClass;
        @Getter private ConcurrentList<UUID> teammates;
        @SerializedName("deaths")
        @Getter private int deaths;
        @SerializedName("mobs_killed")
        @Getter private int mobsKilled;
        @SerializedName("secrets_found")
        @Getter private int secretsFound;

        public DungeonClassModel getDungeonClass() {
            return SimplifiedApi.getRepositoryOf(DungeonClassModel.class).findFirstOrNull(DungeonClassModel::getKey, this.dungeonClass.toUpperCase());
        }

    }

}
