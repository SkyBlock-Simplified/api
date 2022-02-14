package dev.sbs.api.data.model.skyblock.seasons;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SeasonSqlRepository extends SqlRepository<SeasonSqlModel> {

    public SeasonSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
