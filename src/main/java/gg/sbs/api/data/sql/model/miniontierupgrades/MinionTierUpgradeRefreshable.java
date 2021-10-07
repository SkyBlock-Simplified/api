package gg.sbs.api.data.sql.model.miniontierupgrades;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class MinionTierUpgradeRefreshable extends SqlRefreshable<MinionTierUpgradeModel, MinionTierUpgradeRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public MinionTierUpgradeRefreshable() {
        super(fixedRateMs);
    }

}