package dev.sbs.api.model.sql.items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ItemRepository extends SqlRepository<ItemSqlModel> {

    public ItemRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public ItemRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
