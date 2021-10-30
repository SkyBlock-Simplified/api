package dev.sbs.api.data.model.accessories;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.accessory_families.AccessoryFamilyModel;
import dev.sbs.api.data.model.items.ItemModel;
import dev.sbs.api.data.model.rarities.RarityModel;

import java.util.Map;

public interface AccessoryModel extends EffectsModel {

    ItemModel getItem();

    String getName();

    RarityModel getRarity();

    AccessoryFamilyModel getFamily();

    int getFamilyRank();

    Map<String, Object> getEffects();

}
