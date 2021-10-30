package dev.sbs.api.data.model.pet_item_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetItemStatSqlRepository extends SqlRepository<PetItemStatSqlModel> {

    public PetItemStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetItemStatSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
