package dev.sbs.api.data.sql.model.collectionitemtiers;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CollectionItemTierRepository extends SqlRepository<CollectionItemTierModel> {

    public CollectionItemTierRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public CollectionItemTierRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
