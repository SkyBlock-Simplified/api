package dev.sbs.api.data.model.skyblock_craftingtable_recipes;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SkyBlockCTRecipeSqlRepository extends SqlRepository<SkyBlockCTRecipeSqlModel> {

    public SkyBlockCTRecipeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
