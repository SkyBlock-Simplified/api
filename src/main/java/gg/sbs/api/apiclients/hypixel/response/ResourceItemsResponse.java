package gg.sbs.api.apiclients.hypixel.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class ResourceItemsResponse {
    @Getter
    private boolean success;

    @Getter
    private long lastUpdated;

    @Getter
    private List<ResourceItem> items;

    public static class ResourceItem {
        @Getter
        private String material;

        @Getter
        private int durability;

        @Getter
        private String skin;

        @Getter
        private String name;

        @Getter
        private String furniture;

        @Getter
        private String tier;

        @Getter
        private String id;

        @Getter
        private String generator;

        @Getter
        private int generatorTier;

        @Getter
        private boolean glowing;

        @Getter
        private String category;

        @Getter
        private Map<String, Integer> stats;

        @Getter
        @SerializedName("npc_sell_price")
        private int npcSellPrice;

        @Getter
        private boolean unstackable;

        @Getter
        private boolean dungeonItem;

        @Getter
        private String color;

        @Getter
        @SerializedName("tiered_stats")
        private Map<String, List<Integer>> tieredStats;

        @Getter
        @SerializedName("gear_score")
        private int gearScore;

        @Getter
        private ItemRequirements requirements;

        @Getter
        @SerializedName("catacombs_requirements")
        private ItemCatacombsRequirements catacombsRequirements;

        @Getter
        private ItemEssence essence;
    }

    public static class ItemRequirements {
        @Getter
        @SerializedName("dungeon_completion")
        private TypeTierRequirement dungeonCompletion;

        @Getter
        private TypeLevelRequirement skill;

        @Getter
        private SlayerLevelRequirement slayer;

        @Getter
        @SerializedName("heart_of_the_mountain")
        private TierRequirement heartOfTheMountain;
    }

    public static class ItemCatacombsRequirements {
        @Getter
        private TypeLevelRequirement dungeon;
    }

    public static class TypeLevelRequirement {
        @Getter
        private String type;

        @Getter
        private int level;
    }

    public static class TypeTierRequirement {
        @Getter
        private String type;

        @Getter
        private int tier;
    }

    public static class SlayerLevelRequirement {
        @Getter
        @SerializedName("slayer_boss_type")
        private String slayerBossType;

        @Getter
        private int level;
    }

    public static class TierRequirement {
        @Getter
        private int tier;
    }

    public static class ItemEssence {
        @Getter
        @SerializedName("essence_type")
        private String essenceType;

        @Getter
        private List<Integer> costs;
    }
}
