package gg.sbs.api.data.sql.models.items;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class ItemRefreshable extends SqlRefreshable<ItemModel, ItemRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public ItemRefreshable() {
        super(ItemRepository.class, fixedRateMs);
    }
}
