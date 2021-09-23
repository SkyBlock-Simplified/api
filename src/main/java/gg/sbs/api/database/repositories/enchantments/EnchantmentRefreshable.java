package gg.sbs.api.database.repositories.enchantments;

import gg.sbs.api.database.repositories.SqlRefreshable;

public class EnchantmentRefreshable extends SqlRefreshable<EnchantmentModel, EnchantmentRepository> {
    public EnchantmentRefreshable() {
        super(EnchantmentRepository.class);
    }
}
