package dev.sbs.api.model.sql.collections;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CollectionRepository extends SqlRepository<CollectionModel> {

    public CollectionRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public CollectionRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
