package dev.sbs.api.data.model.skyblock.collection_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class CollectionItemSqlRepository extends SqlRepository<CollectionItemSqlModel> {

    public CollectionItemSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
