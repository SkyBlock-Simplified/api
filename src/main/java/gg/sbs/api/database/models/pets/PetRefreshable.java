package gg.sbs.api.database.models.pets;

import gg.sbs.api.database.SqlRefreshable;

import static gg.sbs.api.util.Consts.ONE_MINUTE_MS;

public class PetRefreshable extends SqlRefreshable<PetModel, PetRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public PetRefreshable() {
        super(PetRepository.class, fixedRateMs);
    }
}
