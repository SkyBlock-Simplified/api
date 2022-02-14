package dev.sbs.api.data.model.skyblock.skills;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SkillSqlRepository extends SqlRepository<SkillSqlModel> {

    public SkillSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
