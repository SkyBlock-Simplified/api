package dev.sbs.api.data.model.skyblock.enchantments;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.enchantment_families.EnchantmentFamilyModel;

public interface EnchantmentModel extends Model {

    String getKey();

    String getName();

    EnchantmentFamilyModel getFamily();

    String getDescription();

    Integer getRequiredLevel();

}
