package dev.sbs.api.data.model.pet_ability_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetAbilityStatSqlRepository extends SqlRepository<PetAbilityStatSqlModel> {

    public PetAbilityStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetAbilityStatSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
