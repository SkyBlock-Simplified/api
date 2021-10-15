package gg.sbs.api.data.sql.model.skilllevels;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkillLevelRepository extends SqlRepository<SkillLevelModel> {

    public SkillLevelRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public SkillLevelRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
