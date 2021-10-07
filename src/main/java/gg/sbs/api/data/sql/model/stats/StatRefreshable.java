package gg.sbs.api.data.sql.model.stats;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class StatRefreshable extends SqlRefreshable<StatModel, StatRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public StatRefreshable() {
        super(fixedRateMs);
    }

}