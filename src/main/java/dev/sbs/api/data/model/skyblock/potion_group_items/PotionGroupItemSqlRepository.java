package dev.sbs.api.data.model.skyblock.potion_group_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class PotionGroupItemSqlRepository extends SqlRepository<PotionGroupItemSqlModel> {

    public PotionGroupItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
