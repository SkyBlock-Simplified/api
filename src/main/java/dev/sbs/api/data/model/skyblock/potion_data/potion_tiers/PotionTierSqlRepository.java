package dev.sbs.api.data.model.skyblock.potion_data.potion_tiers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PotionTierSqlRepository extends SqlRepository<PotionTierSqlModel> {

    public PotionTierSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
