package dev.sbs.api.data.model.skyblock.pet_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetStatSqlRepository extends SqlRepository<PetStatSqlModel> {

    public PetStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
