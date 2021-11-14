package dev.sbs.api.data.model.collections;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CollectionSqlRepository extends SqlRepository<CollectionSqlModel> {

    public CollectionSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
