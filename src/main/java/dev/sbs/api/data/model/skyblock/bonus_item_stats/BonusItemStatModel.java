package dev.sbs.api.data.model.skyblock.bonus_item_stats;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface BonusItemStatModel extends BuffEffectsModel<Double> {

    ItemModel getItem();

}
