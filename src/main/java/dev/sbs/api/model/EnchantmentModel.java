package dev.sbs.api.model;

import dev.sbs.api.data.Model;

import java.util.Map;

public interface EnchantmentModel extends Model {

    String getKey();

    String getName();

    ItemCategoryModel getItemType();

    int getItemLevel();

    Map<String, Object> getEffects();

}
