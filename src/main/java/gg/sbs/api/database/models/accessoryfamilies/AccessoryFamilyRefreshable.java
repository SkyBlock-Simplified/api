package gg.sbs.api.database.models.accessoryfamilies;

import gg.sbs.api.database.SqlRefreshable;

import static gg.sbs.api.util.Consts.ONE_MINUTE_MS;

public class AccessoryFamilyRefreshable extends SqlRefreshable<AccessoryFamilyModel, AccessoryFamilyRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public AccessoryFamilyRefreshable() {
        super(AccessoryFamilyRepository.class, fixedRateMs);
    }
}
