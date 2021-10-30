package dev.sbs.api.data.model.enchantments;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.reforge_types.ReforgeTypeModel;

import java.util.Map;

public interface EnchantmentModel extends EffectsModel {

    String getKey();

    String getName();

    ReforgeTypeModel getItemType();

    int getItemLevel();

    Map<String, Object> getEffects();

}
