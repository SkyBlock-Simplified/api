package gg.sbs.api.data.sql.models.formats;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class FormatRefreshable extends SqlRefreshable<FormatModel, FormatRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public FormatRefreshable() {
        super(fixedRateMs);
    }

}