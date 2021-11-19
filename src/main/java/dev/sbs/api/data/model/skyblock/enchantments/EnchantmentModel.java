package dev.sbs.api.data.model.skyblock.enchantments;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeModel;

import java.util.Map;

public interface EnchantmentModel extends EffectsModel {

    String getKey();

    String getName();

    ReforgeTypeModel getItemType();

    int getRequiredLevel();

    Map<String, Object> getEffects();

}
