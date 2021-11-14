package dev.sbs.api.data.model.stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class StatSqlRepository extends SqlRepository<StatSqlModel> {

    public StatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
