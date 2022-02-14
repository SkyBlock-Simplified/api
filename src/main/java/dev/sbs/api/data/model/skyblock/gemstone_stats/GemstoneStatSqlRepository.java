package dev.sbs.api.data.model.skyblock.gemstone_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class GemstoneStatSqlRepository extends SqlRepository<GemstoneStatSqlModel> {

    public GemstoneStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
