package dev.sbs.api.data.model.skyblock.potion_data.potions;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PotionSqlRepository extends SqlRepository<PotionSqlModel> {

    public PotionSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
