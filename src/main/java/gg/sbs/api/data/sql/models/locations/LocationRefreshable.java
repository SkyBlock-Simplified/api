package gg.sbs.api.data.sql.models.locations;

import gg.sbs.api.data.sql.SqlRefreshable;
import gg.sbs.api.data.sql.models.items.ItemModel;
import gg.sbs.api.data.sql.models.items.ItemRepository;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class LocationRefreshable extends SqlRefreshable<LocationModel, LocationRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public LocationRefreshable() {
        super(fixedRateMs);
    }

}