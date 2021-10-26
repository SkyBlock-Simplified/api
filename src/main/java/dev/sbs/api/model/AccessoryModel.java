package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.Map;

public interface AccessoryModel extends Model {

    ItemModel getItem();

    String getName();

    RarityModel getRarity();

    AccessoryFamilyModel getFamily();

    int getFamilyRank();

    Map<String, Object> getEffects();

}
