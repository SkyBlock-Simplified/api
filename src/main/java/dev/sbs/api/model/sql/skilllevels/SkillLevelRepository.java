package dev.sbs.api.model.sql.skilllevels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkillLevelRepository extends SqlRepository<SkillLevelModel> {

    public SkillLevelRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public SkillLevelRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
