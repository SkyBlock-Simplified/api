package gg.sbs.api.data.sql.models.fairysouls;

import gg.sbs.api.data.sql.SqlRefreshable;
import gg.sbs.api.data.sql.models.locations.LocationModel;
import gg.sbs.api.data.sql.models.locations.LocationRepository;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class FairySoulRefreshable extends SqlRefreshable<FairySoulModel, FairySoulRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public FairySoulRefreshable() {
        super(fixedRateMs);
    }

}