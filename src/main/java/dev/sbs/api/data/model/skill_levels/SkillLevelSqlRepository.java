package dev.sbs.api.data.model.skill_levels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkillLevelSqlRepository extends SqlRepository<SkillLevelSqlModel> {

    public SkillLevelSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
