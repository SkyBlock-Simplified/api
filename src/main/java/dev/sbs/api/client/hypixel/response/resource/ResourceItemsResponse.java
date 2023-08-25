package dev.sbs.api.client.hypixel.response.resource;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceItemsResponse {

    @Getter private boolean success;
    @Getter private long lastUpdated;
    @Getter private ConcurrentList<Item> items = Concurrent.newList();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Item {

        @Getter private String id;
        @Getter private String name;
        @Getter private String material;
        @Getter private int durability;
        @Getter private String description;
        @SerializedName("tier")
        @Getter private String rarity;
        @SerializedName("category")
        @Getter private String itemType;
        @Getter private String color;

        @Getter private boolean glowing;
        @Getter private boolean unstackable;
        @Getter private boolean dungeonItem;
        @Getter private boolean museum;
        @SerializedName("can_have_attributes")
        @Getter private boolean attributable;
        @SerializedName("hidden_from_viewrecipe_command")
        @Getter private boolean hiddenFromViewrecipe;
        @SerializedName("salvageable_from_recipe")
        @Getter private boolean salvageableFromRecipe;
        @SerializedName("cannot_reforge")
        @Getter private boolean notReforgeable;
        @SerializedName("rift_transferrable")
        @Getter private boolean riftTransferrable;
        @SerializedName("lose_motes_value_on_transfer")
        @Getter private boolean riftLoseMotesValueOnTransfer;


        @SerializedName("motes_sell_price")
        @Getter private double riftMotesSellPrice;
        @SerializedName("npc_sell_price")
        @Getter private double npcSellPrice;
        @SerializedName("gear_score")
        @Getter private int gearScore;
        @Getter private String generator;
        @SerializedName("generator_tier")
        @Getter private int generatorTier;
        @SerializedName("ability_damage_scaling")
        @Getter private double abilityDamageScaling;
        @Getter private String origin;
        @Getter private String soulbound;
        @Getter private String furniture;
        @SerializedName("sword_type")
        @Getter private String swordType;
        @Getter private String skin;
        @Getter private String crystal;
        @SerializedName("private_island")
        @Getter private String privateIsland;

        @Getter private ConcurrentMap<String, Double> stats = Concurrent.newMap();
        @SerializedName("tiered_stats")
        @Getter private ConcurrentMap<String, List<Double>> tieredStats = Concurrent.newMap();
        @Getter private ConcurrentList<ConcurrentMap<String, Object>> requirements = Concurrent.newList();
        @SerializedName("catacombs_requirements")
        @Getter private ConcurrentList<ConcurrentMap<String, Object>> catacombsRequirements;
        //@Getter private ConcurrentList<ItemCatacombsRequirements> catacombsRequirements;
        @SerializedName("upgrade_costs")
        @Getter private ConcurrentList<ConcurrentList<ConcurrentMap<String, Object>>> upgradeCosts = Concurrent.newList();
        @SerializedName("gemstone_slots")
        @Getter private ConcurrentList<ConcurrentMap<String, Object>> gemstoneSlots = Concurrent.newList();
        @Getter private ConcurrentMap<String, Double> enchantments = Concurrent.newMap();
        @SerializedName("dungeon_item_conversion_cost")
        @Getter private ConcurrentMap<String, Object> dungeonItemConversionCost = Concurrent.newMap();
        @Getter private ConcurrentMap<String, Object> prestige = Concurrent.newMap();
        @SerializedName("item_specific")
        @Getter private ConcurrentMap<String, Object> itemSpecific = Concurrent.newMap();
        @Getter private ConcurrentList<ConcurrentMap<String, Object>> salvages = Concurrent.newList();

    }


}
