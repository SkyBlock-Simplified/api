package gg.sbs.api.data.sql.skilllevels;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.data.sql.exception.SqlException;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeModel;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeRefreshable;
import gg.sbs.api.data.sql.models.skilllevels.SkillLevelModel;
import gg.sbs.api.data.sql.models.skilllevels.SkillLevelRefreshable;
import gg.sbs.api.data.sql.models.skills.SkillModel;
import gg.sbs.api.data.sql.models.skills.SkillRefreshable;
import gg.sbs.api.util.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SkillLevelRefreshableTest {
    private static final SkillRefreshable skillRefreshable;
    private static final SkillLevelRefreshable skillLevelRefreshable;

    static {
        SimplifiedApi.enableDatabase();
        skillRefreshable = SimplifiedApi.getSqlRefreshable(SkillRefreshable.class);
        skillLevelRefreshable = SimplifiedApi.getSqlRefreshable(SkillLevelRefreshable.class);
    }

    @Test
    public void getFirstOrNull_ok() throws SqlException {
        @SuppressWarnings({"unchecked"}) // Doesn't matter because findFirstOrNull uses generics
        SkillLevelModel existingSkillLevel = skillLevelRefreshable.findFirstOrNull(
                new Pair<>(SkillLevelModel::getSkill, skillRefreshable.findFirstOrNull(
                        SkillModel::getSkillKey, "FARMING"
                )),
                new Pair<>(SkillLevelModel::getSkillLevel, -1)
        );
        MatcherAssert.assertThat(existingSkillLevel, Matchers.nullValue());
    }
}
