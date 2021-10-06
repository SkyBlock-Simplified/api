package gg.sbs.api.data.sql.models.minionitems;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class MinionItemRefreshable extends SqlRefreshable<MinionItemModel, MinionItemRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public MinionItemRefreshable() {
        super(fixedRateMs);
    }

}