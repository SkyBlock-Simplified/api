package dev.sbs.api.data.model.skyblock.craftingtable_data.craftingtable_slots;

import dev.sbs.api.data.model.Model;

public interface CraftingTableSlotModel extends Model {

    String getKey();

    String getName();

    Integer getSlot();

    boolean isQuickCraft();

}