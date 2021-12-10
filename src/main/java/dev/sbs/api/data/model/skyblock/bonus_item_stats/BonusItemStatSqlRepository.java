package dev.sbs.api.data.model.skyblock.bonus_item_stats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class BonusItemStatSqlRepository extends SqlRepository<BonusItemStatSqlModel> {

    public BonusItemStatSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
