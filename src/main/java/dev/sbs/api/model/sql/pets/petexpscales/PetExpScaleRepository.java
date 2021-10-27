package dev.sbs.api.model.sql.pets.petexpscales;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetExpScaleRepository extends SqlRepository<PetExpScaleSqlModel> {

    public PetExpScaleRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetExpScaleRepository(@NonNull SqlSession sqlSession, long fixedRateMs) {
        super(sqlSession, fixedRateMs);
    }

}
