package dev.sbs.api.model.sql.pets.petabilitystats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetAbilityStatRepository extends SqlRepository<PetAbilityStatSqlModel> {

    public PetAbilityStatRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetAbilityStatRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
