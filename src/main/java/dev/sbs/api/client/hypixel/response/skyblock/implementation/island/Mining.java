package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.gson.SerializedPath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Mining {

    private ConcurrentMap<String, Object> nodes = Concurrent.newMap();
    private ConcurrentMap<String, Boolean> toggles = Concurrent.newMap();
    @SerializedName("last_reset")
    private long lastReset;
    @Getter private double experience;
    @SerializedName("received_free_tier")
    @Getter private boolean receivedFreeTier;
    @SerializedName("retroactive_tier2_token")
    @Getter private boolean retroactiveTier2Token;
    @SerializedName("tokens")
    @Getter private int remainingTokens;
    @SerializedName("tokens_spent")
    @Getter private int usedTokens;
    @SerializedName("selected_pickaxe_ability")
    @Getter private String selectedPickaxeAbility;
    @SerializedName("greater_mines_last_access")
    private long lastAccessToGreaterMines;
    @Getter private ConcurrentMap<Crystal.Type, Crystal> crystals = Concurrent.newMap();

    // Powder
    @SerializedName("powder_mithril")
    @Getter private int mithrilPowder;
    @SerializedName("powder_mithril_total")
    @Getter private int totalMithrilPowder;
    @SerializedName("powder_spent_mithril")
    @Getter private int usedMithrilPowder;
    @SerializedName("powder_gemstone")
    @Getter private int gemstonePowder;
    @SerializedName("powder_gemstone_total")
    @Getter private int totalGemstonePowder;
    @SerializedName("powder_spent_gemstone")
    @Getter private int usedGemstonePowder;

    // Daily Ores
    @SerializedName("daily_ores_mined")
    @Getter private int dailyOresMined;
    @SerializedName("daily_ores_mined_mithril_ore")
    @Getter private int dailyOresMinedMithrilOre;
    @SerializedName("daily_ores_mined_gemstone")
    @Getter private int dailyOresMinedGemstone;
    @SerializedName("daily_ores_mined_day")
    @Getter private int dailyOresMinedDay;
    @SerializedName("daily_ores_mined_day_mithril_ore")
    @Getter private int dailyOresMinedDayMithrilOre;
    @SerializedName("daily_ores_mined_day_gemstone")
    @Getter private int dailyOresMinedDayGemstone;

    // Biomes
    @SerializedPath("biomes.dwarven")
    @Getter private Biome.Dwarven dwarvenMinesBiome = new Biome.Dwarven();
    @SerializedPath("biomes.precursor")
    @Getter private Biome.Precursor precursorCityBiome = new Biome.Precursor();
    @SerializedPath("biomes.goblin")
    @Getter private Biome.Goblin goblinHideoutBiome = new Biome.Goblin();

    public Crystal getCrystal(Crystal.Type type) {
        return this.crystals.get(type);
    }

    public Optional<SkyBlockDate> getLastAccessToGreaterMines() {
        return Optional.ofNullable(this.lastAccessToGreaterMines > 0 ? new SkyBlockDate.RealTime(this.lastAccessToGreaterMines) : null);
    }

    public Optional<SkyBlockDate> getLastReset() {
        return Optional.ofNullable(this.lastReset > 0 ? new SkyBlockDate.RealTime(this.lastReset) : null);
    }

    public ConcurrentMap<String, Double> getNodes() {
        return this.nodes.stream().filter(entry -> !(entry.getValue() instanceof Boolean)).collect(Concurrent.toMap());
    }

    public ConcurrentMap<String, Boolean> getToggles() {
        if (this.toggles == null) {
            this.toggles = this.nodes.stream()
                .filter(entry -> (entry.getValue() instanceof Boolean))
                .map(entry -> Pair.of(entry.getKey().replace("toggle_", ""), (boolean) entry.getValue()))
                .collect(Concurrent.toMap());
        }

        return this.toggles;
    }

    public static class Biome {

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Dwarven extends Biome {

            @SerializedName("statues_placed")
            @Getter private ConcurrentList<Object> placedStatues = Concurrent.newList();

        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Precursor extends Biome {

            @SerializedName("parts_delivered")
            @Getter private ConcurrentList<Object> deliveredParts = Concurrent.newList();

        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Goblin extends Biome {

            @SerializedName("king_quest_active")
            @Getter private boolean kingQuestActive;
            @SerializedName("king_quests_completed")
            @Getter private int completedKingQuests;

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Crystal {

        private State state;
        @SerializedName("total_placed")
        @Getter private int totalPlaced;

        public State getState() {
            return this.state != null ? this.state : State.NOT_FOUND;
        }

        public enum State {

            FOUND,
            NOT_FOUND

        }

        public enum Type {

            @SerializedName("jade_crystal")
            JADE,
            @SerializedName("amber_crystal")
            AMBER,
            @SerializedName("topaz_crystal")
            TOPAZ,
            @SerializedName("sapphire_crystal")
            SAPHIRE,
            @SerializedName("amethyst_crystal")
            AMETHYST,
            @SerializedName("jasper_crystal")
            JASPER,
            @SerializedName("ruby_crystal")
            RUBY

        }

    }

}
