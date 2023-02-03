package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.SerializedPath;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.data.Range;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.StringUtil;
import dev.sbs.api.util.helper.WordUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CrimsonIsle {

    private ConcurrentLinkedMap<Integer, String> last_minibosses_killed = Concurrent.newLinkedMap();
    private ConcurrentMap<String, Integer> dojo = Concurrent.newMap();
    private Abiphone abiphone;

    // Factions
    @SerializedName("mages_reputation")
    @Getter private int mageReputation;
    @SerializedName("barbarians_reputation")
    @Getter private int barbarianReputation;
    @SerializedName("selected_faction")
    @Getter private Faction selectedFaction = Faction.NONE;

    // Kuudra
    private ConcurrentMap<String, Integer> kuudra_completed_tiers = Concurrent.newMap();
    @SerializedPath("kuudra_party_finder.search_settings")
    private Kuudra.SearchSettings kuudra_search_settings;
    @SerializedPath("kuudra_party_finder.group_builder")
    private Kuudra.GroupBuilder kuudra_group_builder;

    public Abiphone getAbiphone() {
        if (Objects.isNull(this.abiphone))
            this.abiphone = new Abiphone();

        return this.abiphone;
    }

    public Dojo getDojo() {
        return new Dojo(this.dojo);
    }

    public Kuudra getKuudra() {
        return new Kuudra(
            this.kuudra_completed_tiers,
            this.kuudra_search_settings,
            this.kuudra_group_builder
        );
    }

    public ConcurrentList<String> getLastMinibossesKilled() {
        return Concurrent.newUnmodifiableList(this.last_minibosses_killed.values());
    }

    public static class Abiphone {

        @SerializedPath("operator_chip.repaired_index")
        @Getter private int repairedOperatorRelays;
        @SerializedName("trio_contact_addons")
        @Getter private int trioContactAddons;
        @SerializedName("active_contacts")
        private ConcurrentMap<Integer, String> activeContacts = Concurrent.newMap();
        private ConcurrentMap<String, Integer> games = Concurrent.newMap();
        @SerializedName("contact_data")
        @Getter private ConcurrentMap<String, Contact> contacts = Concurrent.newMap();

        public ConcurrentList<String> getCollectedContacts() {
            return Concurrent.newUnmodifiableList(this.activeContacts.values());
        }

        public static class Contact {

            @SerializedName("talked_to")
            @Getter private boolean talkedTo;
            @SerializedName("completed_quest")
            @Getter private boolean questCompleted;
            @Getter private ConcurrentMap<String, Object> specific = Concurrent.newMap();
            @SerializedName("last_call")
            private SkyBlockDate.RealTime lastCall;

            public Optional<SkyBlockDate.RealTime> getLastCall() {
                return Optional.ofNullable(this.lastCall);
            }

        }

    }

    public static class Dojo {

        @Getter private final ConcurrentMap<Type, Integer> points;

        private Dojo(ConcurrentMap<String, Integer> dojo) {
            this.points = Concurrent.newUnmodifiableMap(
                dojo.stream()
                    .filter(entry -> !entry.getKey().contains("time_"))
                    .map(entry -> Pair.of(Type.of(entry.getKey().replace("dojo_points_", "")), entry.getValue()))
                    .collect(Concurrent.toMap())
            );
        }

        public Integer getPoints(@NotNull Type type) {
            return this.getPoints().get(type);
        }

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public enum Type {

            FORCE("mob_kb"),
            STAMINA("wall_jump"),
            MASTERY("archer"),
            DISCIPLINE("sword_swap"),
            SWIFTNESS("snake"),
            CONTROL("fireball"),
            TENACITY("lock_head");

            @Getter private final String internalName;

            public static Optional<Type> of(String name) {
                return Arrays.stream(values())
                    .filter(type -> type.name().equalsIgnoreCase(name) || type.getInternalName().equalsIgnoreCase(name))
                    .findFirst();
            }

        }

    }

    public enum Faction {

        NONE,
        @SerializedName("mages")
        MAGE,
        @SerializedName("barbarians")
        BARBARIAN

    }

    public static class Kuudra {

        @Getter private final ConcurrentMap<Tier, Integer> completedTiers;
        @Getter private final ConcurrentMap<Tier, Integer> highestWave;
        @Getter private final Kuudra.SearchSettings searchSettings;
        @Getter private final Kuudra.GroupBuilder groupBuilder;

        private Kuudra(ConcurrentMap<String, Integer> kuudraCompletedTiers, Kuudra.SearchSettings kuudraSearchSettings, Kuudra.GroupBuilder kuudraGroupBuilder) {
            this.searchSettings = (kuudraSearchSettings != null ? kuudraSearchSettings : new SearchSettings());
            this.groupBuilder = (kuudraGroupBuilder != null ? kuudraGroupBuilder : new GroupBuilder());

            this.completedTiers = Concurrent.newUnmodifiableMap(
                kuudraCompletedTiers.stream()
                    .filter(entry -> !entry.getKey().startsWith("highest_"))
                    .map(entry -> Pair.of(Tier.of(entry.getKey()), entry.getValue()))
                    .collect(Concurrent.toMap())
            );

            this.highestWave = Concurrent.newUnmodifiableMap(
                kuudraCompletedTiers.stream()
                    .filter(entry -> entry.getKey().startsWith("highest_"))
                    .map(entry -> Pair.of(Tier.of(entry.getKey()), entry.getValue()))
                    .collect(Concurrent.toMap())
            );
        }

        @RequiredArgsConstructor
        public enum Tier {

            BASIC("NONE"),
            HOT,
            BURNING,
            FIERY,
            INFERNAL;

            @Getter private final String internalName;

            Tier() {
                this.internalName = name();
            }

            public String getName() {
                return WordUtil.capitalizeFully(this.name());
            }

            public static Optional<Tier> of(String name) {
                return Arrays.stream(values())
                    .filter(tier -> tier.name().equalsIgnoreCase(name) || tier.getInternalName().equalsIgnoreCase(name))
                    .findFirst();
            }

        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class SearchSettings {

            @Getter private Tier tier = Tier.BASIC;
            @Getter private String search = "";
            @Getter private Sort sort = Sort.RECENTLY_CREATED;
            private String combat_level = "0-60";

            public Range<Integer> getCombatLevel() {
                if (Objects.isNull(this.combat_level))
                    return Range.between(0, 60);
                else {
                    String[] parts = StringUtil.split(this.combat_level, "-");
                    return Range.between(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                }
            }

            public enum Sort {

                RECENTLY_CREATED,
                HIGHEST_COMBAT_LEVEL,
                LARGEST_GROUP_SIZE

            }

        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class GroupBuilder {

            @Getter private Tier tier;
            @Getter private String note;
            @SerializedName("combat_level_required")
            @Getter private int requiredCombatLevel;

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Matriarch {

        @SerializedName("pearls_collected")
        @Getter private int lastCollectedPearls;
        @SerializedName("last_attempt")
        @Getter private SkyBlockDate.RealTime lastAttempt;
        @SerializedName("recent_refreshes")
        private ConcurrentMap<Integer, SkyBlockDate.RealTime> recentRefreshes = Concurrent.newMap();

        public ConcurrentList<SkyBlockDate.RealTime> getRecentRefreshes() {
            return Concurrent.newUnmodifiableList(this.recentRefreshes.values());
        }

    }

}
