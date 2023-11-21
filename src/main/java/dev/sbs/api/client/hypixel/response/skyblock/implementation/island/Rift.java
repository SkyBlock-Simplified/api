package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.pet.Pet;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.NbtContent;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.gson.SerializedPath;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Rift {

    // Boundaries
    @SerializedName("village_plaza")
    private VillagePlaza villagePlaza;
    @SerializedName("wither_cage")
    private WitherCage witherCage;
    @SerializedName("black_lagoon")
    private BlackLagoon blackLagoon;
    @SerializedName("dead_cats")
    private DeadCats deadCats;
    @SerializedName("wizard_tower")
    private WizardTower wizardTower;
    @SerializedName("west_village")
    private WestVillage westVillage;
    private Enigma enigma;
    @SerializedName("wyld_woods")
    private WyldWoods wyldWoods;
    private Gallery gallery;
    private Castle castle;
    private Access access;
    @SerializedName("slayer_quest")
    private SlayerQuest slayerQuest;
    private Dreadfarm dreadfarm;

    @SerializedName("lifetime_purchased_boundaries")
    private @NotNull ConcurrentList<String> lifetimePurchasedBoundaries = Concurrent.newList();

    // Inventories
    @SerializedPath("inventory.inv_contents")
    private NbtContent inventory;
    @SerializedPath("inventory.inv_armor")
    private NbtContent armor;
    @SerializedPath("inventory.ender_chest_contents")
    private NbtContent enderChest;
    @SerializedPath("inventory.equipment_contents")
    private NbtContent equipment;


    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class VillagePlaza {

        @Accessors(fluent = true)
        @SerializedName("got_scammed")
        private boolean gotScammed;
        private Murder murder;
        @SerializedName("barry_center")
        private BarryCenter barryCenter;
        private Cowboy cowboy;
        private Lonely lonely;
        private Seraphine seraphine;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Murder {

            @SerializedName("step_index")
            private int stepIndex;
            @SerializedName("room_clues")
            private @NotNull ConcurrentList<String> roomClues = Concurrent.newList();

        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class BarryCenter {

            @SerializedName("first_talk_to_barry")
            private boolean firstTalkToBarry;
            @SerializedName("received_reward")
            private boolean receivedReward;
            private @NotNull ConcurrentList<String> convinced = Concurrent.newList();

        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Cowboy {

            private int stage;
            @SerializedName("hay_eaten")
            private int hayEaten;
            @SerializedName("rabbit_name")
            private String rabbitName;
            @SerializedName("exported_carrots")
            private int exportedCarrots;

        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Lonely {

            @SerializedName("seconds_sitting")
            private int secondsSitting;

        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Seraphine {

            @SerializedName("step_index")
            private int stepIndex;

        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WitherCage {

        @SerializedName("killed_eyes")
        private @NotNull ConcurrentList<String> killedEyes = Concurrent.newList();

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BlackLagoon {

        @Accessors(fluent = true)
        @SerializedName("talked_to_edwin")
        private boolean hasTalkedToEdwin;
        @Accessors(fluent = true)
        @SerializedName("received_science_paper")
        private boolean hasReceivedSciencePaper;
        @Accessors(fluent = true)
        @SerializedName("delivered_science_paper")
        private boolean hasDeliveredSciencePaper;
        @SerializedName("completed_step")
        private int completedStep;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DeadCats {

        @Accessors(fluent = true)
        @SerializedName("talked_to_jacquelle")
        private boolean hasTalkedToJacquelle;
        @Accessors(fluent = true)
        @SerializedName("picked_up_detector")
        private boolean hasPickedUpDetector;
        @SerializedName("found_cats")
        private @NotNull ConcurrentList<String> foundCats = Concurrent.newList();
        @Accessors(fluent = true)
        @SerializedName("unlocked_pet")
        private boolean hasUnlockedPet;
        private Pet montezuma;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WizardTower {

        @SerializedName("wizard_quest_step")
        private int wizardQuestStep;
        @SerializedName("crumbs_laid_out")
        private int crumbsLaidOut;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WestVillage {

        @SerializedName("crazy_kloon")
        private CrazyKloon crazyKloon;
        private Mirrorverse mirrorverse;
        @SerializedName("kat_house")
        private KatHouse katHouse;
        private Glyphs glyphs;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class CrazyKloon {

            @SerializedName("selected_colors")
            private @NotNull ConcurrentMap<String, String> selectedColors = Concurrent.newMap();
            @Accessors(fluent = true)
            @SerializedName("talked")
            private boolean hasTalked;
            @SerializedName("hacked_terminals")
            private @NotNull ConcurrentList<String> hackedTerminals = Concurrent.newList();
            @SerializedName("quest_complete")
            private boolean questComplete;

        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Mirrorverse {

            @SerializedName("visited_rooms")
            private @NotNull ConcurrentList<String> visitedRooms = Concurrent.newList();
            @SerializedName("upside_down_hard")
            private boolean upsideDownHardCompleted;
            @SerializedName("claimed_chest_items")
            private @NotNull ConcurrentList<String> claimedChestItems = Concurrent.newList();
            @SerializedName("claimed_reward")
            private boolean claimedReward;

        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class KatHouse {

            @SerializedName("bin_collected_mosquito")
            private int collectedMosquito;
            @SerializedName("bin_collected_spider")
            private int collectedSpider;
            @SerializedName("bin_collected_silverfish")
            private int collectedSilverfish;

        }

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Glyphs {

            @SerializedName("claimed_wand")
            private boolean claimedWand;
            @SerializedName("current_glyph_delivered")
            private boolean currentGlyphDelivered;
            @SerializedName("current_glyph_completed")
            private boolean currentGlyphCompleted;
            @SerializedName("current_glyph")
            private int currentGlyph;
            private boolean completed;
            @SerializedName("claimed_bracelet")
            private boolean claimedBracelet;

        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Enigma {

        @Accessors(fluent = true)
        @SerializedName("bought_cloak")
        private boolean hasBoughtCloak;
        @SerializedName("found_souls")
        private @NotNull ConcurrentList<String> foundSouls = Concurrent.newList();
        @SerializedName("claimed_bonus_index")
        private int claimedBonusIndex;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WyldWoods {

        @SerializedName("talked_threebrothers")
        private @NotNull ConcurrentList<String> talkedThreebrothers = Concurrent.newList();
        @SerializedName("bughunter_step")
        private int bughunterStep;
        @Accessors(fluent = true)
        @SerializedName("sirius_started_q_a")
        private boolean hasStartedSiriusQA;
        @SerializedName("sirius_q_a_chain_done")
        private boolean siriusQAChainDone;
        @Accessors(fluent = true)
        @SerializedName("sirius_completed_q_a")
        private boolean hasCompletedSiriusQA;
        @Accessors(fluent = true)
        @SerializedName("sirius_claimed_doubloon")
        private boolean hasClaimedSiriusDoubloon;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Gallery {

        @SerializedName("elise_step")
        private int eliseStep;
        @SerializedName("secured_trophies")
        private @NotNull ConcurrentList<Trophy> securedTrophies = Concurrent.newList();

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Trophy {

            private String type;
            private Instant timestamp;
            private int visits;

        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Castle {

        @Accessors(fluent = true)
        @SerializedName("unlocked_pathway_skip")
        private boolean hasUnlockedPathwaySkip;
        @SerializedName("fairy_step")
        private int fairyStep;
        @SerializedName("grubber_stacks")
        private int grubberStacks;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Access {

        @SerializedName("last_free")
        private long lastFree;
        @SerializedName("consumed_prism")
        private boolean consumedPrism;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SlayerQuest extends Slayer.Quest {

        @SerializedName("combat_xp")
        private int combatXP;
        @SerializedName("recent_mob_kills")
        private @NotNull ConcurrentList<MobKill> recentMobKills = Concurrent.newList();
        @SerializedName("last_killed_mob_island")
        private String lastKilledMobIsland;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class MobKill {

            private int xp;
            private Instant timestamp;

        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Dreadfarm {

        @SerializedName("shania_stage")
        private int shaniaStage;
        @SerializedName("caducous_feeder_uses")
        private @NotNull ConcurrentList<Instant> caducousFeederUses = Concurrent.newList();

    }

}
