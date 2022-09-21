package dev.sbs.api.data.model.skyblock.shop_data.shop_bit_enchanted_books;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.enchantment_data.enchantments.EnchantmentModel;

public interface ShopBitEnchantedBookModel extends Model {

    EnchantmentModel getEnchantment();

    Integer getBitCost();

}
