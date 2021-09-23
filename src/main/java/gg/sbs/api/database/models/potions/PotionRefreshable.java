package gg.sbs.api.database.models.potions;

import gg.sbs.api.database.SqlRefreshable;

import static gg.sbs.api.util.Consts.ONE_MINUTE_MS;

public class PotionRefreshable extends SqlRefreshable<PotionModel, PotionRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public PotionRefreshable() {
        super(PotionRepository.class, fixedRateMs);
    }
}
