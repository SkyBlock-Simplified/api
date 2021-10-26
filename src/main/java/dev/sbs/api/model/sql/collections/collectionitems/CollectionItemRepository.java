package dev.sbs.api.model.sql.collections.collectionitems;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CollectionItemRepository extends SqlRepository<CollectionItemSqlModel> {

    public CollectionItemRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public CollectionItemRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
