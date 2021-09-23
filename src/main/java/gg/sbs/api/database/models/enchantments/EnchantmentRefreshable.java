package gg.sbs.api.database.models.enchantments;

import gg.sbs.api.database.SqlRefreshable;

public class EnchantmentRefreshable extends SqlRefreshable<EnchantmentModel, EnchantmentRepository> {
    public EnchantmentRefreshable() {
        super(EnchantmentRepository.class);
    }
}
