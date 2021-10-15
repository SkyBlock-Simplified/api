package gg.sbs.api.data.sql.model.collections;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CollectionRepository extends SqlRepository<CollectionModel> {

    public CollectionRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public CollectionRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}