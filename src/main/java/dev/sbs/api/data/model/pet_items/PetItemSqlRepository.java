package dev.sbs.api.data.model.pet_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetItemSqlRepository extends SqlRepository<PetItemSqlModel> {

    public PetItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetItemSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}