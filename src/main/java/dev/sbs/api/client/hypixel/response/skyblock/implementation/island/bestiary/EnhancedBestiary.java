package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.bestiary;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary.BestiaryModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_brackets.BestiaryBracketModel;
import dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_families.BestiaryFamilyModel;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.stream.IntStream;

@Getter
public class EnhancedBestiary extends Bestiary {

    private final @NotNull ConcurrentList<Mob> mobs;

    EnhancedBestiary(@NotNull Bestiary bestiary) {
        // Re-initialize Fields
        super(
            bestiary.getKills(),
            bestiary.getDeaths(),
            bestiary.getLastClaimedMilestone()
        );

        this.mobs = SimplifiedApi.getRepositoryOf(BestiaryFamilyModel.class)
            .stream()
            .flatMap(familyModel -> SimplifiedApi.getRepositoryOf(BestiaryModel.class)
                .findAll(BestiaryModel::getFamily, familyModel)
                .stream()
                .map(bestiaryModel -> new Mob(
                    bestiaryModel,
                    this.getKills().getOrDefault(buildPattern(bestiaryModel), 0),
                    this.getDeaths().getOrDefault(buildPattern(bestiaryModel), 0)
                ))
            )
            .collect(Concurrent.toUnmodifiableList());
    }

    public int getMilestone() {
        return this.getUnlocked() / 10;
    }

    public int getUnlocked() {
        return this.getMobs()
            .stream()
            .mapToInt(Mob::getLevel)
            .sum();
    }

    private static @NotNull String buildPattern(@NotNull BestiaryModel bestiaryModel) {
        return String.format("%s_%s", bestiaryModel.getKey().toLowerCase(), bestiaryModel.getLevel());
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    @RequiredArgsConstructor
    public static class Mob {

        private final @NotNull BestiaryModel type;
        private final int kills;
        private final int deaths;
        private final @NotNull ConcurrentList<Integer> tiers;

        public Mob(@NotNull BestiaryModel type, int kills, int deaths) {
            this.type = type;
            this.kills = kills;
            this.deaths = deaths;
            this.tiers = SimplifiedApi.getRepositoryOf(BestiaryBracketModel.class)
                .findAll(BestiaryBracketModel::getBracket, this.getBracket().getBracket())
                .stream()
                .map(BestiaryBracketModel::getTotalKillsRequired)
                .collect(Concurrent.toUnmodifiableList());
        }

        public @NotNull BestiaryBracketModel getBracket() {
            return this.getFamily().getBracket();
        }

        public @NotNull BestiaryFamilyModel getFamily() {
            return this.getType().getFamily();
        }

        public int getLevel() {
            return Math.min(
                this.getMaxLevel(),
                IntStream.range(0, this.getTiers().size())
                    .filter(index -> this.getTiers().get(index) > this.getKills())
                    .findFirst()
                    .orElse(0)
            );
        }

        public int getMaxLevel() {
            return this.getBracket().getTier();
        }

    }

}
