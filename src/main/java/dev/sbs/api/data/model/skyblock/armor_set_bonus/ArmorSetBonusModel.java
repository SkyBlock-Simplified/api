package dev.sbs.api.data.model.skyblock.armor_set_bonus;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface ArmorSetBonusModel extends BuffEffectsModel<Double> {

    String getKey();

    String getName();

    ItemModel getHelmetItem();

    ItemModel getChestplateItem();

    ItemModel getLeggingsItem();

    ItemModel getBootsItem();

}
