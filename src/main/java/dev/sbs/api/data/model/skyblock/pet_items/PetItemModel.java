package dev.sbs.api.data.model.skyblock.pet_items;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface PetItemModel extends BuffEffectsModel<Double> {

    ItemModel getItem();

    String getDescription();

    boolean isPercentage();

}
