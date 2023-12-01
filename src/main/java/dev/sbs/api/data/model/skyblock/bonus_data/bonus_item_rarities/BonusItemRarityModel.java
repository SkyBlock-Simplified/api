package dev.sbs.api.data.model.skyblock.bonus_data.bonus_item_rarities;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface BonusItemRarityModel extends BuffEffectsModel<Object, Double> {

    ItemModel getItem();

}
