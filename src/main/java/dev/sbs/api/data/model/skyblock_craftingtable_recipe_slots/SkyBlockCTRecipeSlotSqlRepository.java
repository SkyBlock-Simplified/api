package dev.sbs.api.data.model.skyblock_craftingtable_recipe_slots;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockCTRecipeSlotSqlRepository extends SqlRepository<SkyBlockCTRecipeSlotSqlModel> {

    public SkyBlockCTRecipeSlotSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public SkyBlockCTRecipeSlotSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
