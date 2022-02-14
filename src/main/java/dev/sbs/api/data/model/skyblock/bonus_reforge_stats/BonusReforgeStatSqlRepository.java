package dev.sbs.api.data.model.skyblock.bonus_reforge_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BonusReforgeStatSqlRepository extends SqlRepository<BonusReforgeStatSqlModel> {

    public BonusReforgeStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
