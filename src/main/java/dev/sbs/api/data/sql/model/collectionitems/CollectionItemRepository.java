package dev.sbs.api.data.sql.model.collectionitems;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CollectionItemRepository extends SqlRepository<CollectionItemModel> {

    public CollectionItemRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public CollectionItemRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
