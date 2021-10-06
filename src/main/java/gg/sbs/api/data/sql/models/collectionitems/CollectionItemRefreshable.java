package gg.sbs.api.data.sql.models.collectionitems;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class CollectionItemRefreshable extends SqlRefreshable<CollectionItemModel, CollectionItemRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public CollectionItemRefreshable() {
        super(fixedRateMs);
    }

}