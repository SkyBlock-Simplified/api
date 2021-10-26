package dev.sbs.api.model.sql.pets;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetRepository extends SqlRepository<PetModel> {

    public PetRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetRepository(@NonNull SqlSession sqlSession, long fixedRateMs) {
        super(sqlSession, fixedRateMs);
    }

}
