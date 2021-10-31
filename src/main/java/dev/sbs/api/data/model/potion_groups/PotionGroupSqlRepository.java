package dev.sbs.api.data.model.potion_groups;

import dev.sbs.api.data.model.potions.PotionSqlModel;
import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionGroupSqlRepository extends SqlRepository<PotionGroupSqlModel> {

    public PotionGroupSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public PotionGroupSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
