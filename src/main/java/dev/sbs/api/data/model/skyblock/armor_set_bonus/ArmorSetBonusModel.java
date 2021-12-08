package dev.sbs.api.data.model.skyblock.armor_set_bonus;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

import java.util.Map;

public interface ArmorSetBonusModel extends EffectsModel {

    String getKey();

    String getName();

    ItemModel getHelmetItem();

    ItemModel getChestplateItem();

    ItemModel getLeggingsItem();

    ItemModel getBootsItem();

    Map<String, ?> getBuffEffects();

}
