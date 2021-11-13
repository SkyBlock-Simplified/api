package dev.sbs.api.data.model.pet_scores;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PetScoreSqlRepository extends SqlRepository<PetScoreSqlModel> {

    public PetScoreSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
