package dev.sbs.api.data.model.skyblock.collections;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class CollectionSqlRepository extends SqlRepository<CollectionSqlModel> {

    public CollectionSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
