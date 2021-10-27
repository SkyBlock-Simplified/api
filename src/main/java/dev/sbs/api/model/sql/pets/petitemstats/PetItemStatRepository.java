package dev.sbs.api.model.sql.pets.petitemstats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetItemStatRepository extends SqlRepository<PetItemStatSqlModel> {

    public PetItemStatRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetItemStatRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
