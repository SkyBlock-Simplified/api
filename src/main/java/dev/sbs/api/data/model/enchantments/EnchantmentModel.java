package dev.sbs.api.data.model.enchantments;

import dev.sbs.api.data.model.EffectsModel;
import dev.sbs.api.data.model.reforge_types.ReforgeTypeModel;

import java.util.Map;

public interface EnchantmentModel extends EffectsModel {

    String getKey();

    String getName();

    ReforgeTypeModel getItemType();

    Integer getRequiredLevel();

    Map<String, Object> getEffects();

}
