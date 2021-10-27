package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.Map;

public interface ReforgeModel extends Model {

    String getName();

    ItemCategoryModel getItemType();

    RarityModel getRarity();

    boolean isStone();

    Map<String, Object> getEffects();

}
