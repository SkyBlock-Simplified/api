package dev.sbs.api.data.model.skyblock.sacks;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SackSqlRepository extends SqlRepository<SackSqlModel> {

    public SackSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
