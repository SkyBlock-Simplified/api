package dev.sbs.api.data.model.skyblock.bonus_data.bonus_item_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class BonusItemStatSqlRepository extends SqlRepository<BonusItemStatSqlModel> {

    public BonusItemStatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
