package dev.sbs.api.model.sql.pets.pettypes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetTypeRepository extends SqlRepository<PetTypeSqlModel> {

    public PetTypeRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetTypeRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
