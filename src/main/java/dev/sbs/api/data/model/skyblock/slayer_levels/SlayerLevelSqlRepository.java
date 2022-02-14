package dev.sbs.api.data.model.skyblock.slayer_levels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SlayerLevelSqlRepository extends SqlRepository<SlayerLevelSqlModel> {

    public SlayerLevelSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
