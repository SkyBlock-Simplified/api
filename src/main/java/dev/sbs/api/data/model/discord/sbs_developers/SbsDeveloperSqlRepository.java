package dev.sbs.api.data.model.discord.sbs_developers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SbsDeveloperSqlRepository extends SqlRepository<SbsDeveloperSqlModel> {

    public SbsDeveloperSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
