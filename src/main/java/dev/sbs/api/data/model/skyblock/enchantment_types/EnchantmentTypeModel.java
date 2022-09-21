package dev.sbs.api.data.model.skyblock.enchantment_types;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentModel;

import java.util.List;

public interface EnchantmentTypeModel extends Model {

    EnchantmentModel getEnchantment();

    List<String> getItemTypes();

}
