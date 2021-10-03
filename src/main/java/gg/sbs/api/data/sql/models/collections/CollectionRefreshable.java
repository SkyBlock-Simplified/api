package gg.sbs.api.data.sql.models.collections;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class CollectionRefreshable extends SqlRefreshable<CollectionModel, CollectionRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public CollectionRefreshable() {
        super(CollectionRepository.class, fixedRateMs);
    }
}
