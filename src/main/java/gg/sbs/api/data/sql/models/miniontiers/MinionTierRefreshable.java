package gg.sbs.api.data.sql.models.miniontiers;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class MinionTierRefreshable extends SqlRefreshable<MinionTierModel, MinionTierRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public MinionTierRefreshable() {
        super(fixedRateMs);
    }

}