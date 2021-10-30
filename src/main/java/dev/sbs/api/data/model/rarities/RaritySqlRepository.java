package dev.sbs.api.data.model.rarities;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class RaritySqlRepository extends SqlRepository<RaritySqlModel> {

    public RaritySqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public RaritySqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
