package dev.sbs.api.model.sql.pets.petabilities;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetAbilityRepository extends SqlRepository<PetAbilitySqlModel> {

    public PetAbilityRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetAbilityRepository(@NonNull SqlSession sqlSession, long fixedRateMs) {
        super(sqlSession, fixedRateMs);
    }

}
