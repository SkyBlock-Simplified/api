package dev.sbs.api.data.model.collection_item_tiers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CollectionItemTierSqlRepository extends SqlRepository<CollectionItemTierSqlModel> {

    public CollectionItemTierSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public CollectionItemTierSqlRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
