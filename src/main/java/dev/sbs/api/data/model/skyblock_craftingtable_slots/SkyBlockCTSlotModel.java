package dev.sbs.api.data.model.skyblock_craftingtable_slots;

import dev.sbs.api.data.model.Model;

public interface SkyBlockCTSlotModel extends Model {

    String getKey();

    String getName();

    Integer getSlot();

    boolean isQuickCraft();

}
