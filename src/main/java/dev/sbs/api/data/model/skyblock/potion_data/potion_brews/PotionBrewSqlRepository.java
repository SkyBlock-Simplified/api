package dev.sbs.api.data.model.skyblock.potion_data.potion_brews;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PotionBrewSqlRepository extends SqlRepository<PotionBrewSqlModel> {

    public PotionBrewSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}