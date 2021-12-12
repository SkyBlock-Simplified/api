package dev.sbs.api.data.model.skyblock.shop_bit_enchanted_books;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentModel;

public interface ShopBitEnchantedBookModel extends Model {

    EnchantmentModel getEnchantment();

    Integer getBitCost();

}
