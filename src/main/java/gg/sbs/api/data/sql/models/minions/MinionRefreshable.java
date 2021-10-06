package gg.sbs.api.data.sql.models.minions;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class MinionRefreshable extends SqlRefreshable<MinionModel, MinionRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public MinionRefreshable() {
        super(fixedRateMs);
    }

}