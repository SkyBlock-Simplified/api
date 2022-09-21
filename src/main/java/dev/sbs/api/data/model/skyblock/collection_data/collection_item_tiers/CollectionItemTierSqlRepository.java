package dev.sbs.api.data.model.skyblock.collection_data.collection_item_tiers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class CollectionItemTierSqlRepository extends SqlRepository<CollectionItemTierSqlModel> {

    public CollectionItemTierSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
