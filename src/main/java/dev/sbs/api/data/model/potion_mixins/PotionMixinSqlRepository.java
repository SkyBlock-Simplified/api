package dev.sbs.api.data.model.potion_mixins;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionMixinSqlRepository extends SqlRepository<PotionMixinSqlModel> {

    public PotionMixinSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
