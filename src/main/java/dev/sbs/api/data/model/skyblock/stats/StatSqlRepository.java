package dev.sbs.api.data.model.skyblock.stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class StatSqlRepository extends SqlRepository<StatSqlModel> {

    public StatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
