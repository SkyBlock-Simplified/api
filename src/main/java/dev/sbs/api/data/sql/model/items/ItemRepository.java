package dev.sbs.api.data.sql.model.items;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class ItemRepository extends SqlRepository<ItemModel> {

    public ItemRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public ItemRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
