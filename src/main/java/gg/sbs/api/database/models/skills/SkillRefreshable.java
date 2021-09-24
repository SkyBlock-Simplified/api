package gg.sbs.api.database.models.skills;

import gg.sbs.api.database.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class SkillRefreshable extends SqlRefreshable<SkillModel, SkillRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public SkillRefreshable() {
        super(SkillRepository.class, fixedRateMs);
    }
}
