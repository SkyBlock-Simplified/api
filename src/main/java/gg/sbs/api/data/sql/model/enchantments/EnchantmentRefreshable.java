package gg.sbs.api.data.sql.model.enchantments;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class EnchantmentRefreshable extends SqlRefreshable<EnchantmentModel, EnchantmentRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public EnchantmentRefreshable() {
        super(fixedRateMs);
    }

}