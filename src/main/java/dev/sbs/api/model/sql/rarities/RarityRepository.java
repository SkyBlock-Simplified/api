package dev.sbs.api.model.sql.rarities;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class RarityRepository extends SqlRepository<RarityModel> {

    public RarityRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public RarityRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
