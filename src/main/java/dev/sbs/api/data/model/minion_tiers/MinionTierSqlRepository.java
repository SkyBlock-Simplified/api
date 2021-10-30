package dev.sbs.api.data.model.minion_tiers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionTierSqlRepository extends SqlRepository<MinionTierSqlModel> {

    public MinionTierSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public MinionTierSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
