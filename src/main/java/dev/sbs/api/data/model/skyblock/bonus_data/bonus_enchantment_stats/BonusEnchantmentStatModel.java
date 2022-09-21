package dev.sbs.api.data.model.skyblock.bonus_data.bonus_enchantment_stats;

import dev.sbs.api.data.model.BuffEffectsModel;
import dev.sbs.api.data.model.skyblock.enchantment_data.enchantments.EnchantmentModel;

public interface BonusEnchantmentStatModel extends BuffEffectsModel<Object, Double> {

    EnchantmentModel getEnchantment();

}
