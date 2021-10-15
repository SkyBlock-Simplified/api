package gg.sbs.api.data.sql.model.potions;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionRepository extends SqlRepository<PotionModel> {

    public PotionRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PotionRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}