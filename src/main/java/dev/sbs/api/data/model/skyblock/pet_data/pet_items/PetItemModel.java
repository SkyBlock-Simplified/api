package dev.sbs.api.data.model.skyblock.pet_data.pet_items;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface PetItemModel extends BuffEffectsModel<Object, Double> {

    ItemModel getItem();

    String getDescription();

    boolean isPercentage();

    default boolean notPercentage() {
        return !this.isPercentage();
    }

}
