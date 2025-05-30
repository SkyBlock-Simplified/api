package dev.sbs.api.data.model.skyblock.accessory_data.accessories;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.accessory_data.accessory_families.AccessoryFamilyModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;

public interface AccessoryModel extends EffectsModel<Double> {

    ItemModel getItem();

    String getName();

    RarityModel getRarity();

    AccessoryFamilyModel getFamily();

    Integer getFamilyRank();

}
