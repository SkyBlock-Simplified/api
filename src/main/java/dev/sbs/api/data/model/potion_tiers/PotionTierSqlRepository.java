package dev.sbs.api.data.model.potion_tiers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionTierSqlRepository extends SqlRepository<PotionTierSqlModel> {

    public PotionTierSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
