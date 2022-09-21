package dev.sbs.api.data.model.skyblock.pet_data.pet_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PetStatSqlRepository extends SqlRepository<PetStatSqlModel> {

    public PetStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
