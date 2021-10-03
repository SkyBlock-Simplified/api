package gg.sbs.api.data.sql.models.skilllevels;

import gg.sbs.api.data.sql.SqlRefreshable;

import static gg.sbs.api.util.TimeUtil.ONE_MINUTE_MS;

public class SkillLevelRefreshable extends SqlRefreshable<SkillLevelModel, SkillLevelRepository> {
    private static final long fixedRateMs = ONE_MINUTE_MS;

    public SkillLevelRefreshable() {
        super(SkillLevelRepository.class, fixedRateMs);
    }
}