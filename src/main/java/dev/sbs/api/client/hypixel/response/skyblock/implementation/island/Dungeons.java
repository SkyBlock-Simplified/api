package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_classes.DungeonClassModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeons.DungeonModel;
import dev.sbs.api.minecraft.text.ChatFormat;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.gson.SerializedPath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Dungeons {

    @SerializedName("dungeons_blah_blah")
    private ConcurrentSet<String> dungeonsBlahBlah;
    @SerializedName("selected_dungeon_class")
    private String selectedClass;
    @SerializedPath("dungeon_journal.journal_entries")
    @Getter private ConcurrentMap<String, ConcurrentList<Integer>> journalEntries;
    @SerializedName("player_classes")
    private ConcurrentMap<String, Dungeon.Class> playerClasses = Concurrent.newMap();
    @SerializedName("dungeon_types")
    private ConcurrentMap<String, Dungeon> types = Concurrent.newMap();
    @SerializedName("daily_runs")
    @Getter private DailyRuns dailyRuns;
    @Getter private Treasures treasures;

    public Dungeon.Class getClass(DungeonClassModel dungeonClassModel) {
        Dungeon.Class dungeonClass = this.playerClasses.get(dungeonClassModel.getKey().toLowerCase());
        dungeonClass.type = dungeonClassModel;
        return dungeonClass;
    }

    public ConcurrentList<Dungeon.Class> getClasses() {
        return this.playerClasses.stream()
            .map(entry -> this.getClass(SimplifiedApi.getRepositoryOf(DungeonClassModel.class)
                .findFirstOrNull(DungeonClassModel::getKey, entry.getKey().toUpperCase()))
            )
            .collect(Concurrent.toList());
    }

    public Dungeon getDungeon(@NotNull DungeonModel dungeonModel) {
        return this.getDungeon(dungeonModel, false);
    }

    public Dungeon getDungeon(@NotNull DungeonModel dungeonModel, boolean masterMode) {
        Dungeon dungeon = this.types.getOrDefault(String.format("%s%s", (masterMode ? "master_" : ""), dungeonModel.getKey().toLowerCase()), new Dungeon());
        dungeon.type = dungeonModel;
        dungeon.masterMode = masterMode;
        return dungeon;
    }

    public Optional<Dungeon.Class> getSelectedClass() {
        return this.getSelectedClassModel().map(this::getClass);
    }

    public Optional<DungeonClassModel> getSelectedClassModel() {
        return SimplifiedApi.getRepositoryOf(DungeonClassModel.class).findFirst(DungeonClassModel::getKey, this.selectedClass.toUpperCase());
    }

    @Getter
    public static class DailyRuns {

        @SerializedName("current_day_stamp")
        private int currentDayStamp;
        @SerializedName("completed_runs_count")
        private int completedRuns;

    }

    @Getter
    public static class Treasures {

        private ConcurrentList<Run> runs = Concurrent.newList();
        private ConcurrentList<Chest> chests = Concurrent.newList();

        public static class Run {

            @SerializedName("run_id")
            @Getter private UUID runId;
            @SerializedName("completion_ts")
            @Getter private SkyBlockDate.RealTime completionTime;
            private String dungeon_type;
            @SerializedName("dungeon_tier")
            @Getter private int tier;
            @Getter private ConcurrentList<Participant> participants = Concurrent.newList();

            public DungeonModel getType() {
                return SimplifiedApi.getRepositoryOf(DungeonModel.class).findFirstOrNull(DungeonModel::getKey, this.dungeon_type.toUpperCase());
            }

            @Getter
            public static class Participant {

                private static final Pattern DISPLAY_PATTERN = Pattern.compile(String.format(
                    "^%s([0-9a-f])(.*?)%<s[0-9a-f]: %<s[0-9a-f](.*?)%<s[0-9a-f] \\(%<s[0-9a-f]([0-9]+)%<s[0-9a-f]\\)",
                    ChatFormat.SECTION_SYMBOL
                ));

                @SerializedName("player_uuid")
                private UUID playerId;
                @SerializedName("display_name")
                private String displayName;
                @SerializedName("class_milestone")
                private int milestone;

                public int getClassLevel() {
                    return Integer.parseInt(DISPLAY_PATTERN.matcher(this.getDisplayName()).group(4));
                }

                public DungeonClassModel getDungeonClass() {
                    return SimplifiedApi.getRepositoryOf(DungeonClassModel.class).findFirstOrNull(DungeonClassModel::getKey, DISPLAY_PATTERN.matcher(this.getDisplayName()).group(3).toUpperCase());
                }

                public String getName() {
                    return DISPLAY_PATTERN.matcher(this.getDisplayName()).group(2);
                }

            }

        }

        @Getter
        public static class Chest {

            @SerializedName("run_id")
            private UUID runId;
            @SerializedName("chest_id")
            private UUID chestId;
            @SerializedName("treasure_type")
            private Type type;
            private int quality;
            @SerializedName("shiny_eligible")
            private boolean shinyEligible;
            private boolean paid;
            private int rerolls;
            @SerializedPath("rewards.rolled_rng_meter_randomly")
            private boolean rolledRngMeterRandomly;
            @SerializedPath("rewards.rewards")
            private ConcurrentList<String> items = Concurrent.newList();

            public enum Type {

                WOOD,
                GOLD,
                DIAMOND,
                EMERALD,
                OBSIDIAN,
                BEDROCK

            }

        }

    }

}
