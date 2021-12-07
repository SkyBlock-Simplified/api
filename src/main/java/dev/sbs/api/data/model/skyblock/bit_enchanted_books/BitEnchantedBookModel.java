package dev.sbs.api.data.model.skyblock.bit_enchanted_books;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentModel;

public interface BitEnchantedBookModel extends Model {

    EnchantmentModel getEnchantment();

    Integer getBitCost();

}
