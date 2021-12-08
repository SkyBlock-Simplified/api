package dev.sbs.api.data.model.skyblock.item_stat_bonus;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ItemStatBonusSqlRepository extends SqlRepository<ItemStatBonusSqlModel> {

    public ItemStatBonusSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
