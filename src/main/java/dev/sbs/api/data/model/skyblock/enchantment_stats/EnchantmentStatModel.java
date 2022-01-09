package dev.sbs.api.data.model.skyblock.enchantment_stats;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentModel;
import dev.sbs.api.data.model.skyblock.stats.StatModel;

public interface EnchantmentStatModel extends Model {

    EnchantmentModel getEnchantment();

    StatModel getStat();

    String getBuffKey();

    Double getBaseValue();

    Double getLevelBonus();

    boolean isPercentage();

    default boolean notPercentage() {
        return !this.isPercentage();
    }

}
