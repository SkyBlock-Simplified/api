package dev.sbs.api.data.model.pet_abilities;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetAbilitySqlRepository extends SqlRepository<PetAbilitySqlModel> {

    public PetAbilitySqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetAbilitySqlRepository(@NonNull SqlSession sqlSession, long fixedRateMs) {
        super(sqlSession, fixedRateMs);
    }

}
