package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.NbtContent;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.linked.ConcurrentLinkedMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Inventory {

    @SerializedName("inv_armor")
    private NbtContent armor;
    @SerializedName("equipment_contents")
    private NbtContent equipment;
    @SerializedName("wardrobe_contents")
    private NbtContent wardrobe;
    @SerializedName("bag_contents")
    private BagContents bags;
    @SerializedName("inv_contents")
    private NbtContent content;
    @SerializedName("wardrobe_equipped_slot")
    private int equippedWardrobeSlot;
    @SerializedName("backpack_icons")
    private ConcurrentMap<Integer, NbtContent> backpackIcons = Concurrent.newMap();
    @SerializedName("personal_vault_contents")
    private NbtContent personalVault;
    @SerializedName("sacks_counts")
    private ConcurrentLinkedMap<String, Integer> sacks = Concurrent.newLinkedMap();
    @SerializedName("backpack_contents")
    private ConcurrentMap<Integer, NbtContent> backpacks = Concurrent.newMap();
    @SerializedName("ender_chest_contents")
    private NbtContent enderChest;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BagContents {

        @SerializedName("fishing_bag")
        private NbtContent fishing;
        private NbtContent quiver;
        @SerializedName("fishing_bag")
        private NbtContent accessories;
        @SerializedName("potion_bag")
        private NbtContent potions;

    }

}
