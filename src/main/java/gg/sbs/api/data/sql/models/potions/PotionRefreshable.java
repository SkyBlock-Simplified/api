package gg.sbs.api.data.sql.models.potions;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class PotionRefreshable extends SqlRefreshable<PotionModel, PotionRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public PotionRefreshable() {
        super(fixedRateMs);
    }

}