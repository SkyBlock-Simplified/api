package dev.sbs.api.data.model.pet_exp_scales;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetExpScaleSqlRepository extends SqlRepository<PetExpScaleSqlModel> {

    public PetExpScaleSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetExpScaleSqlRepository(@NonNull SqlSession sqlSession, long fixedRateMs) {
        super(sqlSession, fixedRateMs);
    }

}