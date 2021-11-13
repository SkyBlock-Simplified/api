package dev.sbs.api.data.model.skills;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkillSqlRepository extends SqlRepository<SkillSqlModel> {

    public SkillSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
