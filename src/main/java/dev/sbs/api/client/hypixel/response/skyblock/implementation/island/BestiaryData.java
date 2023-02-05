package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary.BestiaryModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_families.BestiaryFamilyModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_type_levels.BestiaryTypeLevelModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class BestiaryData {

    @Getter private final ConcurrentList<Bestiary> bestiaries;
    @Getter private final boolean migrated;

    BestiaryData(ConcurrentMap<String, Object> bestiaryMap) {
        this.migrated = (boolean) bestiaryMap.removeOrGet("migrated_stats", false);

        this.bestiaries = Concurrent.newUnmodifiableList(
            SimplifiedApi.getRepositoryOf(BestiaryModel.class)
            .findAll()
            .stream()
            .map(bestiaryModel -> {
                Pattern bestiaryPattern = buildPattern(bestiaryModel);

                ConcurrentMap<String, Double> groupMap = bestiaryMap.stream()
                    .filter(entry -> bestiaryPattern.matcher(entry.getKey()).matches())
                    .collect(Concurrent.toMap());

                Double familyKills = groupMap.removeOrGet(FormatUtil.format("kills_family_{0}", bestiaryModel.getKey().toLowerCase()), 0.0);
                Double familyDeaths = groupMap.removeOrGet(FormatUtil.format("deaths_family_{0}", bestiaryModel.getKey().toLowerCase()), 0.0);
                ConcurrentMap<Integer, Bestiary.Data> levelData = Concurrent.newMap();

                groupMap.forEach((key, value) -> {
                    Matcher matcher = bestiaryPattern.matcher(key);

                    if (matcher.matches()) {
                        boolean isKills = matcher.group(1).equals("kills");
                        int level = Integer.parseInt(matcher.group(2));

                        if (!levelData.containsKey(level))
                            levelData.put(level, new Bestiary.Data());

                        if (isKills)
                            levelData.get(level).setKills(value.intValue());
                        else
                            levelData.get(level).setDeaths(value.intValue());
                    }
                });

                return new Bestiary(
                    bestiaryModel,
                    familyKills.intValue(),
                    familyDeaths.intValue(),
                    levelData
                );
            })
            .collect(Concurrent.toList())
        );
    }

    public ConcurrentList<Bestiary> getBestiaries(@NotNull String bestiaryFamilyKey) {
        return this.getBestiaries().findAll(bestiaryModel -> bestiaryModel.getType().getFamily().getKey(), bestiaryFamilyKey.toUpperCase());
    }

    public ConcurrentList<Bestiary> getBestiaries(@NotNull BestiaryFamilyModel bestiaryFamilyModel) {
        return this.getBestiaries().findAll(bestiaryModel -> bestiaryModel.getType().getFamily(), bestiaryFamilyModel);
    }

    public Bestiary getBestiary(@NotNull String bestiaryKey) {
        return this.getBestiaries().findFirstOrNull(bestiary -> bestiary.getType().getKey(), bestiaryKey.toUpperCase());
    }

    public Bestiary getBestiary(@NotNull BestiaryModel bestiaryModel) {
        return this.getBestiaries().findFirstOrNull(Bestiary::getType, bestiaryModel);
    }

    public int getMilestone() {
        return this.getUnlocked() / 10;
    }

    public int getUnlocked() {
        return this.getBestiaries()
            .stream()
            .mapToInt(Bestiary::getLevel)
            .sum();
    }

    private static Pattern buildPattern(BestiaryModel bestiaryModel) {
        return Pattern.compile(FormatUtil.format("^(kills|deaths)(?:_family)?_{0}(?:_([\\d]+))?$", bestiaryModel.getKey().toLowerCase()));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Bestiary {

        @Getter private final BestiaryModel type;
        @Getter private final int kills;
        @Getter private final int deaths;
        @Getter private final ConcurrentMap<Integer, Data> levelData;

        public ConcurrentList<Integer> getLevelTiers() {
            return SimplifiedApi.getRepositoryOf(BestiaryTypeLevelModel.class)
                .findAll(BestiaryTypeLevelModel::getType, this.getType().getType())
                .stream()
                .map(BestiaryTypeLevelModel::getTotalKillsRequired)
                .collect(Concurrent.toList());
        }

        public int getLevel() {
            ConcurrentList<Integer> levelTiers = this.getLevelTiers();

            return Math.min(
                this.getType().getMaxLevel(),
                IntStream.range(0, levelTiers.size())
                    .filter(index -> levelTiers.get(index) > this.getKills())
                    .findFirst()
                    .orElse(0)
            );
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Data {

            @Setter(AccessLevel.PRIVATE)
            @Getter private int kills;
            @Setter(AccessLevel.PRIVATE)
            @Getter private int deaths;

        }

    }

}
