package dev.sbs.api.data.model.discord.sbs_beta_testers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SbsBetaTesterSqlRepository extends SqlRepository<SbsBetaTesterSqlModel> {

    public SbsBetaTesterSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
