package gg.sbs.api.database.models.accessories;

import gg.sbs.api.database.SqlRefreshable;

import static gg.sbs.api.util.Consts.ONE_MINUTE_MS;

public class AccessoryRefreshable extends SqlRefreshable<AccessoryModel, AccessoryRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public AccessoryRefreshable() {
        super(AccessoryRepository.class, fixedRateMs);
    }
}