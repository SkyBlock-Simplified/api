package dev.sbs.api.data.model.skyblock.item_stat_bonus;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

import java.util.Map;

public interface ItemStatBonusModel extends EffectsModel {

    ItemModel getItem();

    Map<String, ?> getBuffEffects();

}
