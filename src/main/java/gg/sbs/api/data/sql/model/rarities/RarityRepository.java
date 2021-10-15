package gg.sbs.api.data.sql.model.rarities;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class RarityRepository extends SqlRepository<RarityModel> {

    public RarityRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public RarityRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}