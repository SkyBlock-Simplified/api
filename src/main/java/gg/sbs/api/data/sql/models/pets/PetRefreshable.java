package gg.sbs.api.data.sql.models.pets;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class PetRefreshable extends SqlRefreshable<PetModel, PetRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public PetRefreshable() {
        super(fixedRateMs);
    }
}
