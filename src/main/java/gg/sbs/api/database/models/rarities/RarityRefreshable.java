package gg.sbs.api.database.models.rarities;

import gg.sbs.api.database.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class RarityRefreshable extends SqlRefreshable<RarityModel, RarityRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public RarityRefreshable() {
        super(RarityRepository.class, fixedRateMs);
    }
}
