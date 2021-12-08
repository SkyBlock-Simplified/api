package dev.sbs.api.data.model.skyblock.item_stat_bonus;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface ItemStatBonusModel extends BuffEffectsModel<Double> {

    ItemModel getItem();

}
