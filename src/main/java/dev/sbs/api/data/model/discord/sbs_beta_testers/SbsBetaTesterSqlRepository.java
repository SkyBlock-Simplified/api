package dev.sbs.api.data.model.discord.sbs_beta_testers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SbsBetaTesterSqlRepository extends SqlRepository<SbsBetaTesterSqlModel> {

    public SbsBetaTesterSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
