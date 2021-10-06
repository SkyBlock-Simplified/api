package gg.sbs.api.data.sql.models.collectionitemtiers;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class CollectionItemTierRefreshable extends SqlRefreshable<CollectionItemTierModel, CollectionItemTierRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public CollectionItemTierRefreshable() {
        super(fixedRateMs);
    }

}