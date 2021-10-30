package dev.sbs.api.data.model.slayer_levels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SlayerLevelSqlRepository extends SqlRepository<SlayerLevelSqlModel> {

    public SlayerLevelSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public SlayerLevelSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
