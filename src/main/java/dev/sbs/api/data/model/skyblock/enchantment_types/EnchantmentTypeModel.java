package dev.sbs.api.data.model.skyblock.enchantment_types;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentModel;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeModel;

public interface EnchantmentTypeModel extends Model {

    EnchantmentModel getEnchantment();

    ReforgeTypeModel getReforgeType();

}
