package gg.sbs.api.data.sql.skilllevels;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.data.sql.models.skilllevels.SkillLevelModel;
import gg.sbs.api.data.sql.models.skilllevels.SkillLevelRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SkillLevelRepositoryTest {
    private static final SkillLevelRepository skillLevelRepository;

    static {
        SimplifiedApi.enableDatabase();
        skillLevelRepository = SimplifiedApi.getSqlRepository(SkillLevelRepository.class);
    }

    @Test
    public void findAll_ok() {
        List<SkillLevelModel> all = skillLevelRepository.findAll();
        MatcherAssert.assertThat(all.size(), Matchers.greaterThan(0));
    }
}
