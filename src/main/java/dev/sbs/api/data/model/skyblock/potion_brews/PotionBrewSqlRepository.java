package dev.sbs.api.data.model.skyblock.potion_brews;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionBrewSqlRepository extends SqlRepository<PotionBrewSqlModel> {

    public PotionBrewSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}