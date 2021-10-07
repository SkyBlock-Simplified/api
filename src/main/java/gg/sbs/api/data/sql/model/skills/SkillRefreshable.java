package gg.sbs.api.data.sql.model.skills;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class SkillRefreshable extends SqlRefreshable<SkillModel, SkillRepository> {

    private static final long fixedRateMs = ONE_MINUTE_MS;

    public SkillRefreshable() {
        super(fixedRateMs);
    }

}