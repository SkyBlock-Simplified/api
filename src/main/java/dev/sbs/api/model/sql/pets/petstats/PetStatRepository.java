package dev.sbs.api.model.sql.pets.petstats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetStatRepository extends SqlRepository<PetStatSqlModel> {

    public PetStatRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PetStatRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
