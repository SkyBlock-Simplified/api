package gg.sbs.api.data.sql.model.accessories;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class AccessoryRefreshable extends SqlRefreshable<AccessoryModel, AccessoryRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public AccessoryRefreshable() {
        super(fixedRateMs);
    }

}