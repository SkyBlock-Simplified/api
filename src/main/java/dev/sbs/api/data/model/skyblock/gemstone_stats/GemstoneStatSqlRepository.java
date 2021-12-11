package dev.sbs.api.data.model.skyblock.gemstone_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class GemstoneStatSqlRepository extends SqlRepository<GemstoneStatSqlModel> {

    public GemstoneStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
