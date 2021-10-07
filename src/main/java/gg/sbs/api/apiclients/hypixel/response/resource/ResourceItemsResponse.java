package gg.sbs.api.apiclients.hypixel.response.resource;

import com.google.gson.annotations.SerializedName;
import gg.sbs.api.util.concurrent.ConcurrentList;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceItemsResponse {

    @Getter private boolean success;
    @Getter private long lastUpdated;
    @Getter private ConcurrentList<Item> items;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Item {

        @Getter private String material;
        @Getter private int durability;
        @Getter private String skin;
        @Getter private String name;
        @Getter private String furniture;
        @Getter private String tier;
        @Getter private String id;
        @Getter private String generator;
        @Getter private int generatorTier;
        @Getter private boolean glowing;
        @Getter private String category;
        @Getter private ConcurrentMap<String, Double> stats;
        @SerializedName("npc_sell_price")
        @Getter private double npcSellPrice;
        @Getter private boolean unstackable;
        @Getter private boolean dungeonItem;
        @Getter private String color;
        @SerializedName("tiered_stats")
        @Getter private ConcurrentMap<String, ConcurrentList<Integer>> tieredStats;
        @SerializedName("gear_score")
        @Getter private int gearScore;
        @Getter private ItemRequirements requirements;
        @SerializedName("catacombs_requirements")
        @Getter private ItemCatacombsRequirements catacombsRequirements;
        @Getter private ItemEssence essence;
        @Getter private String description;
        @SerializedName("ability_damage_scaling")
        @Getter private double abilityDamageScaling;
        @Getter private ConcurrentMap<String, Integer> enchantments;
        @Getter private String crystal;
        @SerializedName("private_island")
        @Getter private String privateIsland;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ItemRequirements {

        @SerializedName("dungeon_completion")
        @Getter private TypeTierRequirement dungeonCompletion;
        @Getter private TypeLevelRequirement skill;
        @Getter private SlayerLevelRequirement slayer;
        @SerializedName("heart_of_the_mountain")
        @Getter private TierRequirement heartOfTheMountain;
        @SerializedName("target_practice_requirement")
        @Getter private TargetPracticeRequirement targetPracticeRequirement;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ItemCatacombsRequirements {

        @Getter private TypeLevelRequirement dungeon;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TypeLevelRequirement {

        @Getter private String type;
        @Getter private int level;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TypeTierRequirement {

        @Getter private String type;
        @Getter private int tier;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SlayerLevelRequirement {

        @SerializedName("slayer_boss_type")
        @Getter private String slayerBossType;
        @Getter private int level;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TierRequirement {

        @Getter private int tier;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TargetPracticeRequirement {

        @Getter private String mode;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ItemEssence {

        @SerializedName("essence_type")
        @Getter private String essenceType;
        @Getter private ConcurrentList<Integer> costs;

    }

}