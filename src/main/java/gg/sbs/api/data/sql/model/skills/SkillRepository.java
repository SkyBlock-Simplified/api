package gg.sbs.api.data.sql.model.skills;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkillRepository extends SqlRepository<SkillModel> {

    public SkillRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public SkillRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}