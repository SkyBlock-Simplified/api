package gg.sbs.api.data.sql.models.itemtypes;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class ItemTypeRefreshable extends SqlRefreshable<ItemTypeModel, ItemTypeRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public ItemTypeRefreshable() {
        super(fixedRateMs);
    }
}