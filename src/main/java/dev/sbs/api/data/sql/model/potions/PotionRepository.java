package dev.sbs.api.data.sql.model.potions;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionRepository extends SqlRepository<PotionModel> {

    public PotionRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PotionRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
