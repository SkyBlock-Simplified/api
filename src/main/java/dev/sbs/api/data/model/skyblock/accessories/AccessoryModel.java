package dev.sbs.api.data.model.skyblock.accessories;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.accessory_families.AccessoryFamilyModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;

public interface AccessoryModel extends EffectsModel<Double> {

    ItemModel getItem();

    String getName();

    RarityModel getRarity();

    AccessoryFamilyModel getFamily();

    Integer getFamilyRank();

    boolean isAttainable();

    default boolean isNotAttainable() {
        return !this.isAttainable();
    }

}
