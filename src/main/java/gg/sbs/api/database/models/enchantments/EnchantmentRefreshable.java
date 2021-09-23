package gg.sbs.api.database.models.enchantments;

import gg.sbs.api.database.SqlRefreshable;

import static gg.sbs.api.util.Consts.ONE_MINUTE_MS;

public class EnchantmentRefreshable extends SqlRefreshable<EnchantmentModel, EnchantmentRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public EnchantmentRefreshable() {
        super(EnchantmentRepository.class, fixedRateMs);
    }
}
