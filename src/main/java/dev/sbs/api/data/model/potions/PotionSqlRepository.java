package dev.sbs.api.data.model.potions;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionSqlRepository extends SqlRepository<PotionSqlModel> {

    public PotionSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PotionSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
