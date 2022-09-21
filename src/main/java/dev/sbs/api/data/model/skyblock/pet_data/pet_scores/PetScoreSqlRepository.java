package dev.sbs.api.data.model.skyblock.pet_data.pet_scores;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PetScoreSqlRepository extends SqlRepository<PetScoreSqlModel> {

    public PetScoreSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
