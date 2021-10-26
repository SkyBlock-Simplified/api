package dev.sbs.api.model.sql.potions;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionRepository extends SqlRepository<PotionSqlModel> {

    public PotionRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PotionRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
