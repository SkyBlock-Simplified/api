package dev.sbs.api.model.sql.slayers.slayerlevels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SlayerLevelRepository extends SqlRepository<SlayerLevelSqlModel> {

    public SlayerLevelRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public SlayerLevelRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
