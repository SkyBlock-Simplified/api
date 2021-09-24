package gg.sbs.api.data.sql.models.reforges;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class ReforgeRefreshable extends SqlRefreshable<ReforgeModel, ReforgeRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public ReforgeRefreshable() {
        super(ReforgeRepository.class, fixedRateMs);
    }
}
