package gg.sbs.api.data.sql.models.locationareas;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class LocationAreaRefreshable extends SqlRefreshable<LocationAreaModel, LocationAreaRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public LocationAreaRefreshable() {
        super(fixedRateMs);
    }

}