package dev.sbs.api.data.sql.model.miniontiers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MinionTierRepository extends SqlRepository<MinionTierModel> {

    public MinionTierRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public MinionTierRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
