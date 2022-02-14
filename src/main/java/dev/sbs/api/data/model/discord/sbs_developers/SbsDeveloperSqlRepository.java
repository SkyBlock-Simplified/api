package dev.sbs.api.data.model.discord.sbs_developers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SbsDeveloperSqlRepository extends SqlRepository<SbsDeveloperSqlModel> {

    public SbsDeveloperSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
