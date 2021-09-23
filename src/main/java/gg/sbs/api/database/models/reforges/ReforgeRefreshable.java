package gg.sbs.api.database.models.reforges;

import gg.sbs.api.database.SqlRefreshable;

import static gg.sbs.api.util.Consts.ONE_MINUTE_MS;

public class ReforgeRefreshable extends SqlRefreshable<ReforgeModel, ReforgeRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public ReforgeRefreshable() {
        super(ReforgeRepository.class, fixedRateMs);
    }
}
