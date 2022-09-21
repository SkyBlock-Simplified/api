package dev.sbs.api.data.model.skyblock.potion_data.potion_mixins;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PotionMixinSqlRepository extends SqlRepository<PotionMixinSqlModel> {

    public PotionMixinSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
