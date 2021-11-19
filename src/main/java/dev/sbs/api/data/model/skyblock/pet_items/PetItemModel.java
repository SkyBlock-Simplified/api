package dev.sbs.api.data.model.skyblock.pet_items;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

import java.util.Map;

public interface PetItemModel extends EffectsModel {

    ItemModel getItem();

    String getDescription();

    boolean isPercentage();

    Map<String, Object> getBuffEffects();

}
