package dev.sbs.api.data.model.skyblock.collection_items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class CollectionItemSqlRepository extends SqlRepository<CollectionItemSqlModel> {

    public CollectionItemSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
