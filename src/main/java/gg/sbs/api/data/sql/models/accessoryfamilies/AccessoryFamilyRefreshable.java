package gg.sbs.api.data.sql.models.accessoryfamilies;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class AccessoryFamilyRefreshable extends SqlRefreshable<AccessoryFamilyModel, AccessoryFamilyRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public AccessoryFamilyRefreshable() {
        super(AccessoryFamilyRepository.class, fixedRateMs);
    }
}
