package dev.sbs.api.data.model.skyblock.bonus_reforge_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BonusReforgeStatSqlRepository extends SqlRepository<BonusReforgeStatSqlModel> {

    public BonusReforgeStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
