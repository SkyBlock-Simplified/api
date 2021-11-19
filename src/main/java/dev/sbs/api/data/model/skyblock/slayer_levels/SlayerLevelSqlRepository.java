package dev.sbs.api.data.model.skyblock.slayer_levels;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SlayerLevelSqlRepository extends SqlRepository<SlayerLevelSqlModel> {

    public SlayerLevelSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
