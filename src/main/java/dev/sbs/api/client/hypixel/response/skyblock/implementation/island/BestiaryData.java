package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary.BestiaryModel;
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

    @Getter private final ConcurrentList<Family> families;
    @Getter private final boolean migrated;

    BestiaryData(ConcurrentMap<String, Object> bestiaryMap) {
        this.migrated = (boolean) bestiaryMap.removeOrGet("migrated_stats", false);

        this.families = SimplifiedApi.getRepositoryOf(BestiaryModel.class)
            .findAll()
            .stream()
            .map(bestiaryModel -> {
                Pattern bestiaryPattern = buildPattern(bestiaryModel);

                ConcurrentMap<String, Double> familyMap = bestiaryMap.stream()
                    .filter(entry -> bestiaryPattern.matcher(entry.getKey()).matches())
                    .collect(Concurrent.toMap());

                Double familyKills = familyMap.removeOrGet(FormatUtil.format("kills_family_{0}", bestiaryModel.getKey().toLowerCase()), 0.0);
                Double familyDeaths = familyMap.removeOrGet(FormatUtil.format("deaths_family_{0}", bestiaryModel.getKey().toLowerCase()), 0.0);
                ConcurrentMap<Integer, Family.Data> levelData = Concurrent.newMap();

                familyMap.forEach((key, value) -> {
                    Matcher matcher = bestiaryPattern.matcher(key);

                    if (matcher.matches()) {
                        boolean isKills = matcher.group(1).equals("kills");
                        int level = Integer.parseInt(matcher.group(2));

                        if (!levelData.containsKey(level))
                            levelData.put(level, new Family.Data());

                        if (isKills)
                            levelData.get(level).setKills(value.intValue());
                        else
                            levelData.get(level).setDeaths(value.intValue());
                    }
                });

                return new Family(
                    bestiaryModel,
                    familyKills.intValue(),
                    familyDeaths.intValue(),
                    levelData
                );
            })
            .collect(Concurrent.toList());
    }

    public Family getFamily(@NotNull String bestiaryKey) {
        return this.getFamilies().findFirstOrNull(family -> family.getType().getKey(), bestiaryKey);
    }

    public Family getFamily(@NotNull BestiaryModel bestiaryModel) {
        return this.getFamilies().findFirstOrNull(Family::getType, bestiaryModel);
    }

    public int getMilestone() {
        return this.getUnlocked() / 10;
    }

    public int getUnlocked() {
        return this.getFamilies()
            .stream()
            .mapToInt(Family::getLevel)
            .sum();
    }

    private static Pattern buildPattern(BestiaryModel bestiaryModel) {
        return Pattern.compile(FormatUtil.format("^(kills|deaths)(?:_family)?_{0}(?:_([\\d]+))?$", bestiaryModel.getKey().toLowerCase()));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Family {

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
